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
				"티끌모아 태산 부지런한 개미형 투자자",
				"안전과 수익 두 마리 토끼를 잡자. 이상적 투자자",
				"내가 할 수 있는 만큼만! 멘탈부자 투자자",
				"주식은 수익이 우선이지. 마이웨이형 투자자"};
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (report.getRiskLevelScore() < 8) ? 0 : 1;			// 11~13 Question 3 ~ 7 / 8 ~ 12
		int tracingIdx = (report.getRiskStockRatioScore() < 3) ? 0 : 1;		// 2 Question 1,2 / 3,4
		int typeIdx = profileIdx * 2 + tracingIdx;
		
		String riskComment = "";
		
		String[] investRiskComment = {	//211006 ver
			"당신은 원금손실 등 투자에서 발생하는 위험을 피하는 투자자 입니다. 자신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있기도 합니다.",
			"당신은 원금손실 등 투자에서 발생하는 위험을 피하는 투자자 입니다. 투자 과정에서 심각한 실수나 손실을 줄이는 것에 중점을 두는 경향이 있습니다.",
			"당신은 원금손실 등 투자에서 발생하는 위험을 피하는 투자자이지만, 때로는 수익을 내기 위해서 기꺼이 투자위험을 감수하려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 당신의 재무능력과 투자위험 간에 균형을 맞추려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 있습니다.",
			"당신은 투자목표를 위해 투자위험을 감내하는 투자자입니다. 특히 투자수익을 내기 위하여 높은 투자위험을 감수하려는 경향이 매우 강합니다."
		};
		String[] investMethodComment = {	// 211006 ver
			"투자방법에 있어서는 느리더라도 꾸준한게 중요하다고 생각합니다. 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 느리더라도 꾸준한게 중요하다고 생각합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 느리더라도 꾸준한게 중요하다고 생각합니다. 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 느리더라도 꾸준한게 중요하다고 생각합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 우량주 및 블루칩 등 안정성이 높은 자산에 집중투자하는 경향이 있습니다.",
			"투자방법에 있어서는 높은 수익률을 얻을 수 있는 방법을 추구합니다. 성장성이 높은 자산에 집중투자하는 경향이 있습니다."
		};
		String[] combiComment = {			// 211006 ver
			"손실위험을 최소화하는 안정적인 투자를 선호하며, 금융상품이나 종목을 선택할때도 위험이 최소화된 방향으로 투자를 하려고 합니다.",
			"안정적이고 낮은 수익률로 포트폴리오를 구성했다면, 높은 수익률에 유혹을 느낄 수 있습니다.",
			"위험성향과 실제 투자 방식이 상당부분 일치하기 때문에 주변 사람들이 봤을때는 안정적으로 투자하는 사람으로 여겨질 수 있습니다.",
			"여러 종목 투자를 선호하지만, 스스로 여러 종목을 관리하기 어렵다면 펀드 등과 같은 투자상품을 고려하는 것도 방법일 수 있습니다.",
			"위험을 회피하려는 성향 때문에 가격 변동이 커졌을때 심적으로 흔들릴 가능성이 큽니다.",
			"안정형과 공격형 두 성향을 모두 가지고 있습니다. 투자 후 계속 주식앱을 보며 불안에 떨고 있다면 안정투자형에 가깝고, 만족스럽지 못한 수익에 밤잠을 설치면 공격투자형에 가깝습니다.",
			"높은 수익률을 추구하면서도 위험을 관리하려는 성향을 보이기 때문에, 투자경력이 쌓였다면 투자한 자본 수익이 누적되어 복리화되는 과정이 시작되었을 확률이 높습니다.",
			"높은 수익과 위험을 추구하는 성향으로 공격적인 투자방법을 선택하고 있을 수 있습니다.",
			"주식투자를 고려할 수 도 있지만, 원금이 보장되거나 국채 등 안정적인 투자에서 심리적 안정감을 느낄 수 있습니다.",
			"종목에 대한 정확한 분석을 전제로 나의 상황을 이해하고 투자원칙을 수립하는 것이 중요합니다.",
			"투자수익률이 낮더라도 원금보장이 확실한 금융상품을 투자할때 편안함을 느낄 수 있습니다.",
			"투자에 들일 수 있는 시간이 부족하다면 자산관리사 등 \"전문가\"의 도움을 받는 것도 방법일 수 있습니다.",
			"자신의 위험회피 성향과 다른 고수익을 추구하는 집중투자를 선택하여 그로 인하여 증가하는 리스크를 감수해야 합니다.",
			"투자손실을 최소화하려는 성향 때문에 가격 변동이 커졌을 때 심리적으로 흔들릴 가능성이 있습니다.",
			"저평가된 주식을 매수하여 제 가치에 매도하기 위해서는 기업의 가치평가 방법에 관심을 가지고 습득할 필요가 있습니다.",
			"성향상 고수익/고위험 종목의 투자비중이 높기 때문에, 시장상황을 지속적으로 확인할 필요가 있습니다.",
			"투자기간은 비교적 단기간으로 설정하고 예상수익률이 높다면 위험이 높은 상품을 집중적으로 선택하기도 합니다.",
			"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"아직은 시장에서 저평가된 기업을 찾아서 투자하는 것을 선호합니다. 투자하고자 하는 기업의 재무정보, 시장전망 뿐만 아니라 경제 상황, 정책과 금리, 기업 실적 등 객관적 자료를 기반으로 의사결정을 해야합니다.",
			"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"투자기간은 비교적 단기간으로 설정하고, 높은 수익을 목표로하기 때문에 위험이 높은 상품을 집중적으로 선택하기도 합니다.",
			"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"선택한 종목의 기업가치 변화를 꼼꼼히 체크해야 합니다. 기업가치의 변화는 매분기 발표되는 실적보고서를 통해서 확인할 수 있습니다.",
			"성장하고 있는 또는 성장할 것으로 예상되는 주식에 집중투자하고 있기 때문에 옥석을 가려 투자하는 능력이 중요합니다.",
			"주식시장에서 저평가된 가격에 사서 가치가 반영되는 동안 오래 기다릴 수 있어 대출 등 신용거래를 해서는 안 됩니다.",
			"적은 수의 종목 수익률을 관리하기 때문에 수익폭이 매우 클 수 있으며 한 종목에서 발생할 수 있는 리스크가 크다는 것을 인지해야 합니다.",
			"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 큰 수익이 나지 않는다고 생각할 수 있습니다.",
			"고위험을 선호하는 성향과 안전수익을 추구하는 투자방법간의 차이로 큰 수익이 나지 않는다고 생각할 수 있어 펀드, ETF 등 간접투자도 고려해 볼만 합니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 변동성이 큰 시장 상황에서도 잘 견딜 수 있으며 투자성과를 내기 위해서는 장기투자가 필요할 수 있습니다.",
			"자신의 위험성향과 실제 투자방식이 상당부분 일치하고 투자하려는 종목은 늘 고평가 논란에 시달릴 수 있으니 옥석을 잘 가릴 필요가 있습니다.",
			"시장점유율이 높고 위험변수가 적은 기업에 투자하는 것을 선호하는 경향이 있기 때문에 시장대표주, 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
			"투자하는 종목의 특성 상 기대되는 영업이익을 예상하고 그 가치를 평가하여야 하므로 투자리스크가 큰 편입니다. 이를 대비하기 위해서는 어떤 새로운 기술이 부상하는지, 미래의 주요 비즈니스 모델은 무엇인지에 관심을 가져야 합니다.",
			"주가 변동성이 크지 않고, 꾸준히 배당을 주는 안정적인 주식에 투자하는 것을 선호합니다.",
			"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고래해 보는 것도 좋습니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높습니다.",
			"하이리스크-하이리턴을 선호하니, 주식시장에 큰 영향을 줄 수 있는 상황에 관심을 가지면서 시장 흐름을 민감하게 파악하고 분석하는 자세가 필요합니다.",
			"하이리스크-하이리턴을 선호하니, 기업의 가치와 낮은 주가의 차이인 안전마진을 확보하는 노력이 필요합니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높아 기업의 가치변화를 꼼꼼히 체크할 필요가 있습니다.",
			"시장점유율이 높고 위험변수가 적은 기업에 투자하는 것을 선호하는 경향이 있기 때문에 시장대표주, 1등 기업 등에 투자를 고려해 보는 것도 좋습니다.",
			"투자하는 종목의 특성 상 기대되는 영업이익을 예상하고 그 가치를 평가하여야 하므로 투자리스크가 큰 편입니다. 이를 대비하기 위해서는 어떤 새로운 기술이 부상하는지, 미래의 주요 비즈니스 모델은 무엇인지에 관심을 가져야 합니다.",
			"저PER주 투자 등 주가 변동이 크지 않고 꾸준히 배당을 주는 안정적인 주식에 투자하는 것을 선호합니다.",
			"높은 수익을 추구하기 위해 글로벌 시장에도 적극적인 투자를 고려해 보는 것도 좋습니다.",
			"하이리스크-하이리턴을 선호하여 큰 수익을 가져올 수도 있지만 반대로 손실을 볼 확률도 높으니 투자한 기업과 시장상황이 악화된다면 과감한 리밸런싱이 필요할 수 있습니다.",
			"하이리스크-하이리턴을 선호하니, 주식시장에 큰 영향을 줄 수 있는 상황에 관심을 가지면서 시장 흐름을 민감하게 파악하고 분석하는 자세가 필요합니다.",
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
		
		String[] profileMainList = {"위험을 회피하는 안정형 투자자", 
				"위험을 감내하는 공격형 투자자"};
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
				"용의주도한 탐정형 투자자",
				"나만의 원칙이 필요해. 예비 주식고수",
				"투자고수 포텐을 가진 대기만성형 투자자",
				"흔들린다 흔들려. 촛불형 투자자"
				};
		
		String[] investMainComment = {
				"당신은 명확한 투자원칙을 갖추고 있는 편입니다. 주식투자 개념, 사업보고서나 재무제표 보는법 등 주식에 필요한 기초 지식을 "
				+ "갖추고 투자하고 있을 가능성이 높습니다. 투자의 목표를 세우고 이를 달성하기 위한 구체적인 실행방법을 가진 용의주요한 "
				+ "성향향을 가지고 있습니다. 나만의 투자원칙을 만들고 일관되게 지키는 것은 분명 힘든 과정이지만, 실패를 줄이고 성공투자로 "
				+ "나아가는 최선의 방법이 됩니다. 당신이 가지고 있는 몇몇 투자원칙은 평생투자라는 긴 항해에서 흔들리지 않는 나침반 역할을 "
				+ "해 줄 것입니다.",
				
				"당신은 투자에 있어서 심리적 오류나 불합리한 의사결정이 무엇인지는 인지하고 있지만, 나만의 투자원칙이 필요할 "
				+ "가능성이 큽니다. 투자에 있어서 심리적 오류는 나도 모르는 사이 언제 어디서든 발생할 수 있습니다. 긍정적 "
				+ "정보만 찾는 확증편향, 자신의 지식과 경험을 지나치게 믿는 자기과잉, 과거 정보를 의사결정의 근거로 삼는 앵커링 "
				+ "효과 등 다양한 편향들을 인지하고 객관적 분석에 근거한 투자결정을 추구해야 합니다.\n\n"
				+ "객관적인 분석에 근거한 투자 결정을 지향하더라도 투자원칙이 확고하지 않다면, 작은 변동에도 불안하거나 크게 "
				+ "흔들릴 수 있습니다. 급변하는 주가에 마음이 불안하고 주변 사람들의 이야기에 솔깃하지 않도록 조심해야 합니다. "
				+ "그렇지만 학습과 투자경험이 쌓이고 나만의 투자원칙을 하나씩 장착한다면 흔들림 없이 투자를 이어갈 수 있을겁니다.",
				
				"당신은 심리적 오류와 편향으로 불합리한 투자결정을 내릴 가능성은 있지만, 대체로 자신만의 투자 원칙을 지키려고 합니다. "
				+ "나도 모르게 내가 보유한 종목에 유리한 정보를 열심히 찾아다니거나 나의 선택이 틀리지 않았다는 것을 확인하려고 하는 "
				+ "경향도 있습니다.\n\n"
				+ "투자에 있어서 불확실하고 혼란스러운 상황에 직면하게 될 때, 올바른 투자결정을 내린다는 것은 생각만큼 쉬운 일이 아닙니다. "
				+ "하지만 나의 심리보다는 경제 상황, 정책과 금리, 기업 실적 등 객관적 자료를 기반으로 의사결정하는 연습을 지속한다면 "
				+ "발전할 가능성이 굉장히 높습니다.",
				
				"혹시 하루에도 몇 번씩 주식 앱을 켜고 시세 변화에 마음이 흔들리시나요? 상황을 객관적으로 판단하지 않고, 자신 판단의 정당성을 "
				+ "받쳐 줄 정보만을 찾고 있으시나요? 투자경험이 많다고 행동편향 징후를 무시하고 있으신가요? 자신의 판단이나 투자결정이 잘못임을 "
				+ "알면서도 계속 고집하고 있으신가요?\n\n"
				+ "자신에게 긍정적인 정보만 찾는 확증편향, 과거 정보를 의사결정의 근거로 삼는 기준점편향, 통제가능성에 대한 과신 등 다양한 "
				+ "편향들을 경계할 필요가 있습니다. 심리적으로는 같은 크기의 이득보다는 손실로 인한 고통이 더 크게 느끼면서 손실회의 편향에 "
				+ "빠질 수도 있습니다. 이를 극복하기 위해선 자신만의 투자원칙이 세워져 있어야 합니다. 그렇지 않다면 투자하는 동안 심리적 안정을 "
				+ "얻기 힘듭니다.\n\n"
				+ "자기 합리화를 통한 판단은 객관적인 시장상황과 원칙이 반영되지 않았기 때문에 주식투자에 있어 치명적으로 작용할 수 있습니다. "
				+ "균형적이고 이성적인 판단이 결코 쉽지는 않지만 최대한 여러 시각으로 바라보고 판단하려는 노력이 필요합니다."};
		
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
				"주식투자에서 수익을 낼 수 있는 가장 적합한 장기투자 방법에 대한 지식을 가지고 있습니다.",
				"투자위험과 회피방법인 분산투자에 대해 이해하고 있습니다.", 
				"명확한 투자목표와 그를 위해 선택하는 금융상품의 특징을 이해하고 있습니다.",
				"기업의 본질가치 산정 방식에 대해 잘 알고 있습니다.",
				"기업의 사업가치 개념 중 영업이익, 어닝에 대해 잘 알고 있습니다.",
				"기업의 안전가치 측정 방법(부채비율)에 대해 잘 알고 있습니다.",
				"시장가치 한계에 대해 잘 알고 있습니다.",
				"주가배수 개념에 대해 잘 알고 있습니다.",
				"시가총액의 개념에 대해서 잘 알고 있습니다.",  
				"종목고르기를 위한 종합적인 지식을 갖추고 있습니다.",
				"배당 성격 및 의미에 대해서 잘 알고 있습니다.",
				"배당의 개념에 대해서 잘 알고 있습니다.",
				"주식의 발행물량이 주가에 미치는 영향에 대해 잘 알고 있습니다.",
				"주식의 종류에 따른 가격변동요인에 대해 잘 알고 있습니다.",
				"주식의 단기적인 가격 변동 요인에 대해 잘 알고 있습니다.",
				"기업이 속한 산업의 상황이 주가에 미치는 영향에 대해 잘 알고 있습니다.",
				"기업의 역량이 주가변동에 미치는 영향에 대해 잘 알고 있습니다.",
				"기업의 실적이 주가변동에 미치는 영향에 대해 잘 알고 있습니다.",
				"원자재 가격변화가 주식 가격변화에 미치는 영향에 대해 잘 알고 있습니다.",
				"경기 변화가 주식 가격변화에 미치는 영향에 대해 잘 알고 있습니다.",
				"금리 변화가 주식 가격변화에 미치는 영향에 대해 잘 알고 있습니다.",
				"주식 투자에 있어 공시 보는 방법에 대해 잘 알고 있습니다.",
				"주식 투자의 손익 계산에 대해 잘 알고 있습니다.",
				"주식의 주문, 결제 방법에 대해 잘 알고 있습니다.",
				"현재 형성된 주식의 가격에 대해 잘 이해하고 있습니다.",
				"여러 가지의 주식 가격에 대한 설명을 잘 이해하고 있습니다.",
				"과거부터 이어지는 주가의 흐름에 대해 잘 이해하고 있습니다.",
				"매매전략(분할매수, 분할매도)에 대해 잘 이해하고 있습니다.",
				"주식 거래 시에 발생하는 세금과 수수료에 대해 잘 알고 있습니다.",
				"기술적 분석의 기초에 대해 잘 알고 있습니다."
		};
		String[] knowledgeDetailWrongList = {
				"높은 수익률 제안에 의심을 품고 일고의 가치도 없다는 것을 알아야 합니다.",
				"하이리스크 vs. 하이리턴과 같은 수익대비위험 개념에 대해 학습할 필요가 있습니다.",
				"기업에 투자한다는 주식투자의 개념/원리에 대해서도 더 알아보기 바랍니다.",  
				"주식투자에서 수익을 낼 수 있는 가장 적합한 장기투자 방법에 대한 이해가 부족하군요. 스노볼효과와 복리 효과에 대한 학습이 필요합니다.",
				"투자위험과 회피방법인 분산투자에 대한 이해가 필요합니다.", 
				"명확한 투자목표와 그를 위해 선택하는 금융상품의 특징을 이해해야 합니다.",
				"기업의 본질가치 산정 방식에 대해 학습이 필요합니다.",
				"기업의 사업가치 개념에 대해서도 알아보세요.",
				"기업의 안전가치 측정 방법(부채비율)에 대해서도 알아보세요.",
				"시장가치 한계에 대해서 추가적인 학습이 필요합니다.",
				"주가배수 개념에 대해서도 다시 확인하시기 바랍니다.",
				"시가총액의 개념에 대해서도 다시 확인하시기 바랍니다.",
				"종목고르기를 위한 개발 지식의 종합적인 판단력을 더 길러보기 바랍니다.",
				"배당 성격 및 의미에 대해서 더 공부해 보세요.",
				"배당의 개념에 대해서 더 공부해 보세요.",
				"주식의 발행물량이 주가에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"주식의 종류에 따른 가격변동요인에 대해 더 알아보시기 바랍니다.",
				"주식의 단기적인 가격 변동 요인에 대해 더 알아보시기 바랍니다.",
				"기업이 속한 산업의 상황이 주가에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"기업의 역량이 주가변동에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"기업의 실적이 주가변동에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"원자재 가격변화가 주식 가격변화에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"경기 변화가 주식 가격변화에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"금리 변화가 주식 가격변화에 미치는 영향에 대해 더 알아보시기 바랍니다.",
				"주식 투자에 있어 공시 보는 방법에 대해 더 알아보시기 바랍니다.",
				"주식 투자의 손익 계산에 대해 더 알아보시기 바랍니다.",
				"주식의 주문, 결제 방법에 대해 더 알아보시기 바랍니다.",
				"현재 형성된 주식의 가격에 대해 더 알아보시기 바랍니다.",
				"여러 가지의 주식 가격에 대한 설명을 더 알아보시기 바랍니다.",
				"과거부터 이어지는 주가의 흐름에 대해 더 알아보시기 바랍니다.",
				"매매전략(분할매수, 분할매도)에 대해 더 알아보시기 바랍니다.",
				"주식 거래 시에 발생하는 세금과 수수료에 대해 더 알아보시기 바랍니다.",
				"기술적 분석의 기초에 대해 더 알아보시기 바랍니다."
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
