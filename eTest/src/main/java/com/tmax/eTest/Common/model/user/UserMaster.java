package com.tmax.eTest.Common.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmax.eTest.Auth.dto.Gender;
import com.tmax.eTest.Auth.model.AuthProvider;
import com.tmax.eTest.Auth.model.Role;
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
	private String userType;

	private String name;

	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Email
	@Column(nullable = false)
	private String email;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

}
