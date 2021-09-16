package com.tmax.eTest.Report.util.minitest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
// import java.util.concurrent.atomic.AtomicInteger;

import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.repository.problem.PartRepo;
import com.tmax.eTest.Report.util.SNDCalculator;

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
    public static Map<String, SNDCalculator.Type> sndCalcTypeMap = new HashMap<>();

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
        List<Part> set = partRepo.findAll();
        initRoutine(set);
    }

    @Scheduled(fixedRate=60*60*1000)
    public void scheduledUpdater(){
        log.debug("Checking update in part section");
        List<Part> set = partRepo.findAll();
        initRoutine(set);
    }

    private void initRoutine(List<Part> set) {
        if(set.size() < PART_COUNT){
            log.warn("Part count does not match expected size {}. Application might suffer from unexcepted side-effects. {}", PART_COUNT, set.toString());
        }

        updateMapperData(set);
        createSndCalcMap(set);
    }

    private void updateMapperData(List<Part> set) {
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

    private void createSndCalcMap(List<Part> set) {
        if(map.size() == 0)
            updateMapperData(set);


        //Copy list and append if shorter than PART_COUNT
        List<String> partNameList = set.stream().map(Part::getPartName).collect(Collectors.toList());
        if(partNameList.size() < PART_COUNT){
            int diff = PART_COUNT - partNameList.size();

            while(diff-- != 0){
                partNameList.add("dummy" + diff);
            }
        }

 
        sndCalcTypeMap.put(set.get(0).getPartName(), SNDCalculator.Type.MINI_BASIC); //금융투자 이해&금융투자 기본
        sndCalcTypeMap.put(set.get(1).getPartName(), SNDCalculator.Type.MINI_STOCK); //금융투자상품 - 주식
        sndCalcTypeMap.put(set.get(2).getPartName(), SNDCalculator.Type.MINI_VALUE); //금융투자상품 가치평가
        sndCalcTypeMap.put(set.get(3).getPartName(), SNDCalculator.Type.MINI_POSSESSION); //금융투자상품 보유관리
        sndCalcTypeMap.put(set.get(4).getPartName(), SNDCalculator.Type.MINI_RISK); //리스크관리 및 행동편향
    }
}