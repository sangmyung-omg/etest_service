package com.tmax.eTest.Common.model.user;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.Data;


@Entity
@Table(name="USER_KNOWLEDGE")
@Data
public class UserKnowledge {
	@Id
	private String userUuid;

	private String ukMastery;
	private Timestamp updateDate;

}
