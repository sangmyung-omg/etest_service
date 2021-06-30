package com.tmax.eTest.Report.dto.triton;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TritonRequestDTO {
    private String id;
    private List<TritonDataDTO> inputs;
    private List<TritonDataDTO> outputs;
    
    private void initOutputsDefault()
    {
    	if(outputs == null)
    		outputs = new ArrayList<TritonDataDTO>();
    	
    	TritonDataDTO masteryOutput = new TritonDataDTO();
    	masteryOutput.setName("Mastery");
    	outputs.add(masteryOutput);
    	
    	TritonDataDTO embedOutput = new TritonDataDTO();
    	embedOutput.setName("Embeddings");
    	outputs.add(embedOutput);

    }
    
    private void initInputsEmptyEmbedding()
    {
    	if(inputs == null)
    		inputs = new ArrayList<>();
    	
    	TritonDataDTO embedDTO = new TritonDataDTO();
    	List<Object> embedData = new ArrayList<>();
    	String temp = "";
    	embedData.add(temp);
    	embedDTO.setName("Embeddings");
    	embedDTO.setDatatype("BYTES");
    	embedDTO.setData(embedData);
    	embedDTO.setShape(Arrays.asList(embedData.size()));
    	inputs.add(embedDTO);

    }
    
    public void pushInputData(
    		String name, 
    		String dataType,
    		List<Object> dataList)
    {
    	if(inputs == null)
    		inputs = new ArrayList<>();
    	
    	TritonDataDTO data = new TritonDataDTO();
    	data.setName(name);
    	data.setDatatype(dataType);
    	data.setData(dataList);
    	data.setShape(Arrays.asList(dataList.size()));
    	
    }
    
    public void initDefault()
    {
    	id = "1";
    	
    	initOutputsDefault();
    	initInputsEmptyEmbedding();

    }
    
    public boolean initForDummy()
    {
    	id = "1";
    	
    	TritonDataDTO ukDTO = new TritonDataDTO();
    	List<Object> ukData = new ArrayList<Object>();
    	for(int i = 0; i < 3; i++)
    		ukData.add(30);
    	ukDTO.setName("UKList");
    	ukDTO.setDatatype("INT32");
    	ukDTO.setData(ukData);
    	ukDTO.setShape(Arrays.asList(ukData.size()));
    	
    	TritonDataDTO isCorrectDTO = new TritonDataDTO();
    	List<Object> isCorrectData = new ArrayList<Object>();
    	for(int i = 0; i < 3; i++)
    		isCorrectData.add(1);
    	isCorrectDTO.setName("IsCorrectList");
    	isCorrectDTO.setDatatype("INT32");
    	isCorrectDTO.setData(isCorrectData);
    	isCorrectDTO.setShape(Arrays.asList(isCorrectData.size()));
    	
    	TritonDataDTO diffDTO = new TritonDataDTO();
    	List<Object> diffData = new ArrayList<Object>();
    	diffData.add(1);
    	diffData.add(2);
    	diffData.add(3);
    	diffDTO.setName("DifficultyList");
    	diffDTO.setDatatype("INT32");
    	diffDTO.setData(diffData);
    	diffDTO.setShape(Arrays.asList(diffData.size()));
    	
 
    	initInputsEmptyEmbedding();
    	
    	inputs = new ArrayList<TritonDataDTO>();

    	inputs.add(ukDTO);
    	inputs.add(isCorrectDTO);
    	inputs.add(diffDTO);
    	
    	
    	initOutputsDefault();    	

    	return true;
    }
}
