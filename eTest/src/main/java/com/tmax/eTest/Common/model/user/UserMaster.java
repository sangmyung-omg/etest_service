package com.tmax.eTest.Common.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmax.eTest.Auth.model.AuthProvider;
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

	private String UserUuid;
	private String UserType;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@JsonIgnore
	private String password;

	private String role;

	@Email
	@Column(nullable = false)
	private String email;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();

	private String providerId;

	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

	@Column(nullable = false)
	private Boolean emailVerified = false;
}
