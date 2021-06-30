package com.tmax.eTest.Test.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name="USER_KNOWLEDGE")
@Data
@IdClass(UserKnowledgeKey.class)
public class UserKnowledge {
	@Id
	private String userUuid;

	@Id
	private Integer ukUuid;
	
	private String ukId;
	
	private Float ukMastery;
	private Timestamp updateDate;
	
	@OneToOne(cascade=(CascadeType.ALL))
	@JoinColumn(name="ukId", insertable = false, updatable = false)
	private UkMaster ukDao;
}
