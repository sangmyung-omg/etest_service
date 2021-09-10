package com.tmax.eTest.Report.dto.minitest;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
// import lombok.NoArgsConstructor;

import java.util.ArrayList;

import com.tmax.eTest.Report.util.minitest.PartMapper;

// import org.springframework.stereotype.Component;

// import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
// @NoArgsConstructor
public class PartInfoDTO {
    private PartDataDTO part1;    
    private PartDataDTO part2;
    private PartDataDTO part3;
    private PartDataDTO part4;
    private PartDataDTO part5;
    // private PartDataDTO part6;

    public PartInfoDTO() {
        this.initDefaultDatas();
    }

    public void setPartData(String key, PartDataDTO data){
        Integer index = PartMapper.map.get(key);
        switch(index){
            case 1:
                this.part1 = data; break;
            case 2:
                this.part2 = data; break;
            case 3:
                this.part3 = data; break;
            case 4:
                this.part4 = data; break;
            case 5:
                this.part5 = data; break;
            // case 6:
            //     this.part6 = data; break;
            default:
                break;
        }

        
    }

    private void initDefaultDatas(){
        PartMapper.map.keySet().stream().forEach(key -> {
            this.setPartData(key, PartDataDTO.builder().partName(key).ukInfo(new ArrayList<>()).build());
        });
    }
}
