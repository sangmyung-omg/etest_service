package com.tmax.eTest.Test.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.tmax.eTest.Test.common.MasteryAPIManager;
import com.tmax.eTest.Test.dto.UKMasteryDTO;
import com.tmax.eTest.Test.model.UserEmbedding;
import com.tmax.eTest.Test.model.UserKnowledge;
import com.tmax.eTest.Test.model.UserKnowledgeKey;
import com.tmax.eTest.Test.repository.UserEmbeddingRepository;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class MasteryService {

	private ObjectMapper objectMapper = new ObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	MasteryAPIManager masteryAPIManager = new MasteryAPIManager();

	@Autowired
	UserEmbeddingRepository userEmbeddingRepository;

	@Autowired
	UserKnowledgeRepository userKnowledgeRepository;

	public Map<String, Object> getMastery(String userId, List<String> ukIdList) throws Exception {
		Map<String, Object> output = new HashMap<String, Object>();

		List<UKMasteryDTO> ukMasteryList = new ArrayList<UKMasteryDTO>();

		for (String ukId : ukIdList) {
			UserKnowledgeKey userKnowledgeKey = new UserKnowledgeKey();
			userKnowledgeKey.setUkId(ukId);
			userKnowledgeKey.setUserUuid(userId);
			UserKnowledge userKnowledge;
			try {
				userKnowledge = userKnowledgeRepository.findById(userKnowledgeKey)
						.orElseThrow(() -> new NoSuchElementException(ukId));
			} catch (NoSuchElementException e) {
				output.put("resultMessage", String.format("ukId = %s is not in USER_KNOWLEDGE TB.", e.getMessage()));
				return output;
			}
			UKMasteryDTO mastery = new UKMasteryDTO(ukId, userKnowledge.getUkMastery().toString());
			ukMasteryList.add(mastery);
		}

		output.put("resultMessage", "Successfully return uk mastery list.");
		output.put("ukMasteryList", ukMasteryList);

		return output;
	}

	public Map<String, String> updateMastery(Map<String, Object> input) throws Exception {
		Map<String, String> output = new HashMap<String, String>();
		String userId = (String) input.get("userId");
		@SuppressWarnings("unchecked")
		List<String> ukIdList = (List<String>) input.get("ukIdList");
		@SuppressWarnings("unchecked")
		List<String> correctList = (List<String>) input.get("correctList");
		@SuppressWarnings("unchecked")
		List<String> difficultyList = (List<String>) input.get("difficultyList");
		String userEmbedding = "";

		System.out.println("userId: " + userId);
		System.out.println("ukIdList: " + ukIdList);
		System.out.println("correctList: " + correctList);
		System.out.println("difficultyList: " + difficultyList);

		// check whether user embedding saved in UserEmbedding TB or not
		logger.info("Get user embedding...");
		Optional<UserEmbedding> userEmbeddingOptional = userEmbeddingRepository.findById(userId);
		if (userEmbeddingOptional.isPresent())
			userEmbedding = userEmbeddingOptional.get().getUserEmbedding();

		// Triton server HTTP request/response
		JsonObject tritonOutput;
		try {
			tritonOutput = masteryAPIManager.measureMastery(userId, ukIdList, correctList, difficultyList,
					userEmbedding);
		} catch (Exception e) {
			output.put("resultMessage", "Triton Internal Server Error: " + e.getMessage());
			return output;
		}
		JsonObject masteryJson = tritonOutput.get("Mastery").getAsJsonObject();
		userEmbedding = tritonOutput.get("Embeddings").toString();

		// update user mastery
		logger.info("Update mastery of user...");
		Set<UserKnowledge> userKnowledgeSet = new HashSet<UserKnowledge>();

		masteryJson.keySet().forEach(ukId -> {
			UserKnowledge userKnowledge = new UserKnowledge();
			userKnowledge.setUserUuid(userId);
			userKnowledge.setUkId(ukId);
			userKnowledge.setUkMastery(masteryJson.get(ukId).getAsFloat());
			userKnowledge.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
			userKnowledgeSet.add(userKnowledge);
		});
		userKnowledgeRepository.saveAll(userKnowledgeSet);

		// update user embedding
		logger.info("Update embedding of user...");
		UserEmbedding updateEmbedding = new UserEmbedding();
		updateEmbedding.setUserUuid(userId);
		updateEmbedding.setUserEmbedding(userEmbedding);
		updateEmbedding.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
		userEmbeddingRepository.save(updateEmbedding);

		output.put("resultMessage", "Successfully update user mastery.");
		return output;
	}

}
