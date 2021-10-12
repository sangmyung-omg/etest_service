package com.tmax.eTest.Report.util;

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
import com.tmax.eTest.Report.util.DiagnosisUtil.InvestProfile;
import com.tmax.eTest.Report.util.DiagnosisUtil.InvestTracing;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSubSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.RiskProfile;
import com.tmax.eTest.Report.util.DiagnosisUtil.RiskTracing;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DiagnosisComment {
	
	public Map<String, String> makeRiskMainComment(DiagnosisReport report)
	{
		String[] riskType = {
				"티끌모아 태산을 만들꺼야, 예금형 투자자",
				"안전과 수익 두 마리 토끼를 잡을 거야, 유토피아형 투자자",
				"시장평균수익이면 만족이야, 주가지수형 투자자",
				"수익이 우선이야, 호랑이 굴로 들어가는 사냥꾼형 투자자"};
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (report.getRiskLevelScore() < 8) ? 0 : 1;			// 11~13 Question 3 ~ 7 / 8 ~ 12
		int tracingIdx = (report.getRiskStockRatioScore() < 3) ? 0 : 1;		// 2 Question 1,2 / 3,4
		int typeIdx = profileIdx * 2 + tracingIdx;
		
		String riskComment = "";
		
		String[] investRiskComment = {	//211012 ver
			"당신은 원금손실 등 투자 시 발생하는 위험을 피하는 투자자입니다. 자신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있기도 합니다.",
			"당신은 원금손실 등 투자 시 발생하는 위험을 피하는 투자자입니다. 투자 과정에서 심각한 실수나 손실을 줄이는 것에 중점을 두는 경향이 있습니다.",
			"당신은 원금손실 등 투자 시 발생하는 위험을 피하는 투자자이지만, 때로는 수익을 내기 위해서 기꺼이 투자위험을 감수하려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 당신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 매우 강합니다."
		};
		String[] investMethodComment = {	// 211006 ver
			"투자방법으로는 느리더라도 꾸준한게 중요하다고 생각합니다. 우량주 및 블루칩 등 안전성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법으로는 느리더라도 꾸준한게 중요하다고 생각합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법으로는 느리더라도 꾸준한게 중요하다고 생각합니다. 우량주 및 블루칩 등 안전성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자방법으로는 느리더라도 꾸준한게 중요하다고 생각합니다. 성장성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자방법으로는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 우량주 및 블루칩 등 안전성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법으로는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법으로는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 우량주 및 블루칩 등 안전성이 높은 자산에 분산투자하는 경향이 있습니다.",
			"투자방법으로는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 성장성이 높은 자산에 분산투자하는 경향이 있습니다."

		};
		String[] combiComment = {			// 211012 ver
			"손실위험을 최소화하는 안정적인 투자를 선호하여, 금융상품이나 종목을 선택할때도 위험이 최소화된 방향으로 투자하려 할 수 있습니다.",
			"안정적이고 낮은 기대수익률의 포트폴리오를 구성했다면, 높은 수익률에 유혹을 느낄 수도 있습니다.",
			"위험성향과 실제 투자 방식이 상당부분 일치하여 주변 사람들에게 일관되게 투자하는 사람으로 여겨질 수 있습니다.",
			"여러 종목 투자를 선호하지만, 스스로 여러 종목을 관리하기 어렵다면 펀드 등과 같은 투자상품을 고려하는 것도 방법일 수 있습니다.", 
			"투자위험을 회피하려는 성향 때문에 투자자산의 가격 변동성이 커졌을때 심적으로 흔들릴 가능성이 있습니다.",
			"저위험을 선호하는 성향과 고수익을 추구하는 투자방법간의 차이로 투자 후 계속 주식앱을 보면서 밤잠을 설치는 등 불편함을 느낄 수 있습니다.",
			"높은 기대수익률을 추구하면서도 투자위험을 관리하려는 성향을 보이기 때문에, 투자경력이 쌓였다면 투자한 자본 수익이 누적되어 복리화되는 과정이 시작되었을 수 있습니다.",
			"투자성과로 고수익을 추구하여, 매우 공격적으로 투자하는 방법을 선택하고 있을 수 있습니다.",
			"주식투자를 고려할 수 도 있지만, 원금이 보장되거나 국채 등 안전성이 높은 투자에서 심리적 안정감을 느낄 수 있습니다.",
			"종목에 대한 충분하면서도 면밀한 투자분석과 나의 성향에 맞는 투자원칙을 수립하는 것이 중요합니다.",
			"투자수익률이 낮더라도 원금보장이 확실한 금융상품을 투자할때 편안함을 느낄 수 있습니다.",
			"투자에 들일 수 있는 시간이 부족하다면 자산관리사, 로보어드바이저 등 \"전문가\"의 도움을 받는 것도 방법일 수 있습니다.",
			"자신의 위험회피 성향과 상반되는 고수익을 추구하는 집중투자를 선택함에 따라 증가하는 투자위험을 감수할 수 있어야 합니다.",
			"투자손실을 최소화하려는 성향 때문에 투자자산의 가격 변동성이 커졌을 때 심적으로 흔들릴 가능성이 있습니다.",
			"저평가된 주식을 매수하여 제 가치에 매도하기 위해서는 기업의 가치평가 방법에 관심을 가질 필요가 있습니다.",
			"고위험/고수익 종목의 투자비중이 높아 질 수 있기 때문에, 자신의 투자현황을 지속적으로 점검할 필요가 있습니다.", 
			"투자기간을 비교적 단기간으로 설정하고 기대수익률을 높게 잡는다면, 투자위험이 높은 상품을 집중적으로 선택하려 할 수 있습니다.",
			"성장할 것으로 기대되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"저평가된 기업에 투자하는 것을 선호함으로, 그에 해당하는 우량기업 리스트를 미리 작성해 볼 필요가 있습니다.",
			"성장할 것으로 기대되는 주식에 분산투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"투자기간을 비교적 단기간으로 설정하고 기대수익률을 높게 잡는다면, 투자위험이 높은 상품을 집중적으로 선택하려 할 수 있습니다.",
			"성장하고 있는 또는 성장할 것으로 기대되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"선택한 종목의 기업가치 변화를 꼼꼼히 체크해야 합니다. 기업가치의 변화는 매분기 발표되는 실적보고서를 통해서 확인할 수 있습니다.",
			"성장할 것으로 기대되는 주식에 분산투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"주식시장에서 저평가된 가격에 매수하고 제 가치가 반영되는 동안 오래 기다릴 수 있어 대출 등 신용거래를 해서는 안 됩니다.",
			"적은 수의 종목을 관리하기 때문에 수익폭이 매우 클 수도 있으지만, 한 종목에서 발생할 수 있는 투자위험도 크다는 것을 인지해야 합니다.", 
			"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 투자수익이 나지 않는다고 생각할 수 있습니다.",
			"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 투자수익률에 불만이 있는 경우는 펀드, ETF 등 간접투자도 고려해 볼만 합니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 투자수익을 가져올 수도 있지만 반대로 투자손실을 볼 확률도 높습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 투자수익을 가져올 수도 있지만 반대로 투자손실을 볼 확률도 높아 투자대상의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 가격 변동성이 큰 시장 상황에서도 잘 견딜 수 있지만 투자성과를 내기 위해서는 장기투자가 필요할 수 있습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 있지만, 투자하려는 종목은 늘 고평가 논란에 빠져있을 수 있으니 옥석을 잘 가릴 필요가 있습니다.",
			"시장점유율이 높고 위험변수가 적은 기업에 투자하는 것을 선호하는 경향이 있기 때문에 시장대표주,  업종 내 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
			"투자대상의 미래가치를 주된 판단기준으로 삼기 때문에 불확실성이 큰 편입니다. 따라서, 떠오르는 새로운 기술은 무엇인지, 미래의 주요 비즈니스 모델은 무엇인지 등에 관심을 가질 필요가 있습니다.",
			"주가 변동성이 크지 않고, 꾸준히 배당을 주는 안정적인 주식에 투자하는 경향이 있습니다.",
			"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고려해 보는 것도 좋습니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높습니다.",
			"하이리스크-하이리턴을 선호하니, 주식시장에 큰 영향을 줄 수 있는 상황에 관심을 가지면서 시장 흐름을 파악하고 분석하는 자세가 필요합니다.",
			"하이리스크-하이리턴을 선호하니, 투자위험을 줄이기 위해 기업의 본질가치와 낮은 주가의 차이인 안전마진을 확보하려는 노력이 필요합니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
			"시장점유율이 높고 위험변수가 적은 기업에 투자하는 것을 선호하는 경향이 있기 때문에 시장대표주,  업종 내 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
			"투자대상의 미래가치를 주된 판단기준으로 삼기 때문에 불확실성이 큰 편입니다. 따라서, 떠오르는 새로운 기술은 무엇인지, 미래의 주요 비즈니스 모델은 무엇인지 등에 관심을 가질 필요가 있습니다.",
			"저PER주 투자 등 주가 변동이 크지 않고 꾸준히 배당을 주는 안정적인 주식에 투자하는 경향이 있습니다.",
			"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고려해 보는 것도 좋습니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높으니 투자기업의 본질가치가 훼손되었다면 과감한 리밸런싱도 필요할 수 있습니다.",
			"하이리스크-하이리턴을 선호하니, 주식시장에 큰 영향을 줄 수 있는 상황에 관심을 가지면서 시장 흐름을 파악하고 분석하는 자세가 필요합니다.",
			"하이리스크-하이리턴을 선호하니, 투자위험을 줄이기 위해 기업의 본질가치와 낮은 주가의 차이인 안전마진을 확보하려는 노력이 필요합니다.",
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
		int profLevelScore = (int)((14-report.getRiskLevelScore()) / 12. * 100 );
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
				"합리적으로 투자판단하는 솔로몬형 투자자",
				"나만의 투자원칙이 필요한 임기응변형 투자자",
				"분위기 따라 오르락 내리락 롤러코스터형 투자자",
				"유행을 좇는 테마형 투자자"
				};
		
		String[] investMainComment = {
				"당신은 명확한 투자원칙을 갖추고 있는 편입니다. 나만의 투자원칙을 만들고 일관되게 지키는 것은 분명 힘든 과정이지만, "
				+ "실패를 줄이고 성공투자로 나아가는 최선의 방법이 됩니다. 당신이 가지고 있는 몇몇 투자원칙은 평생투자라는 긴 항해에서 "
				+ "흔들리지 않는 나침반 역할을 해 줄 것입니다.",
				
				"당신은 대체로 외부의 다양한 변수와 급격한 상황변화에 흔들리지 않고 투자를 결정하는 편입니다. 그렇지만, "
				+ "자신의 투자목표를 정하고 위험 감내수준에 맞는 장기투자를 하고 자산배분으로 위험을 통제하고 주기적으로 재평가하는 "
				+ "규칙적인 투자를 하고 있는지 되돌아봐야 합니다\n\n"
				+ "투자에 있어서 긍정적 정보만 찾는 확증편향, 자신의 지식과 경험을 지나치게 믿는 자기과신편향, "
				+ "과거 정보를 의사결정의 근거로 삼는 기준점편향 등의 인지편향은 자신도 모르는 사이 언제 어디서든 발생할 수도 있습니다. "
				+ "이런 행동편향을 경계하고 투자지식과 투자경험을 쌓으며 나만의 투자원칙을 하나씩 세워 나간다면 일관성 있는 투자를 "
				+ "이어갈 수 있을 것입니다.",
				
				"당신은 심리적 오류와 편향으로 불합리한 투자결정을 내릴 가능성이 있습니다. 나도 모르게 자신의 능력 또는 판단이 유리한 "
				+ "결과를 가져다줄 것이라고 과신하여 투자위험의 가능성을 낮게 평가할 수도 있습니다. 그리고 보유한 주식을 과대평가하여 "
				+ "변화로 인한 이익은 과소평가하고 손실은 과대평가할 수도 있습니다. 또 기존의 판단에 맞는 정보만 찾고 그 정보를 더 "
				+ "중요하게 여겨 반대되는 정보의 중요성을 무시할 수 도 있습니다.\n\n"
				+ "투자에 있어서 불확실하고 혼란스러운 상황에 직면하게 될 때, 올바른 투자결정을 내린다는 것은 생각만큼 쉬운 일이 아닙니다. "
				+ "그렇기 때문에 나의 투자심리보다는 경제 상황, 정책과 금리, 기업 실적 등 객관적인 정보로 의사결정하는 투자습관을 들이는 "
				+ "것이 중요합니다.\n",
				
				"혹시 하루에도 몇 번씩 주식 앱을 켜고 시세 변화에 마음이 흔들리시나요? 상황을 객관적으로 판단하지 않고, 자신 판단의 "
				+ "정당성을 받쳐 줄 정보만을 찾고 있으시나요? 투자경험이 많다고 행동편향 징후를 무시하고 있으신가요? 자신의 판단이나 "
				+ "투자결정이 잘못임을 알면서도 계속 고집하고 있으신가요?\n\n"
				+ "심리적으로는 같은 크기의 이득보다는 손실로 인한 고통을 더 크게 느끼는 손실회피 편향, 자신에게 긍정적인 정보만 찾는 "
				+ "확증편향, 과거 정보를 의사결정의 근거로 삼는 기준점편향, 통제가능성에 대한 과신 등 다양한 편향들을 경계할 필요가 있습니다. "
				+ "그렇지 않다면, 투자하는 동안 심리적 안정을 얻기 힘들 수 있고 투자 목적에 다다르지 못한 채 방향을 잃을 수도 있습니다.\n\n"
				+ "이를 극복하기 위해선 자신만의 투자원칙을 세워야 합니다. 과도한 탐욕을 부리지 않고, 행동편향을 경계하고, 리딩방에 현혹되지 "
				+ "않아야 하고 투자지식을 계속 쌓고 실전경험를 반복하며 기업 성장의 과실을 함께 누리는 투자의 본질을 잊지 않으려는 노력이 "
				+ "필요합니다."};
		
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
	
	public Map<String, String> makeKnowledgeMainComment(
			int knowledgeScore,
			List<Problem> probList,
			List<StatementDTO> statementForDetail)
	{
		String[] knowledgeMain = {
				"기초와 실전 지식을 갖춘 준비된 투자자", 
				"투자지식의 기초는 알고있는 투자자", 
				"투자기초부터 탄탄한 학습이 필요한 투자자"};
		String[] knowledgeDetailCorrList = {
				"높은 수익률 제안에 의심을 품을 줄 알고 있습니다.",
				"수익대비위험 개념에 대해 잘 알고 있습니다.",
				"주식투자의 개념/원리에 대해 알고 있습니다.",
				"장기투자 이점에 대한 지식을 가지고 있습니다.",
				"분산투자에 대해 이해하고 있습니다.", 
				"금융상품의 특징에 대해 이해하고 있습니다.",
				"기업의 본질가치에 대해 잘 알고 있습니다.",
				"기업의 사업가치 개념 중 영업이익, 어닝 서프라이즈 등을 잘 알고 있습니다.",
				"기업의 안전가치(부채비율)과 주주의 권리를 이해하고 있습니다.",
				"기업의 시장가치를 잘 알고 있습니다.",
				"주가배수 개념을 잘 알고 있습니다.",
				"시가총액의 개념을 잘 알고 있습니다.",  
				"종목고르기를 위한 종합적인 지식을 갖추고 있습니다.",
				"배당의 성격과 의미를 잘 알고 있습니다.",
				"배당기준일의 개념을 잘 알고 있습니다.",
				"주식의 발행물량이 주가에 미치는 영향을 잘 알고 있습니다.",
				"기업 규모, 분류에 따른 가격변동요인을 잘 알고 있습니다.",
				"주식의 단기적인 가격 변동 요인을 잘 알고 있습니다.",
				"기업이 속한 산업 및 업종의 특징이 주가에 미치는 영향을 잘 알고 있습니다.",
				"기업의 역량이 주가변동에 미치는 영향을 잘 알고 있습니다.",
				"기업의 실적이 주가변동에 미치는 영향을 잘 알고 있습니다.",
				"원자재 가격변화가 주식 가격변화에 미치는 영향을 잘 알고 있습니다.",
				"경기 변화가 주식 가격변화에 미치는 영향을 잘 알고 있습니다.",
				"금리 변화가 주식 가격변화에 미치는 영향을 잘 알고 있습니다.",
				"주식 투자에 있어 공시에 대해 잘 알고 있습니다.",
				"주식 투자의 손익 계산을 잘 알고 있습니다.",
				"주식의 주문, 결제 방법을 잘 알고 있습니다.",
				"주식 시세에 대해 이해하고 있습니다.",
				"공모주 등 여러 종류의 주식 특징에 대한 설명을 이해하고 있습니다.",
				"과거부터 이어지는 주가의 흐름을 이해하고 있습니다.",
				"매매전략(분할매수, 분할매도)을 이해하고 있습니다.",
				"주식 거래 시에 발생하는 세금과 수수료를 이해하고 있습니다.",
				"챠트 분석의 기초에 대해 잘 알고 있습니다."

		};
		String[] knowledgeDetailWrongList = {
				"높은 수익률 제안에 의심을 품고 정보를 객관적으로 분석할 수 있어야 합니다.",
				"하이리스크 vs. 하이리턴과 같은 수익대비위험 개념을 학습할 필요가 있습니다.",
				"주식투자의 개념/원리에 대해서도 더 알아보기 바랍니다.",  
				"스노볼효과와 복리 효과에 대한 학습이 필요합니다.",
				"분산투자에 대한 이해가 필요합니다.", 
				"투자목표에 적합한 금융상품을 찾기 위해선 금융상품의 특징을 이해해야 합니다.",
				"기업의 본질가치에 대한 학습이 필요합니다.",
				"기업의 사업가치 개념을 더 알아보시기 바랍니다.",
				"기업의 안전가치와 주주의 권리를 더 알아보시기 바랍니다.",
				"기업의 시장가치에 대한 추가적인 학습이 필요합니다.",
				"주가배수 개념을 다시 확인하시기 바랍니다.",
				"시가총액의 개념을 다시 확인하시기 바랍니다.",
				"종목고르기를 위한 종합적인 학습이 필요합니다.",
				"배당의 성격과 의미를 더 알아보시기 바랍니다.",
				"배당기준일의 개념을 더 알아보시기 바랍니다.",
				"주식의 발행물량이 주가에 미치는 영향을 더 알아보시기 바랍니다.",
				"기업 규모, 분류에 따른 가격변동요인을 더 알아보시기 바랍니다.",
				"주식의 단기적인 가격 변동 요인을 더 알아보시기 바랍니다.",
				"기업이 속한 산업 및 업종의 특징이 주가에 미치는 영향을 더 알아보시기 바랍니다.",
				"기업의 역량이 주가변동에 미치는 영향을 더 알아보시기 바랍니다.",
				"기업의 실적이 주가변동에 미치는 영향을 더 알아보시기 바랍니다.",
				"원자재 가격변화가 주식 가격변화에 미치는 영향을 더 알아보시기 바랍니다.",
				"경기 변화가 주식 가격변화에 미치는 영향을 더 알아보시기 바랍니다.",
				"금리 변화가 주식 가격변화에 미치는 영향을 더 알아보시기 바랍니다.",
				"주식 투자에 있어 공시에 대해 더 알아보시기 바랍니다.",
				"주식 투자의 손익 계산을 더 알아보시기 바랍니다.",
				"주식의 주문, 결제 방법을 더 알아보시기 바랍니다.",
				"주식 시세에 대해 더 알아보시기 바랍니다.",
				"공모주 등 여러 종류의 주식 특징에 대해 더 알아보시기 바랍니다.",
				"과거부터 이어지는 주가의 흐름을 더 알아보시기 바랍니다.",
				"매매전략(분할매수, 분할매도)을 더 알아보시기 바랍니다.",
				"주식 거래 시에 발생하는 세금과 수수료를 더 알아보시기 바랍니다.",
				"챠트 분석의 기초를 더 알아보시기 바랍니다."

		};
		
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
					detIdx = (diffIdx == 0) ? 0 : 1;
				else if(probOrderIdx > 1)
					detIdx = 3 + (probOrderIdx - 2) * 3 + diffIdx;
				
				knowledgeDetail += (isCorrect) ? knowledgeDetailCorrList[detIdx] + "\n"
						:knowledgeDetailWrongList[detIdx] + "\n";
			}
		}
		
		
		result.put("main", knowledgeMain[knowledgeScoreIdx]);
		result.put("detail", knowledgeDetail);
		
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
