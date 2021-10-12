package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.List;

import com.tmax.eTest.Common.model.uk.UkRel;
import com.tmax.eTest.Common.repository.uk.UkRelRepo;
import com.tmax.eTest.Test.dto.UkRelOutputDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ModelUpdateService")
@Primary
public class ModelUpdateService {
    
    @Autowired
    UkRelRepo ukRelRepo;

    public UkRelOutputDTO getUkRelInfo() {
        UkRelOutputDTO output = new UkRelOutputDTO();

        List<Integer> baseUkList = new ArrayList<Integer>();
        List<Integer> preUkList = new ArrayList<Integer>();
        
        List<UkRel> re = ukRelRepo.findAll();
        log.info("UK Rel query result size : " + Integer.toString(re.size()));
        
        if (re == null | re.size() == 0) {
            log.info("warning: DB query result is empty!");
            output.setResultMessage("warning: DB query result is empty!");
            return output;
        }

        for (UkRel rel : re) {
            baseUkList.add(rel.getBaseUkId());
            preUkList.add(rel.getPreUkId());
        }

        output.setBaseUk(baseUkList);
        output.setPreUk(preUkList);
        output.setResultMessage("Successfully returned");

        return output;
    }
}
