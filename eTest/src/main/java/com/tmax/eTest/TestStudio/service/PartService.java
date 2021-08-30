package com.tmax.eTest.TestStudio.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.repository.problem.PartRepo;
import com.tmax.eTest.TestStudio.dto.PartListDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PartService {

	private final PartRepo partRepo;
	
	public void create(String name, Integer order, Integer count) {
		partRepo.save(new Part(name, order, count));
	}

	public List<PartListDTO> read() {
		return partRepo.findAll().stream().map(PartListDTO::new).collect(Collectors.toList());
	}

	public void update(Integer id, String name, Integer order, Integer count) {
		partRepo.getById(id).updatePart(name, order, count);
	}

	public void delete(Integer id) {
		partRepo.deleteById(id);
	}

}