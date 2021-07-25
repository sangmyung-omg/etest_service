package com.tmax.eTest.Test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.tmax.eTest.Contents.model.ErrorReport;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name="USER_MASTER")
@Builder
public class UserMaster {
	@Id
	private String userUuid;

	private String name;

	private String userType;

	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;
	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();
}
