package com.tmax.eTest.Report.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

// Standard Normal Distribution Calculator
@Component
@Log4j2
public class SNDCalculator {
	
	// 자가진단 CBT 분석 기초자료_20210813_v1.0.xlsx 에서 발췌
	final double[] MEAN_LIST = {70.7, 81.1149, 65.6091, 67.6552}; // GI 지수, risk, invest, knowledge
	final double[] SD_LIST = {7.5801, 8.5672, 10.6003, 14.0570}; // GI 지수, risk, invest, knowledge
	
	public final int GI_IDX = 0;
	public final int RISK_IDX = 1;
	public final int INVEST_IDX = 2;
	public final int KNOWLEDGE_IDX = 3;
	
	public int calculateForDiagnosis(int typeIndex, int score)
	{
		Long tempResult = Math.round( new NormalDistribution(
				MEAN_LIST[typeIndex],
				SD_LIST[typeIndex]).cumulativeProbability(score) * 100);
		
		int result = tempResult.intValue();
		
		// clamp 0 ~ 100 range
		result = (result < 0)? 0 
				: (result > 100) ? 100 
				: result;
		
		// lower percentage => upper percentage
		result = 100 - result;
		
		return result;
	}
	
	public int calculateForMiniTest(double ukAvgScore)
	{
		return calculateForConf90Percent(0, 100, ukAvgScore);
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
