package com.tmax.eTest.Report.dto.minitest;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.HashMap;

//temp part mapper
class PartMapper {
    public static Map<String, Integer> map = new HashMap<>();
    
    static {
        map.put("가치변화의 요인", 1);
        map.put("금융투자상품 관리", 2);
        map.put("금융투자상품의 매매", 3);
        map.put("생애주기", 4);
        map.put("필요자금 마련방법", 5);
        map.put("행동편향", 6);
    }
}


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartInfoDTO {
    private PartDataDTO part1;    
    private PartDataDTO part2;
    private PartDataDTO part3;
    private PartDataDTO part4;
    private PartDataDTO part5;
    private PartDataDTO part6;

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
            case 6:
                this.part6 = data; break;
            default:
                break;
        }
    }
}
