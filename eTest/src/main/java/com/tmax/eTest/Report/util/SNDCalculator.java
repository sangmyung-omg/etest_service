package com.tmax.eTest.Report.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// Standard Normal Distribution Calculator
@Component
@Slf4j
public class SNDCalculator {
	
	private final int MIN_PERCENT = 1;
	private final int MAX_PERCENT = 100;
	
	
	public enum Type{
        DIAG_GI("gi"),
        DIAG_RISK("diag_risk"),
        DIAG_INVEST("invest"),
        DIAG_KNOWLEDGE("knowledge"),
        MINI_TOTAL("total"),
        MINI_BASIC("basic"),
        MINI_STOCK("stock"),
        MINI_VALUE("value"),
        MINI_POSSESSION("possession"),
        MINI_RISK("mini_risk");

        //@Getter
        private String value;
        
        private Type(String value){
            this.value = value;
        }

        public String toString(){
            return this.value;
        }
	}

	private static final Map<Type, Double> MEAN_MAP = new HashMap<>();
	private static final Map<Type, Double> SD_MAP = new HashMap<>();
	static {
		MEAN_MAP.put(Type.DIAG_GI, 71.);
		MEAN_MAP.put(Type.DIAG_RISK, 81.1149);
		MEAN_MAP.put(Type.DIAG_INVEST, 65.6091);
		MEAN_MAP.put(Type.DIAG_KNOWLEDGE, 67.6552);
		MEAN_MAP.put(Type.MINI_TOTAL, 51.6531);
		MEAN_MAP.put(Type.MINI_BASIC, 53.2712);
		MEAN_MAP.put(Type.MINI_STOCK, 53.0511);
		MEAN_MAP.put(Type.MINI_VALUE, 49.1177);
		MEAN_MAP.put(Type.MINI_POSSESSION, 50.4613);
		MEAN_MAP.put(Type.MINI_RISK, 52.3645);
		
		
		SD_MAP.put(Type.DIAG_GI, 7.7107);
		SD_MAP.put(Type.DIAG_RISK, 8.5425);
		SD_MAP.put(Type.DIAG_INVEST, 10.5698);
		SD_MAP.put(Type.DIAG_KNOWLEDGE, 13.9352	);
		SD_MAP.put(Type.MINI_TOTAL, 12.48);
		SD_MAP.put(Type.MINI_BASIC, 16.16);
		SD_MAP.put(Type.MINI_STOCK, 13.39);
		SD_MAP.put(Type.MINI_VALUE, 12.14);
		SD_MAP.put(Type.MINI_POSSESSION, 15.02);
		SD_MAP.put(Type.MINI_RISK, 17.10);
		
	}

	@Deprecated
	public int calculateForMiniTest(double ukAvgScore)
	{
		return calculateForConf90Percent(0, 100, ukAvgScore);
	}
	
	public int calculatePercentage(Type type, int score)
	{
		double mean = MEAN_MAP.get(type);
		double sd = SD_MAP.get(type);
		
		// 자가진단의 데이터가 너무 평균에 몰려있는 관계로. 자가진단 하위 50% 인 경우 분산 3배.
		if(score < mean && 
				(type == Type.DIAG_GI 
				|| type == Type.DIAG_RISK 
				|| type == Type.DIAG_INVEST 
				|| type == Type.DIAG_KNOWLEDGE))
			sd *= 3;
		
		Long tempResult = Math.round( 
				new NormalDistribution(mean, sd)
				.cumulativeProbability(score) * 100);
		
		int result = tempResult.intValue();
		
		// lower percentage => upper percentage
		result = MAX_PERCENT - result;
		
		// clamp 0 ~ 100 range
		result = (result < MIN_PERCENT)? MIN_PERCENT 
				: (result > MAX_PERCENT) ? MAX_PERCENT 
				: result;
		
		
		
		return result;
	}
	
	private int calculateForConf90Percent(double min, double max, double rawX)
	{
		double diff = (max - min);
		double center = (max + min) / 2;
		double div = diff / (1.645 * 2);
		double x = (rawX - center) / div;
		
		int result = (int) Math.round(new NormalDistribution(0, 1).cumulativeProbability(x)*100);
		// clamp 0 ~ 100 range
		result = (result < 0)? 0 
				: (result > 100) ? 100 
				: result;
		
		// lower percentage => upper percentage
		result = 100 - result;
		
		return result;
	}


}
