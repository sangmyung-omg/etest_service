package com.tmax.eTest.Common.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="USER_MASTER")
@Builder
public class UserMaster {
	@Id
	private String userUuid;

	private String name;

	private String username;

	private String userType;

	@Column(nullable = false)
	private String password;

	@Column(length = 100,  unique = true)
	private String email;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();
}
