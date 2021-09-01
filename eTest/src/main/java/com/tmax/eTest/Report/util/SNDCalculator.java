package com.tmax.eTest.Report.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Standard Normal Distribution Calculator
@Component
public class SNDCalculator {
	
	final double Z_90_PERCENT = 1.645;
	final double Z_95_PERCENT = 1.96;
	
	final double SELF_DIAG_MIN = 0;
	final double SELF_DIAG_MAX = 100;
	final double MINI_TEST_MIN = 0;
	final double MINI_TEST_MAX = 100;
	
	// 자가진단 CBT 분석 기초자료_20210813_v1.0.xlsx 에서 발췌
	final double[] MEAN_LIST = {71.0793, 81.1149, 65.6091, 67.6552}; // GI 지수, risk, invest, knowledge
	final double[] SD_LIST = {7.7330, 8.5672, 10.6003, 14.0570}; // GI 지수, risk, invest, knowledge
	
	public final int GI_IDX = 0;
	public final int RISK_IDX = 1;
	public final int INVEST_IDX = 2;
	public final int KNOWLEDGE_IDX = 3;
	
	
	

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public int calculateForDiagnosis(int typeIndex, int score)
	{
		return (int) Math.round( new NormalDistribution(
				MEAN_LIST[typeIndex],
				SD_LIST[typeIndex]).cumulativeProbability(score) * 100);
	}
	
	public int calculateForSelfDiag(double giScore)
	{
		return calculateForConf95Percent(SELF_DIAG_MIN, SELF_DIAG_MAX, giScore);
	}
	
	public int calculateForMiniTest(double ukAvgScore)
	{
		return calculateForConf90Percent(MINI_TEST_MIN, MINI_TEST_MAX, ukAvgScore);
	}
	
	private int calculateForConf90Percent(double min, double max, double rawX)
	{
		double diff = (max - min);
		double center = (max + min) / 2;
		double div = diff / (Z_90_PERCENT * 2);
		double x = (rawX - center) / div;
		
		
		return (int) Math.round((standard(x))*100);
	}
	
	private int calculateForConf95Percent(double min, double max, double rawX)
	{
		double diff = (max - min);
		double center = (max + min) / 2;
		double div = diff / (Z_95_PERCENT * 2);
		double x = (center - rawX) / div;
		
		return (int) Math.round((standard(x))*100);
	}
	
	
	private double standard(double x)
	{		
		return new NormalDistribution(0, 1).cumulativeProbability(x);
	}
	
//	private double norm(double x, double m, double d)
//	{
//		return ( 1 / (Math.sqrt(2*Math.PI)*d) *
//				Math.exp(-(x-m)*(x-m) / (2*d*d)));
//	}
}
