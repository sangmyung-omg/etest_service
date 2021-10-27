package com.tmax.eTest.Report.util.diagnosis;

public class DiagnosisUtil {
	

	// Main Score Name
	public enum ScoreKey{
		KNOWLEDGE("지식이해도"),
		RISK("리스크점수"),
		RISK_TRACING("트레이싱점수"),
		RISK_PROFILE("프로파일점수"),
		INVEST("의사결정적합도점수"),
		GI("GI점수");
		
		private String value;
		
		private ScoreKey(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	

	public enum AnswerKey{
		RISK_1("RiskA1"),
		RISK_2("RiskA2");
		
		private String value;
		
		private AnswerKey(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	
	
	public enum Chapter{
		KNOWLEDGE("지식"),
		TENDENCY("성향");
		
		private String value;
		
		private Chapter(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	
	public enum TendencySection{
		RISK_TRACING("투자현황"),
		RISK_PROFILE("리스크"),
		INVEST_TRACING("투자원칙"),
		INVEST_PROFILE("인지편향");
		
		private String value;
		
		private TendencySection(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	// Detail Score Name
	
	// Risk Profile Detail
	public enum RiskProfile{
		CAPACITY("리스크 감내역량"),
		LEVEL("리스크 감내수준");
		
		private String value;
		
		private RiskProfile(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	// Risk Tracing Detail(SubSection
	public enum RiskTracing{
		STOCK_PERIOD("투자현황"),
		STOCK_RATIO("주식투자 비중"),
		STOCK_NUM("보유 종목 수"),
		STOCK_PREFER("선호 투자 종목");
		
		private String value;
		
		private RiskTracing(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	// Invest Profile Detail(SubSection)
	public enum InvestProfile{
		BIAS_ANCHOR("기준점편향"),
		BIAS_SELF("자기과신편향"),
		BIAS_LOSS("현상유지편향"),
		BIAS_CONFIRM("사후확증편향"),
		BIAS_CROWN("군중심리편향");
		
		private String value;
		
		private InvestProfile(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	// Invest Tracing Detail(SubSection)
	public enum InvestTracing{
		RULE_SELL("매매기준 원칙"),
		RULE_PORTFOLIO("포트폴리오 원칙"),
		RULE_INFO("정보분석 원칙"),
		RULE_METHOD("투자방법 원칙");
		
		private String value;
		
		private InvestTracing(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	// Knowledge Detail
	
	public enum KnowledgeSection{
		BASIC("투자기초"),
		TYPE_SELECT("종목고르기"),
		PRICE_CHANGE("가격 변동 특징"),
		SELL_WAY("매매방법");
		
		private String value;
		
		private KnowledgeSection(String value) {
			this.value = value;
		}
		
		public final String toString() {
			return this.value;
		}
	}
	
	public enum KnowledgeSubSection{
		PROFIT_GUARANTEED("수익보장경계"),
		BASIC("기본개념"),
		INVEST_RULE("투자원칙");
		
		private String value;
		
		private KnowledgeSubSection(String value) {
			this.value = value;
		}
		
		public final String toStringForScoreMap(){
			return "투자지식-"+this.value;
		}
		public final String toString() {
			return this.value;
		}
	}

}
