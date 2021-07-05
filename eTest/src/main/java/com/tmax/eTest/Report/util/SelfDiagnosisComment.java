package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SelfDiagnosisComment {

	String[] finalResultComment ={
		"하하하",	"하하중",	"하하상",
		"하중하",	"하중중",	"하중상",
		"하상하",	"하상중",	"하상상",
		"중하하",	"중하중",	"중하상",
		"중중하",	"중중중",	"중중상",
		"중상하",	"중상중",	"중상상",
		"상하하",	"상하중",	"상하상",
		"상중하",	"상중중",	"상중상",
		"상상하",	"상상중",	"상상상",
	};
	
	String[] riskDiagnosisComment = {
		"하하",
		"하상",
		"상하",
		"하상"
	};
	
	String[] riskPatienceComment = {
		"안정적 투자를 지향하지만 투자 가능 기간이 긴 편은 아니기 때문에 꾸준한 모니터링이 필요함", // 보수, 보수, 답변 조건 X
		"리스크 감내역량이 있으니, 공격적 투자 고려 가능", // 보수, 보수, 답변 조건 O
		"공격적 투자 성향을 가지고 있지만, 투자자본 등 여건 상 보수적 투자방식을 유지하는게 안정적일 수 있음", // 보수, 공격, 답변 조건 X
		"리스크 감내하는 성향을 가지고 있고 역량도 갖춘 상태이므로, 공격적 투자 고려 가능", // 보수, 공격, O
		"성향과 역량을 고려하지 않는 투자를 하고 있으므로, 안정적 포트폴리오로 변경을 고려해야 함", // 공격, 보수, X
		"리스크 감당할 역량은 있기 때문에 현재의 공격적 투자 가능할 수 있음. 단 본인의 성향과 맞추어 투자하는 것을 고려해 볼 수 있음", // 공격, 보수, O
		"성향대로 공격적인 투자를 하고 있지만, 현실적으로 리스크를 감당할 수 있는 여건이 부족한 상황이므로, 안정적 포트폴리오로 구성 변경을 고려해볼 수 있음", // 공격, 공격, X
		"리스크 성향, 감내역량 충족"		
	};
	
	// 모두 트리톤을 타지 않고 나오는 점수들. (단순 알고리즘으로 나오는 점수)
	// 위험 적합도 = riskScore
	// 투자 결정 적합도 = investDecisionScore
	// 지식 이해도 = knowledgeScore
	String getFinalResultComment(int riskScore, int investDecisionScore, int knowledgeScore)
	{
		int idx = 0;
		
		idx += riskScore >= 75 ? 18 : riskScore >= 55 ? 9 : 0;
		idx += investDecisionScore >= 75 ? 6 : riskScore >= 55 ? 3 : 0;
		idx += knowledgeScore >= 70 ? 2 : knowledgeScore >= 50 ? 1 : 0;
		
		return finalResultComment[idx];
	}
	
	String getRiskPatienceComment(int recentInvestmentScore, int riskScore, int riskQ1, int riskQ2)
	{
		int idx = 0;
		
		idx += recentInvestmentScore >= 10 ? 4 : 0;
		idx += riskScore >= 12 ? 2 : 0;
		idx += (riskQ1 == 1 && riskQ2 != 1) ? 1 : 0;
		
		return riskPatienceComment[idx];
	}
}
