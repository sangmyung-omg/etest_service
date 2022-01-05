package com.tmax.eTest.Report.util.diagnosis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.Report.dto.comment.CommentInfoDTO;
import com.tmax.eTest.Report.util.diagnosis.CommentMapper.Type;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.InvestProfile;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.InvestTracing;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.KnowledgeSubSection;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.RiskProfile;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.RiskTracing;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DiagnosisComment {
	
	public Map<String, String> makeRiskMainComment(DiagnosisReport report)
	{		///
		Map<String, String> result = new HashMap<>();

		String riskComment = "";
		
		int profileIdx = (report.getRiskLevelScore() < 8) ? 0 : 1;			// 11~13 Question 3 ~ 7 / 8 ~ 12
		int tracingIdx = (report.getRiskStockRatioScore() < 3) ? 0 : 1;		// 2 Question 1,2 / 3,4
		int typeIdx = profileIdx * 2 + tracingIdx;
		int levelCapaDiff = report.getRiskLevelScore() - report.getRiskCapaScore();
		int investRiskComIdx = 0;
		if(profileIdx == 0)
			investRiskComIdx += (Math.abs(levelCapaDiff) < 3 ) ? 0	// -2 ~ 2
				: (levelCapaDiff > 0) ? 1							// 3, 4, 5
				: 2;												// -3, -4, -5
		else
			investRiskComIdx += (levelCapaDiff > -3 ) ? 3				// 0 ~ -2
					: (levelCapaDiff > -8) ? 4							// -3 ~ -7
					: 5;												// -8 ~ -10
		
		riskComment += CommentMapper.getComment(Type.RISK_COMBI, investRiskComIdx) + " ";
		
		int stockNumIdx = (report.getRiskStockNumScore() > 2) ? 0 : 1;		// number 3 question 점수 reverse 되어있음.
		int preferNumIdx = (report.getRiskStockPreferScore() < 3) ? 0 : 1;	// number 4 question
		int investMethodComIdx = tracingIdx * 4 + stockNumIdx * 2 + preferNumIdx;

		riskComment += CommentMapper.getComment(Type.RISK_METHOD, investMethodComIdx) + " ";
		riskComment += CommentMapper.getComment(Type.RISK_COMBI, investRiskComIdx * 8 + investMethodComIdx) + " ";

		
		result.put("main", CommentMapper.getComment(Type.RISK_TYPE, typeIdx));
		result.put("detail", riskComment);
		result.put("fixed", CommentMapper.getComment(Type.RISK_FIXED, 0));
		
		return result;
	}
	
	public List<CommentInfoDTO> makeRiskDetailComment(DiagnosisReport report)
	{
		int profLevelScore = (int)((report.getRiskLevelScore()-1.5) / 12. * 100 );
		int profCapaScore = (int)((report.getRiskCapaScore()) / 8. * 100);
		
		List<List<String>> profileDetailScore = new ArrayList<>();
		profileDetailScore.add(Arrays.asList(RiskProfile.LEVEL.toString(), String.valueOf(profLevelScore)));
		profileDetailScore.add(Arrays.asList(RiskProfile.CAPACITY.toString(), String.valueOf(profCapaScore)));

		List<List<String>> tracingDetailScore = new ArrayList<>();
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_PERIOD.toString(), 
				String.valueOf((report.getRiskInvestPeriodScore()-1)*33+1)));	// 0, 34, 67, 100 표현
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_RATIO.toString(), 
				String.valueOf(report.getRiskStockRatioScore()*20)));			// 20,40,60,80 표현
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_NUM.toString(), 
				String.valueOf((report.getRiskStockNumScore()-1)*33+1)));
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_PREFER.toString(), 
				String.valueOf((report.getRiskStockPreferScore()-1)*33+1)));
		
		CommentInfoDTO profileCommentInfo = CommentInfoDTO.builder()
				.name("투자위험 태도")
				.main((report.getRiskLevelScore() < 8)
				?CommentMapper.getComment(Type.RISK_PROFILE_MAIN, 0)
				:CommentMapper.getComment(Type.RISK_PROFILE_MAIN, 1))
				.detail("")
				.score(profLevelScore)
				.detailScoreList(profileDetailScore)
				.build();
		
		CommentInfoDTO tracingCommentInfo = CommentInfoDTO.builder()
				.name("투자 방법")
				.main((report.getRiskStockRatioScore() < 3)
				? CommentMapper.getComment(Type.RISK_TRACING_MAIN, 0)
				: CommentMapper.getComment(Type.RISK_TRACING_MAIN, 1))
				.detail("")
				.score(report.getRiskStockRatioScore()*20)
				.detailScoreList(tracingDetailScore)
				.build();
		
		List<CommentInfoDTO> result = new ArrayList<>();
		
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}


	public Map<String, String> makeInvestMainComment(int profileScore, int tracingScore)
	{
		
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (profileScore >= 40) ? 0
				: 1;
		
		int tracingIdx = (tracingScore >= 45) ? 0
				: 1;
		
		int typeIdx = profileIdx * 2 + tracingIdx;
		
		result.put("main", CommentMapper.getComment(Type.INVEST_TYPE, typeIdx));
		result.put("detail", CommentMapper.getComment(Type.INVEST_MAIN, typeIdx));
		
		return result;
	}
	
	public List<CommentInfoDTO> makeInvestDetailComment(DiagnosisReport report)
	{
		int profileScore = report.getInvestProfileScore(); 
		int tracingScore = report.getInvestTracingScore();
		
		int profileIdx = (profileScore >= 40) ? 0 : 1;
		int tracingIdx = (tracingScore >= 45) ? 0 : 1;
		
		int stretchProfileScore = (int)((profileScore - 20) / 25.f * 100);
		int stretchTracingScore = (int)((tracingScore - 17) / 38.f * 100);
		
		List<List<String>> profileDetailScore = new ArrayList<>();
		
		profileDetailScore.add(Arrays.asList(
				InvestProfile.BIAS_ANCHOR.toString(), 
				String.valueOf((report.getInvestAnchorScore() * 10))));
		profileDetailScore.add(Arrays.asList(
				InvestProfile.BIAS_SELF.toString(), 
				String.valueOf((report.getInvestSelfScore() * 10))));
		profileDetailScore.add(Arrays.asList(
				InvestProfile.BIAS_LOSS.toString(), 
				String.valueOf((report.getInvestLossScore() * 10))));
		profileDetailScore.add(Arrays.asList(
				InvestProfile.BIAS_CONFIRM.toString(), 
				String.valueOf((report.getInvestConfirmScore() * 10))));
		profileDetailScore.add(Arrays.asList(
				InvestProfile.BIAS_CROWN.toString(), 
				String.valueOf((report.getInvestCrownScore() * 10))));
		
		List<List<String>> tracingDetailScore = new ArrayList<>();
		
		tracingDetailScore.add(Arrays.asList(
				InvestTracing.RULE_METHOD.toString(), 
				String.valueOf((report.getInvestMethodScore() * 10))));
		tracingDetailScore.add(Arrays.asList(
				InvestTracing.RULE_SELL.toString(), 
				String.valueOf((report.getInvestSellScore() * 10))));
		tracingDetailScore.add(Arrays.asList(
				InvestTracing.RULE_PORTFOLIO.toString(), 
				String.valueOf((report.getInvestPortfolioScore() * 10))));
		tracingDetailScore.add(Arrays.asList(
				InvestTracing.RULE_INFO.toString(), 
				String.valueOf((report.getInvestInfoScore() * 10))));
		
		CommentInfoDTO profileCommentInfo = CommentInfoDTO.builder()
				.name("행동편향")
				.main(CommentMapper.getComment(Type.INVEST_PROFILE_MAIN, profileIdx))
				.detail("")
				.score(stretchProfileScore)
				.detailScoreList(tracingDetailScore)
				.build();
		CommentInfoDTO tracingCommentInfo = CommentInfoDTO.builder()
				.name("투자원칙")
				.main(CommentMapper.getComment(Type.INVEST_TRACING_MAIN, tracingIdx))
				.detail("")
				.score(stretchTracingScore)
				.detailScoreList(tracingDetailScore)
				.build();
		
		List<CommentInfoDTO> result = new ArrayList<>();
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}
	
	public Map<String, String> makeKnowledgeMainComment(
			int knowledgeScore,
			List<Problem> probList,
			List<StatementDTO> statementForDetail)
	{
		Map<String, String> result = new HashMap<>();
		int[] rankMinValue = {80, 60, 0};
		int knowledgeScoreIdx = rankMinValue.length - 1;
		
		String knowledgeDetail = "";
		
		for(int i = 0; i < rankMinValue.length; i++)
			if(knowledgeScore >= rankMinValue[i])
			{
				knowledgeScoreIdx = i;
				break;
			}
		
		// make detail comment
		if(probList != null && statementForDetail != null)
		{
			int minCurriCulumId = 999;
			
			Collections.sort(probList, new Comparator<Problem>() {
				@Override
				public int compare(Problem o1, Problem o2) {
					if(o1.getDiagnosisInfo().getCurriculumId() < o2.getDiagnosisInfo().getCurriculumId())
						return -1;
					else if(o1.getDiagnosisInfo().getCurriculumId() > o2.getDiagnosisInfo().getCurriculumId())
						return 1;
					return 0;
				}
			});
			
			for(Problem prob : probList)
				minCurriCulumId = (minCurriCulumId > prob.getDiagnosisInfo().getCurriculumId()) 
					? prob.getDiagnosisInfo().getCurriculumId() 
					: minCurriCulumId;
			
			for(Problem prob : probList)
			{
				boolean isCorrect = false;
				int diffIdx = prob.getDifficulty().equals("상") ? 0
						: prob.getDifficulty().equals("중") ? 1
						: 2;
				int probOrderIdx = prob.getDiagnosisInfo().getCurriculumId() - minCurriCulumId; // 16 == 지식 첫 문제 Curriculum ID

				for(StatementDTO state : statementForDetail)
					if(Integer.parseInt(state.getSourceId()) == prob.getProbID())
					{
						isCorrect = state.getIsCorrect() == 1;
						break;
					}
				
				int detIdx = 0;
				
				if(probOrderIdx == 1)
					detIdx = (diffIdx == 0) ? 1 : 2;
				else if(probOrderIdx > 1)
					detIdx = 3 + (probOrderIdx - 2) * 3 + diffIdx;
				
				knowledgeDetail += (isCorrect) ? CommentMapper.getComment(Type.KNOWLEDGE_COMMENT, detIdx*2) + "\n"
						:CommentMapper.getComment(Type.KNOWLEDGE_COMMENT, detIdx*2+1) + "\n";
			}
		}
		
		
		result.put("main", CommentMapper.getComment(Type.KNOWLEDGE_MAIN, knowledgeScoreIdx) );
		result.put("detail", knowledgeDetail);
		result.put("fixed", CommentMapper.getComment(Type.KNOWLEDGE_FIXED, 0));
		result.put("fixedGuide", CommentMapper.getComment(Type.KNOWLEDGE_FIXED, 1));
		
		return result;
	}
	
	public List<CommentInfoDTO> makeKnowledgeDetailComment(
			DiagnosisReport report)
	{
		int basicScore = report.getKnowledgeCommonScore();
		int typeScore = report.getKnowledgeTypeScore();
		int changeScore = report.getKnowledgeChangeScore();
		int sellScore = report.getKnowledgeSellScore();
		
		List<CommentInfoDTO> result = new ArrayList<>();
		int stretchCommonScore = (int) (basicScore / 22.f * 100);
		int stretchActualScore = (int) ((typeScore + changeScore + sellScore) / 72. * 100);
		
		List<List<String>> commonDetailScore = new ArrayList<>();
		
		commonDetailScore.add(Arrays.asList(
				KnowledgeSubSection.BASIC.toString(), 
				String.valueOf(report.getKnowledgeCommonBasic() * 10)));
		commonDetailScore.add(Arrays.asList(
				KnowledgeSubSection.INVEST_RULE.toString(), 
				String.valueOf(report.getKnowledgeCommonRule() * 10)));
		commonDetailScore.add(Arrays.asList(
				KnowledgeSubSection.PROFIT_GUARANTEED.toString(), 
				String.valueOf(report.getKnowledgeCommonProfit() * 10)));
		
		List<List<String>> actualDetailScore = new ArrayList<>();
		
		actualDetailScore.add(Arrays.asList(
				KnowledgeSection.PRICE_CHANGE.toString(), 
				String.valueOf((int)(changeScore / 24. * 100))));
		actualDetailScore.add(Arrays.asList(
				KnowledgeSection.SELL_WAY.toString(), 
				String.valueOf((int)(sellScore / 24. * 100))));
		actualDetailScore.add(Arrays.asList(
				KnowledgeSection.TYPE_SELECT.toString(), 
				String.valueOf((int)(typeScore / 24. * 100))));
				
		CommentInfoDTO commonCommentInfo = CommentInfoDTO.builder()
				.name("투자 기초")
				.main("")
				.detail("")
				.score(stretchCommonScore)
				.detailScoreList(commonDetailScore)
				.build();
		
		CommentInfoDTO actualCommentInfo = CommentInfoDTO.builder()
				.name("투자 실전")
				.main("")
				.detail("")
				.score(stretchActualScore)
				.detailScoreList(actualDetailScore)
				.build();
		
		result.add(commonCommentInfo);
		result.add(actualCommentInfo);
		
		return result;
	}
	
	
	@Deprecated
	public List<String> makeSimilarTypeInfo(int riskScore, int investScore, int knowledgeScore) {
		List<String> res = new ArrayList<>();
		
		int totalScore = riskScore >= 75 ? 3 : riskScore >= 55 ? 2 : 1;
		totalScore += investScore >= 75 ? 3 : investScore >= 55 ? 2 : 1;
		totalScore += knowledgeScore >= 70 ? 3 : knowledgeScore >= 50 ? 2 : 1;
				
		String investerRatio = totalScore == 9 || totalScore == 3 ? "3.7%"
				: totalScore > 5 ? "33.33%" : "25.93%";
		String avgItemNum = riskScore >= 75 ? "8종목" : riskScore >= 55 ? "5종목" : "3종목";
		String investRatio = riskScore >= 75 ? "55%" : riskScore >= 55 ? "40%" : "10%";
		
		res.add(investerRatio);
		res.add(avgItemNum);
		res.add(investRatio);
		
		return res;
	}
	
}
