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
	final double MINI_TEST_MIN = 15;
	final double MINI_TEST_MAX = 85;
	
	

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
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
