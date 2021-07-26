package com.tmax.eTest.Common.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

import lombok.Data;

@Data
@Entity
@Table(name="USER_MASTER")
public class UserMaster {
	@Id
	private String userUuid;
	private String name;
	private String userType;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();
}
