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
	
	public Map<String, String> makeRiskMainComment(DiagnosisReport report)
	{
		String[] riskType = {
				"투자위험을 회피하면서 안전수익을 추구하는 투자자",
				"투자위험을 회피하지만 고수익을 추구하는 투자자",
				"투자위험을 감내하지만 안전수익을 추구하는 투자자",
				"투자위험을 감내하면서 고수익을 추구하는 투자자"};
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (report.getRiskLevelScore() < 8) ? 0 : 1;			// 11~13 Question 3 ~ 7 / 8 ~ 12
		int tracingIdx = (report.getRiskStockRatioScore() < 3) ? 0 : 1;		// 2 Question 1,2 / 3,4
		int typeIdx = profileIdx * 2 + tracingIdx;
		
		String riskComment = "";
		
		String[] investRiskComment = {
			"원금손실 등 투자에서 발생하는 위험은 피하는 투자자이며, 자신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있습니다.",
			"원금손실 등 투자에서 발생하는 위험은 피하는 투자자이며, 투자 과정에서 심각한 실수나 손실을 줄이는 것에 중점을 두는 경향이 있습니다.",
			"원금손실 등 투자에서 발생하는 위험은 피하는 투자자이지만, 때로는 수익을 내기 위해서 기꺼이 투자위험을 감수하려는 경향이 있습니다.",
			"투자위험은 적극적으로 수용하거나 감내하는 투자자이며, 자신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있습니다.",
			"투자위험은 적극적으로 수용하거나 감내하는 투자자이며, 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 있습니다.",
			"투자위험은 적극적으로 수용하거나 감내하는 투자자이며, 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 매우 많습니다."
		};
		String[] investMethodComment = {
			"투자는 느리더라도 꾸준한 것이 중요하다고 생각하며, 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자는 느리더라도 꾸준한 것이 중요하다고 생각하며, 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자는 느리더라도 꾸준한 것이 중요하다고 생각하며, 우량주 및 블루칩 등 안정성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자는 느리더라도 꾸준한 것이 중요하다고 생각하며, 성장성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자성과로 높은 수익률을 추구하며, 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자성과로 높은 수익률을 추구하며, 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자성과로 높은 수익률을 추구하며, 우량주 및 블루칩 등 안정성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자성과로 높은 수익률을 추구하며, 성장성이 높은 자산에 분산투자하는 경향이 있습니다."
		};
		String[] combiComment = {
				"투자방법 및 금융상품 선택 시 투자위험에 노출되는 것을 선호하지 않아 낮은 수익률로 자신의 투자방법을 심심한 투자로 여길 수 있습니다.",
				"자신에게 맞는 낮은 수익률의 포트폴리오 구성으로 높은 수익률에 유혹을 느낄 수도 있습니다.",
				"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 주변 사람들에게 안정감 있게 투자하는 것으로 보일 수 있습니다.",
				"스스로 여러 종목을 관리하기 어렵다면 펀드 등과 같은 투자상품을 고려하는 것도 방법일 수 있습니다.",
				"투자위험을 회피하는 성향 때문에 가격변동이 커졌을 때 심리적으로 흔들릴 수 있어 투자종목을 선택할 때는 충분히 분석하고 고민할 필요가 있습니다.",
				"저위험을 선호하는 성향과 고수익을 추구하는 투자방법간의 차이로 투자 후 계속 주식앱을 보면서 불편감을 느낄 수 있습니다.",
				"투자된 자본에 대한 수익을 복리화하는 과정이 시작되었다고 할 수 있습니다.",
				"투자성과는 고수익을 추구하며 매우 공격적으로 투자하는 방법을 선택하고 있을 수 있습니다.",
				"주식투자를 고려할 수 도 있지만, 원금이 보장되거나 국채 등 안정적인 투자를 고려해 보는 것도 좋습니다.",
				"종목에 대한 정확한 분석을 전제로 본인의 상황을 이해하고 투자원칙을 수립하는 것이 중요합니다.",
				"투자수익률이 낮더라도 원금보장이 확실한 금융상품 선택에서 편안함을 느낄 수 있습니다.",
				"투자에 들이는 시간과 노력이 적은 투자자는 자산관리사 등 \"전문가\"의 도움을 받는 것도 방법일 수 있습니다.",
				"자신의 위험회피 성향과 다른 고수익을 추구하는 집중투자를 선택하여 그로 인하여 증가하는 리스크를 감수해야 합니다.",
				"투자손실을 최소화하려는 성향 때문에 가격 변동이 커졌을 때 심리적으로 흔들릴 가능성이 있습니다.",
				"저평가된 주식을 매수하여 제 가치에 매도하기 위해서는 기업의 가치평가 방법을 습득하는 곳에 관심을 가져 볼 필요가 있습니다.",
				"고수익/고위험의 종목투자가 늘어갈 수 있으니 수시로 확인할 필요가 있습니다. ",
				"투자기간은 비교적 단기간으로 설정하고 예상수익률이 높다면 위험이 높은 상품을 집중적으로 선택하기도 합니다.",
				"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 핵심입니다.",
				"싼 주식과 고속성장기업의 조합을 찾아서 투자하려고 하고 이를 위해서는 그에 해당하는 우수기업 리스트를 미리 작성해 볼 필요가 있습니다.",
				"성장하고 있는 또는 성장할 것으로 예상되는 주식에 분산투자하고 있기 때문에 옥석을 가려 투자하는 능력이 핵심입니다.",
				"투자기간은 비교적 단기간으로 설정하고 예상수익률이 높다면 위험이 높은 상품을 집중적으로 선택하기도 합니다.",
				"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 핵심입니다.",
				"기업의 가치 변화를 꼼꼼히 체크해야 합니다. 기업가치의 변화는 매분기 발표되는 실적보고서를 통해서 확인할 수 있습니다.",
				"성장하고 있는 또는 성장할 것으로 예상되는 주식에 분산투자하고 있기 때문에 옥석을 가려 투자하는 능력이 핵심입니다.",
				"주식시장에서 저평가된 가격에 사서 가치가 반영되는 동안 오래 기다릴 수 있어 대출 등 신용거래를 해서는 안 됩니다.",
				"적은 수의 종목 수익률을 관리하기 때문에 수익폭이 매우 클 수 있지만 한 종목에서 발생할 수 있는 리스크가 크다는 것을 인지해야 합니다. ",
				"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 큰 수익이 나지 않는다고 생각할 수 있습니다.",
				"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 큰 수익이 나지 않는다고 생각할 수 있어 펀드, ETF 등 간접투자도 고려해 볼만 합니다.",
				"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높습니다.",
				"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
				"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 변동성이 큰 시장 상황에서도 잘 견딜 수 있으며 투자성과를 내기 위해서는 장기투자가 필요할 수 있습니다.",
				"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 투자하려는 종목은 늘 고평가 논란에 시달릴 수 있으니 옥석을 잘 가릴 필요가 있습니다.",
				"시장점유율이 높고 위험변수가 적은 기업에 투자해야 수익률을 높일 수 있어 시장대표주, 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
				"미래 기대되는 영업이익을 예상하고 그 가치를 평가하여야 하므로 투자리스크가 큰 편이어서 어떤 새로운 기술이 적용되는지, 미래사회는 어떻게 바뀔지 등에 관심을 가져야 한다.",
				"주가는 변동성이 크지 않으며, 배당도 많이 주는 안정적인 주식에 투자하여 투자성과에 조바심이 날 수 있습니다.",
				"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고래해 보는 것도 좋습니다.",
				"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높습니다.",
				"하이리스크-하이리턴을 선호하고 주식시장에 큰 영향을 주는 상황에 집중하니 시장의 흐름을 민감하게 파악하고 분석하는 자세가 필요합니다.",
				"하이리스크-하이리턴을 선호하니, 기업의 가치와 낮은 주가의 차이인 안전마진을 확보하는 노력이 필요합니다.",
				"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
				"시장점유율이 높고 위험변수가 적은 기업에 투자해야 수익률을 높일 수 있어 시장대표주, 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
				"미래 기대되는 영업이익을 예상하고 그 가치를 평가하여야 하므로 투자리스크가 큰 편이어서 어떤 새로운 기술이 적용되는지, 미래사회는 어떻게 바뀔지 등에 관심을 가져야 한다.",
				"저PER주 투자 등 주가는 변동성이 크지 않으며, 배당도 많이 주는 안정적인 주식에 투자하여 투자성과에 조바심이 날 수 있습니다.",
				"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고래해 보는 것도 좋습니다.",
				"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높으니 기업가치가 훼손됨을 확인하게 되면 과감한 리밸런싱도 필요할 수 있습니다.",
				"하이리스크-하이리턴을 선호하고 주식시장에 큰 영향을 주는 상황에 집중하니 시장의 흐름을 민감하게 파악하고 분석하는 자세가 필요합니다.",
				"하이리스크-하이리턴을 선호하니, 기업의 가치와 낮은 주가의 차이인 안전마진을 확보하는 노력이 필요합니다.",
				"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다."
		};
		
		///
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
		
		riskComment += investRiskComment[investRiskComIdx] + " ";

		
		///
		
		int stockNumIdx = (report.getRiskStockNumScore() < 3) ? 0 : 1;		// number 3 question
		int preferNumIdx = (report.getRiskStockPreferScore() < 3) ? 0 : 1;	// number 4 question
		int investMethodComIdx = tracingIdx * 4 + stockNumIdx * 2 + preferNumIdx;
		
		riskComment += investMethodComment[investMethodComIdx] + " ";
		
		///
		
		riskComment += combiComment[investRiskComIdx * 8 + investMethodComIdx] + " ";
		
		///
		
		result.put("main", riskType[typeIdx]);
		result.put("detail", riskComment);
		
		return result;
	}
	
	public List<Object> makeRiskDetailComment(DiagnosisReport report)
	{
		int profLevelScore = (int)((report.getRiskLevelScore()) / 12. * 100 );
		int profCapaScore = (int)((report.getRiskCapaScore()) / 8. * 100);
		
		int stretchProfileScore = (int)((report.getRiskProfileScore() - 5) / 15.f * 100);
		int stretchTracingScore = (int)((report.getRiskTracingScore() - 4) / 12.f * 100);
		
		String[] profileMainList = {"투자위험 회피형 투자자", 
				"투자위험 감내형 투자자"};
		String[] tracingMainList = {
				"안전수익 추구형 투자자", 
				"고수익 추구형 투자자"};
		
		
		
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
		profileCommentInfo.put("main", (report.getRiskLevelScore() < 8)
				?profileMainList[0]
				:profileMainList[1]);
		profileCommentInfo.put("detail", "");
		profileCommentInfo.put("score", stretchProfileScore);
		profileCommentInfo.put("detailScoreList", profileDetailScore);
		
		tracingCommentInfo.put("name", "투자 방법");
		tracingCommentInfo.put("main", (report.getRiskStockRatioScore() < 3)
				? tracingMainList[0]
				: tracingMainList[1]);
		tracingCommentInfo.put("detail", "");
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
				"기초와 실전 지식을 갖춘 준비된 투자자", 
				"투자지식의 기초는 알고있는 투자자", 
				"투자기초부터 탄탄한 학습이 필요한 투자자"};
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
		
		Map<String, Object> commonCommentInfo = new HashMap<>();
		Map<String, Object> actualCommentInfo = new HashMap<>();
		int stretchCommonScore = (int) (basicScore / 22.f * 100);
		int stretchActualScore = (int) ((typeScore + changeScore + sellScore) / 72. * 100);
		int[] rankMinValue = {80, 60, 0};
		
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
		commonCommentInfo.put("detail", "");
		commonCommentInfo.put("score", stretchCommonScore);
		commonCommentInfo.put("detailScoreList", commonDetailScore);
		
		actualCommentInfo.put("name", "투자 실전");
		//actualCommentInfo.put("main", "");
		actualCommentInfo.put("detail", "");
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
