package com.tmax.eTest.Report.util.minitest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
// import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired private UkMasterRepo ukMasterRepo;

    public static Map<String, Integer> map = new HashMap<>();
    
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
        Set<String> set = ukMasterRepo.getDistinctPartList();

        //Default fallback
        if(set.size() == 0 ){
            map.put("가치변화의 요인", 1);
            map.put("금융투자상품 관리", 2);
            map.put("금융투자상품의 매매", 3);
            map.put("생애주기", 4);
            map.put("필요자금 마련방법", 5);
            map.put("행동편향", 6);
            return;
        }

        int index = 1;
        for(String partname : set){map.put(partname, index++);}

        //Minimum => fill the rest
        while(index <= 6){
            map.put("dummy" + index,  index++);
        }

        return;
    }
}