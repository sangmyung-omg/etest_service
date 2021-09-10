package com.tmax.eTest.Report.util.minitest;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
// import java.util.concurrent.atomic.AtomicInteger;

import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.repository.problem.PartRepo;
import com.tmax.eTest.Common.repository.uk.UkMasterRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PartMapper {
    @Autowired private PartRepo partRepo;

    public static Map<String, Integer> map = new HashMap<>();

    private static final int PART_COUNT = 5;
    
    // static {
    //     map.put("가치변화의 요인", 1);
    //     map.put("금융투자상품 관리", 2);
    //     map.put("금융투자상품의 매매", 3);
    //     map.put("생애주기", 4);
    //     map.put("필요자금 마련방법", 5);
    //     map.put("행동편향", 6);
    // }

    //Build at application startup
    @EventListener
    public void startUpGenerator(ApplicationStartedEvent event){
        updateMapperData();
    }

    @Scheduled(fixedRate=60*60*1000)
    public void scheduledUpdater(){
        log.debug("Checking update in part section");
        updateMapperData();
    }

    private void updateMapperData() {
        List<Part> set = partRepo.findAll();

        //Default fallback
        if(set.size() == 0 ){
            map.put("금융투자 이해&금융투자 기본", 1);
            map.put("금융투자상품 - 주식", 2);
            map.put("금융투자상품 가치평가", 3);
            map.put("금융투자상품 보유관리", 4);
            map.put("리스크관리 및 행동편향", 5);
            return;
        }

        int index = 1;
        for(Part part : set){map.put(part.getPartName(), index++);}

        //Minimum => fill the rest
        while(index <= PART_COUNT){
            map.put("dummy" + index,  index++);
        }

        return;
    }
}