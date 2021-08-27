package com.tmax.eTest.TestStudio.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.TestStudio.dto.PartListDTO;
import com.tmax.eTest.TestStudio.repository.PartRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PartService {

	private final PartRepository partRepository;

	public void create(String name, Integer order, Integer count) {
		Part part = new Part();
		part.setPartName(name);
		part.setOrderNum(order);
		part.setProblemCount(count);
		partRepository.save(part);
	}

	public List<PartListDTO> read() {
		List<Part> query = partRepository.findAll();
		return query.stream()
				.map(m -> new PartListDTO(m.getPartID(), m.getPartName(), m.getOrderNum(), m.getProblemCount()))
				.collect(Collectors.toList());
	}

	public void update(Integer id, String name, Integer order, Integer count) {
		Part part = partRepository.getById(id);
		part.setPartName(name);
		part.setOrderNum(order);
		part.setProblemCount(count);
		partRepository.save(part);
	}

	public void delete(Integer id) {
		partRepository.deleteById(id);
	}

}