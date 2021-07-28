package studio.teststudio.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(
        name="PROB_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="PROB_SEQ", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
        )
@Table(name = "PROBLEM")
@Getter
@Setter
public abstract class Problem {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROB_SEQ_GEN")
	@Column(name = "PROB_ID")
	private Long probId;
	@Column(name = "ANSWER_TYPE")
	private String answerType;
	private String question;
	private String solution;
	private String difficulty;
	private String category;
	@Column(name = "IMG_SRC")
	private String imgsrc;
	@Column(name = "TIME_RECOMMENDATION")
	private Float timeRecommendation;
	@Column(name = "CREATOR_ID")
	private String creatorId;
	@Column(name = "CREATE_DATE")
	private Timestamp createDate;
	@Column(name = "VALIDATOR_ID")
	private String validatorId;
	@Column(name = "VALIDATE_DATE")
	private Timestamp validateDate;
	@Column(name = "EDITOR_ID")
	private String editorId;
	@Column(name = "EDIT_DATE")
	private Timestamp editDate;
	private String source;
	private String invention;
	@Enumerated(EnumType.STRING)
	private ProblemStatus status;
	
	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
	private List<ProblemUkRel> problemUkRel = new ArrayList<>();
	
	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
	private List<ProblemChoice> problemChoice = new ArrayList<>();
	
}