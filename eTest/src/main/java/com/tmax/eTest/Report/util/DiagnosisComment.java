package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Report.util.DiagnosisUtil.InvestProfile;
import com.tmax.eTest.Report.util.DiagnosisUtil.InvestTracing;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSubSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.RiskProfile;
import com.tmax.eTest.Report.util.DiagnosisUtil.RiskTracing;

@Component
public class DiagnosisComment {
	
	public Map<String, String> makeRiskMainComment(int profileScore, int tracingScore)
	{
		String[] riskType = {
				"주력종목 위주의 공격형 투자자",
				"주력종목 위주의 공격형 투자자",
				"여러종목에 분산 투자하는 공격형 투자자",
				"주력종목 위주의 안정추구 투자자",
				"하이브리드형 투자자",
				"여러종목에 분산 투자하는 공격형 투자자",
				"주력종목 위주의 안정추구 투자자",
				"분산투자하는 안정형 투자자",
				"분산투자하는 안정형 투자자"};
		String[] riskMainComment = {
				"\"주력종목 위주의 공격형 투자자\"는 위험을 감수하면서 특정 종목에 집중투자하는 경향을 보입니다. 집중투자는 높은 수익률을 기대할 수 있지만 그만큼 리스크도 감수해야 합니다. 투자기간을 단기적으로 설정하는 경향이 있는데, 시장의 흐름과 경기변화를 민감하게 파악하고 분석하는 자세가 필요합니다.",
				"\"주력종목 위주의 공격형 투자자\"는 위험을 감수하면서 특정 종목에 집중투자하는 경향을 보입니다. 집중투자는 높은 수익률을 기대할 수 있지만 그만큼 리스크도 감수해야 합니다. 투자기간을 단기적으로 설정하는 경향이 있는데, 시장의 흐름과 경기변화를 민감하게 파악하고 분석하는 자세가 필요합니다.",
				"\"여러종목에 분산 투자하는 공격형 투자자\"는 위험을 감수하면서 높은 수익률을 추구하고 여러 종목에 분산하여 투자하는 경향을 보입니다. 저평가된 주식을 골라 고평가되기 전에 파는 가치투자를 선호하기도 합니다. 리스크를 감수하는 성향으로 고위험 종목 투자가 늘어갈 수 있기 때문에, 늘 수익률을 점검하고 조정하는 태도를 갖는게 필요합니다.",
				
				"\"주력종목 위주의 안정추구 투자자\"는 손실위험을 최소화하는 안정적인 투자를 선호하며, 확실하다고 판단하는특정 종족 중심으로 투자하는 경향이 있습니다. 기업안정성이 높은 우량주 위주의 투자를 선호하기도 합니다. 손실을 최소화하려는 성향 때문에 가격 변동이 커졌을때 흔들릴 가능성이 있습니다. 투자한 종목의 시장 현황과 기업 상황을 지속적으로 분석하여 변수를 미리 준비한다면 더욱 안정적인 투자에 도움이 될 수 있습니다.",
				"\"하이브리드형 투자자\"는 공격적일때는 공격적으로, 보수적일때는 보수적으로 상황에 맞게 투자방법을 결정하는 경향이 있습니다. 대체로 내가 감당할 수 있는 범위 내에서 투자하는 것을 선호하며, 나의 위험성향과 실제 투자 방식이 상당부분 일치하기 때문에 주변 사람들이 봤을때는 안정적으로 투자하는 사람으로 보이기도 합니다.",
				"\"여러종목에 분산 투자하는 공격형 투자자\"는 위험을 감수하면서 높은 수익률을 추구하고 여러 종목에 분산하여 투자하는 경향을 보입니다. 저평가된 주식을 골라 고평가되기 전에 파는 가치투자를 선호하기도 합니다. 리스크를 감수하는 성향으로 고위험 종목 투자가 늘어갈 수 있기 때문에, 늘 수익률을 점검하고 조정하는 태도를 갖는게 필요합니다.",
				
				"\"주력종목 위주의 안정추구 투자자\"는 손실위험을 최소화하는 안정적인 투자를 선호하며, 확실하다고 판단하는특정 종족 중심으로 투자하는 경향이 있습니다. 기업안정성이 높은 우량주 위주의 투자를 선호하기도 합니다. 손실을 최소화하려는 성향 때문에 가격 변동이 커졌을때 흔들릴 가능성이 있습니다. 투자한 종목의 시장 현황과 기업 상황을 지속적으로 분석하여 변수를 미리 준비한다면 더욱 안정적인 투자에 도움이 될 수 있습니다.",
				"\"분산투자하는 안정형 투자자\"는 리스크를 짊어지기 보다 손실을 최소화하여 안정적 수익을 추구하며, 분산투자를 선호하는 경향이 있습니다. 기대수익률이 낮더라도 미래 원금보전이 확실한 종목과 상품에 안정감을 느낍니다. 분산투자를 하는 이유도 위험을 줄이려는 목적이 크며, 기업안정성이 높은 우량주 위주로 투자하는 성향을 보입니다.",
				"\"분산투자하는 안정형 투자자\"는 리스크를 짊어지기 보다 손실을 최소화하여 안정적 수익을 추구하며, 분산투자를 선호하는 경향이 있습니다. 기대수익률이 낮더라도 미래 원금보전이 확실한 종목과 상품에 안정감을 느낍니다. 분산투자를 하는 이유도 위험을 줄이려는 목적이 크며, 기업안정성이 높은 우량주 위주로 투자하는 성향을 보입니다."};
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (profileScore >= 15) ? 0 	// 리스크 선호형
				: (profileScore >= 11) ? 1			// 리스크 중립형
				: 2;								// 리스크 회피형
		
		int tracingIdx = (tracingScore >= 12) ? 0	// 집중 투자형
				: (tracingScore >= 9) ? 1			// 중간 투자형
				: 2;								// 분산 투자형
		
		int finalIdx = profileIdx * 3 + tracingIdx;
		
		result.put("main", riskType[finalIdx]);
		result.put("detail", riskMainComment[finalIdx]);
		
		return result;
	}
	
	public List<Object> makeRiskDetailComment(DiagnosisReport report)
	{
		int profileScore = report.getRiskProfileScore();				
		int tracingScore = report.getRiskTracingScore();
		
		int profLevelScore = (int)((report.getRiskLevelScore() - 3) / 9. * 100 );
		int profCapaScore = (int)((report.getRiskCapaScore() - 2) / 6. * 100);
		
		String[] profileMainList = {"높은 수익률을 추구하는 공격투자형", 
				"너무 큰 위험도 너무 작은 위험도 추구하지 않는, 중립투자형", 
				"손실을 최소화 하며 안정적 수익을 추구하는 안정투자형"};
		String[] tracingMainList = {
				"주력 종목 위주로 집중형 투자자", 
				"집중할때는 집중, 분산할때는 분산! 중간형 투자자", 
				"계란은 여러 바구니에, 분산형 투자자"};
		
		String[] profileDetailList = {
				"당신은 손실 위험을 감수하면서 높은 수익률을 추구하는 공격투자형 입니다. 예상 수익이 높다고 판단하면, 위험이 있는 상품에 투자를 하거나 주식의 특정 종족에 집중하여 투자를 하는 경향도 있습니다. 공격투자형은 한 두번 투자에 성공하면 자기확신 편향에 빠지기 쉽고 위험에 둔감해 질수 있기 때문에, 항상 본인의 투자상황을 점검해야 합니다. 하루하루 수익률에 일희일비하지 않고 확고한 투자원칙을 기반으로 투자를 한다면 원하는 목표에 가까워질 수 있습니다.",
				"당신은 리스크 중립형으로 위험을 적당하게 수용하고 인내하는 투자자입니다. 투자에는 그에 상응하는 투자위험이 있음을 인식하며 예적금보다는 높은 수익률을 추구합니다. 리스크 중립형 투자자는 지금 하고 있는 투자방법이 자신에게 익숙한지 확인해볼 필요가 있습니다. 투자 후 계속 주식앱을 보며 불안에 떨고 있다면 안정투자형에 가깝고, 만족스럽지 못한 수익에 밤잠을 설치면 공격투자형에 가깝습니다. 어느때는 안정투자형, 어떤 때는 공격적투자형에 가까울 수 있기 때문에, 투자방법을 수시로 조정할 수 있도록 준비도 필요합니다.",
				"당신은 손실을 최소화하고 안정적인 수익률을 추구하는 안정형 투자자 입니다. 안정형 투자자는 주식투자를 고려할 수도 있지만, 원금은 보장하거나 원금을 부분 보장하는 국고채, 통안채, RP 등 안정적인 투자상품을 고려해보는 것도 좋습니다. 경험부족으로 인한 불안으로 안정적 투자를 고려한다면 투자이론 공부와 경험을 쌓아가면서 본인에게 맞는 투자방법을 찾는 것도 좋은 방법이 될 수 있습니다."};
		String[] tracingDetailList = {
				"당신은 적은 종목의 수를 보유하면서 높은 수익률을 추구하는 집중 투자를 하고 있을 확률이 큽니다. 집중투자는 적은 종목의 수익률에 의존하기 때문에 수익폭이 매우 큰 특징이 있습니다. 집중투자는 관리해야하는 종목 수가 상대적으로 적지만 한 종목 한 종목에서 발생할 수 있는 리스크가 크다는 것을 인지하고 투자 종목을 선택할 때 충분히 분석하고 고민해야 합니다.",
				"당신은 집중할때는 집중하고 분산할때는 분산하는 하이브리드형 투자자에 가깝습니다. 분산 투자와 집중 투자 중 무엇이 더 좋은 투자인지에 대한 정답이 있는 것은 아닙니다. 하지만 공통적으로는 종목에 대한 정확한 분석과 시장 상황에 대한 높은 이해를 전제로 본인의 상황을 이해하고 투자원칙을 수립하는 것이 중요합니다. 분산 투자와 집중 투자 중 본인에게 더욱 맞는 방법을 찾아 투자를 이어간다면 원하는 목표에 더욱 가까워질 수 있습니다.", 
				"당신은 특정 종목 몇 개에 집중하기 보다는 최대한 분산하여 투자하면서 위험을 줄이는 투자성향을 가지고 있습니다. 경기민감주와 경기와 무관한 업종을 섞어서 담는 등 업종을 분산하거나 국내 뿐만 아니라 해외주식에도 투자하면서 지역을 분산하기도 합니다. 단순히 비슷한 여러 종목에 투자하는 것이 아니라 업종이나 지역이 다양한 종목을 분산하여 투자함으로서 개별종목 하나하나의 위험을 줄이고 원하는 수익률을 실현하기 위한 노력이 필요합니다. 스스로 여러 종목을 분석하고 관리하기 어렵다면 펀드 등 투자상품을 고려하는 것도 방법일 수 있습니다."};
		int profileIdx = (profileScore >= 15) ? 0 	// 리스크 선호형
				: (profileScore >= 11) ? 1			// 리스크 중립형
				: 2;								// 리스크 회피형
		
		int tracingIdx = (tracingScore >= 12) ? 0	// 집중 투자형
				: (tracingScore >= 9) ? 1			// 중간 투자형
				: 2;								// 분산 투자형
		
		int stretchProfileScore = (int)((profileScore - 5) / 15.f * 100);
		int stretchTracingScore = (int)((tracingScore - 4) / 12.f * 100);
		
		List<Object> result = new ArrayList<>();
		
		Map<String, Object> profileCommentInfo = new HashMap<>();
		Map<String, Object> tracingCommentInfo = new HashMap<>();
		
		List<List<String>> profileDetailScore = new ArrayList<>();
		profileDetailScore.add(Arrays.asList(RiskProfile.LEVEL.toString(), String.valueOf(profLevelScore)));
		profileDetailScore.add(Arrays.asList(RiskProfile.CAPACITY.toString(), String.valueOf(profCapaScore)));

		List<List<String>> tracingDetailScore = new ArrayList<>();
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_PERIOD.toString(), 
				String.valueOf(report.getRiskInvestPeriodScore()*25)));
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_RATIO.toString(), 
				String.valueOf(report.getRiskStockRatioScore()*25)));
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_NUM.toString(), 
				String.valueOf(report.getRiskStockNumScore()*25)));
		tracingDetailScore.add(Arrays.asList(
				RiskTracing.STOCK_PREFER.toString(), 
				String.valueOf(report.getRiskStockPreferScore()*25)));
		
		profileCommentInfo.put("name", "투자위험 태도");
		profileCommentInfo.put("main", profileMainList[profileIdx]);
		profileCommentInfo.put("detail", profileDetailList[profileIdx]);
		profileCommentInfo.put("score", stretchProfileScore);
		profileCommentInfo.put("detailScoreList", profileDetailScore);
		
		tracingCommentInfo.put("name", "투자 방법");
		tracingCommentInfo.put("main", tracingMainList[tracingIdx]);
		tracingCommentInfo.put("detail", tracingDetailList[tracingIdx]);
		tracingCommentInfo.put("score", stretchTracingScore);
		tracingCommentInfo.put("detailScoreList", tracingDetailScore);
		
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}

	

	public Map<String, String> makeInvestMainComment(int profileScore, int tracingScore)
	{
		String[] investType = {
				"행동편향에 빠질 가능성이 낮은 자신만의 투자원칙이 명확한 투자자",
				"행동편향에 빠질 가능성은 낮지만 자신만의 투자원칙 정립이 필요한 투자자",
				"행동편향에 빠질 가능성은 있지만 자신만의 투자원칙이 명확한 투자자",
				"행동편향에 빠질 가능성은 있고 자신만의 투자원칙 정립이 필요한 투자자"
				};
		
		String[] investMainComment = {
				"명확한 투자원칙을 갖추고 있는 편입니다. 나만의 투자원칙을 만들고 일관되게 지키는 것은 분명 힘든 과정이지만, "
				+ "실패를 줄이고 성공투자로 나아가는 최선의 방법이 됩니다. 당신이 가지고 있는 몇몇 투자원칙은 평생투자라는 긴 항해에서 "
				+ "흔들리지 않는 나침반 역할을 해 줄 것입니다.",
				
				"투자에서 행동편향의 영향은 상대적으로 적지만, 성공 투자자가 되기 위해서는 객관적인 분석에 근거한 투자 결정을 "
				+ "지향하고 학습과 투자경험을 쌓아가면서 나만의 투자원칙을 하나씩 세워 나가야 합니다. 자신의 생애주기에 맞는 "
				+ "투자목표를 정하고 위험 감내수준에 맞는 장기투자를 하고 자산배분으로 위험을 통제하고 주기적으로 재평가하는 규칙적인 "
				+ "투자를 하고 있는지 되돌아봐야 합니다. 과도한 탐욕을 부리지 않고, 행동편향을 경계하고, 리딩방에 현혹되지 않아야 "
				+ "하고 투자지식을 계속 쌓고 실전경험를 반복하며 기업의 성장과 함께 과실을 누리는 투자의 본질을 잊지 않으려고 노력해야 합니다.",
				
				"심리적 오류와 편향으로 불합리한 투자결정을 내릴 가능성은 있지만, 대체로 자신만의 투자 원칙을 지켜나려고 하는 경향이 "
				+ "있습니다.  자신의 능력 또는 판단이 스스로에게 유리한 결과를 가져다줄 것이라고 지나치게 과신하여 투자위험 "
				+ "가능성을 낮게 평가할 수도 있습니다. 또 보유한 주식을 과대평가하여 변화로 인한 이익은 과소평가하고 손실은 과대평가하는 "
				+ "등 현상유지 편향에 빠지거나 기존의 판단에 맞는 정보만 찾고 그 정보를 더 중요하게 여겨 반대되는 정보의 중요도는 무시할 "
				+ "수 도 있습니다."
				+ "투자에 있어서 불확실하고 혼란스러운 상황에 직면하게 될 때,  올바른 투자결정을 내린다는 것은 생각만큼 쉬운 일이 아닙니다. "
				+ "심리적인 실수를 극복하기 위해서는 객관적인 투자방법을 더욱 발전시켜 나가야 합니다.",
				
				"혹시 하루에도 몇 번씩 주식 앱을 켜고 시세 변화에 마음이 흔들리시나요? 상황을 객관적으로 판단하지 않고, 자신 판단의 "
				+ "정당성을 받쳐 줄 정보만을 찾고 있으시나요? 투자경험이 많다고 행동편향 징후를 무시하고 있으신가요? 자신의 판단이나 "
				+ "투자결정이 잘못임을 알면서도 계속 고집하고 있으신가요?"
				+ "자신에게 긍정적인 정보만 찾는 확증편향, 과거 정보를 의사결정의 근거로 삼는 기준점편향, 통제가능성에 대한 과신 등 "
				+ "다양한 편향들을 경계할 필요가 있습니다. 심리적으로는 같은 크기의 이득보다는 손실로 인한 고통이 더 크게 느끼면서 "
				+ "손실회의 편향에 빠질 수도 있습니다. 이를 극복하기 위해선 자신만의 투자원칙이 세워져 있어야 합니다. 그렇지 않다면 "
				+ "투자하는 동안 심리적 안정을 얻기 힘듭니다."
				+ "자신의 생애주기에 맞는 투자목표를 정하고 위험 감내수준에 맞는 장기투자를 하고 자산배분으로 위험을 통제하고 주기적으로 "
				+ "재평가하는 규칙적인 투자를 하고 있는지 되돌아봐야 합니다. 과도한 탐욕을 부리지 않고, 행동편향을 경계하고, "
				+ "리딩방에 현혹되지 않아야 하고 투자지식을 계속 쌓고 실전경험를 반복하며 기업의 성장과 함께 과실을 누리는 투자의 "
				+ "본질을 잊지 않으려고 노력해야 합니다."};
		
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (profileScore >= 40) ? 0
				: 1;
		
		int tracingIdx = (tracingScore >= 45) ? 0
				: 1;
		
		int typeIdx = profileIdx * 2 + tracingIdx;
		
		result.put("main", investType[typeIdx]);
		result.put("detail", investMainComment[typeIdx]);
		
		return result;
	}
	
	public List<Object> makeInvestDetailComment(DiagnosisReport report)
	{

		int profileScore = report.getInvestProfileScore(); 
		int tracingScore = report.getInvestTracingScore();
		
		String[] profileMainList = {
				"행동편향에 빠질 가능성이 낮은 투자자", 
				"행동편향에 빠질 가능성이 있는 투자자"};
		String[] tracingMainList = {
				"투자원칙이 명확한 투자자",
				"투자원칙 정립이 필요한 투자자"};
		
		int profileIdx = (profileScore >= 40) ? 0
				: 1;
		
		int tracingIdx = (tracingScore >= 45) ? 0
				: 1;
		
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
		
		List<Object> result = new ArrayList<>();
		
		Map<String, Object> profileCommentInfo = new HashMap<>();
		Map<String, Object> tracingCommentInfo = new HashMap<>();
		
		profileCommentInfo.put("name", "행동편향");
		profileCommentInfo.put("main", profileMainList[profileIdx]);
		profileCommentInfo.put("detail", "");
		profileCommentInfo.put("score", stretchProfileScore);
		profileCommentInfo.put("detailScoreList", profileDetailScore);
		
		tracingCommentInfo.put("name", "투자원칙");
		tracingCommentInfo.put("main", tracingMainList[tracingIdx]);
		tracingCommentInfo.put("detail", "");
		tracingCommentInfo.put("score", stretchTracingScore);
		tracingCommentInfo.put("detailScoreList", tracingDetailScore);
		
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}
	
	public Map<String, String> makeKnowledgeMainComment(int knowledgeScore)
	{
		Map<String, String> result = new HashMap<>();
		int[] rankMinValue = {80, 60, 0};
		int knowledgeScoreIdx = rankMinValue.length - 1;
		
		String[] knowledgeMain = {
				"우수", 
				"보통", 
				"부족"};
		String[] knowledgeDetail = {
				"당신은 매매방법이나 종목을 분석하는 방법 등 투자지식의 기본적인 사항을 알고 있고 자신만의 투자방법도 갖추고 있습니다. 많은 노력이 필요하겠지만 더 많은 경험과 공부를 이어간다면 나만의 원칙을 가진 성공적인 투자자가 될 수 있습니다.",
				"당신은 매매방법이나 종목을 분석하는 방법 등 투자지식의 기본적인 사항들은 알고 있는 것으로 보입니다. 많은 노력이 필요하겠지만 더 많은 경험과 공부를 이어간다면 나만의 원칙을 가진 성공적인 투자자로 성장할 가능성이 높습니다.",
				"당신은 아직 투자지식에 익숙하지 않을 가능성이 높습니다. 시장과 산업동향, 주식 투자의 기본 등 꼼꼼하고 꾸준하게 공부하는 투자자가 결국에는 오래 투자할 수 있습니다. 투자와 친해지기 위한 첫걸음을 떼세요. 한 걸음 한 걸음 내딛는다면 소중한 자산이 될 겁니다."};
		
		for(int i = 0; i < rankMinValue.length; i++)
			if(knowledgeScore >= rankMinValue[i])
			{
				knowledgeScoreIdx = i;
				break;
			}
		
		result.put("main", knowledgeMain[knowledgeScoreIdx]);
		result.put("detail", knowledgeDetail[knowledgeScoreIdx]);
		
		return result;
	}
	
	public List<Object> makeKnowledgeDetailComment(
			DiagnosisReport report)
	{
		int basicScore = report.getKnowledgeCommonScore();
		int typeScore = report.getKnowledgeTypeScore();
		int changeScore = report.getKnowledgeChangeScore();
		int sellScore = report.getKnowledgeSellScore();
		
		List<Object> result = new ArrayList<>();
		
		String[] basicCommentList = {
				"당신은 투자기초 영역에 대한 지식을 갖추고 있습니다. 투자 시작을 위한 기본적인 준비는 되어 있는 것으로 보입니다.", 
				"당신은 투자에 관심을 가지고 기본적인 지식은 알고 있는 것으로 보입니다. 수익과 위험의 개념, 장기투자와 단기투자의 차이점 등 어렴풋이 알고 있는 개념들을 명확히 학습할 필요가 있습니다.",
				"배당금이 뭐지? 주식이랑 채권이랑 어떻게 다르고 또 펀드랑은 뭐가 다른거지? 투자지식에 아직은 생소한게 많아 보입니다. 기초적인 것 부터 하나씩 공부하다 보면 하루가 다르게 실력이 상승할 수 있습니다."};
		String[] actualCommentList = {
				"딩신은 투자실전에 대한 기본적인 지식을 갖추고 있습니다. 꾸준한 공부와 쌓여가는 경험을 통해 똑똑하고 안전한 투자를 이어가세요.", 
				"딩신은 투자실전에 대한 기본적인 지식을 갖추고 있습니다. 재무재표를 분석하는 방법, 시장을 분석하는 방법 등 다양한 주제의 학습을 이어가면서 본인만의 무기를 늘려가세요.", 
				"투자를 위해선 종목을 어떻게 골라야 하는지, 가격이 왜 변동하는지 등 실전적인 지식들이 필요합니다. 결코 늦지 않았습니다. 꾸준한 학습을 통해 투자 실력의 토대를 쌓으세요."};
		Map<String, Object> commonCommentInfo = new HashMap<>();
		Map<String, Object> actualCommentInfo = new HashMap<>();
		int stretchCommonScore = (int) (basicScore / 22.f * 100);
		int stretchActualScore = (int) ((typeScore + changeScore + sellScore) / 72. * 100);
		int[] rankMinValue = {80, 60, 0};
		
		int commonIdx = rankMinValue.length -1;
		int actualIdx = rankMinValue.length -1;
		
		for(int i = 0; i < rankMinValue.length; i++)
			if(stretchCommonScore >= rankMinValue[i])
			{
				commonIdx = i;
				break;
			}
		for(int i = 0; i < rankMinValue.length; i++)
			if(stretchActualScore >= rankMinValue[i])
			{
				actualIdx = i;
				break;
			}
		
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
				
		
		commonCommentInfo.put("name", "투자 기초");
		//commonCommentInfo.put("main", "");
		commonCommentInfo.put("detail", basicCommentList[commonIdx]);
		commonCommentInfo.put("score", stretchCommonScore);
		commonCommentInfo.put("detailScoreList", commonDetailScore);
		
		actualCommentInfo.put("name", "투자 실전");
		//actualCommentInfo.put("main", "");
		actualCommentInfo.put("detail", actualCommentList[actualIdx]);
		actualCommentInfo.put("score", stretchActualScore);
		actualCommentInfo.put("detailScoreList", actualDetailScore);
		
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
