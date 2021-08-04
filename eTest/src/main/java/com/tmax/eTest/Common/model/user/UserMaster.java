package com.tmax.eTest.Common.model.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmax.eTest.Auth.dto.Gender;
import com.tmax.eTest.Auth.dto.AuthProvider;
import com.tmax.eTest.Auth.dto.Role;
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
	@Column(name = "USER_UUID")
	private String userUuid;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "PROVIDER_ID")
	private String providerId;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

	@Email
	@Column(nullable = false)
	private String email;

	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();

	// 이벤트 알람 동의 (선택)
	private Boolean event_sms_agreement;
	// 장기 미 접속시 계정 활성화 (선택)
	private Boolean account_active;
	// 만 14세 이상 (필수)
	private Boolean older_than_14 ;
	// 서비스 이용약관에 동의합니다(필수)
	private Boolean service_agreement  ;
	// 개인정보 수집이용에 동의합니다(필수)
	private Boolean collect_info  ;

}
