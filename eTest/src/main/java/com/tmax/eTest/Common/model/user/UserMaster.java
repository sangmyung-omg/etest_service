package com.tmax.eTest.Common.model.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmax.eTest.Auth.dto.Gender;
import com.tmax.eTest.Auth.dto.AuthProvider;
import com.tmax.eTest.Auth.dto.Role;
import com.tmax.eTest.Common.model.error_report.ErrorReport;
import com.tmax.eTest.Common.model.support.Inquiry;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="USER_MASTER")
@ToString(exclude = "inquiry")
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

	@Enumerated(EnumType.STRING)
	private Role role;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

	@OneToMany(mappedBy="user")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();

	@Column(name = "NICK_NAME")
	private String nickname;

	private String name;

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
	
	// 유저 투자 경력 Index
	private Integer investPeriod;

	@OneToMany(mappedBy = "userMaster",  cascade = {CascadeType.REMOVE},fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"userMaster"})
	private List<Inquiry> inquiry;

	@Column(name = "CREATE_DATE")
	private LocalDateTime createDate;

	@PrePersist // 디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

}
