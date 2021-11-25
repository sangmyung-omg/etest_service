package com.tmax.eTest.Auth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.book.BookBookmark;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.repository.book.BookBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import com.tmax.eTest.MyPage.repository.DiagnosisReportRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;
    @Value("${jwt.access.token.time}")
    private long JWT_ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refresh.token.time}")
    private long JWT_REFRESH_TOKEN_VALIDITY;
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    LRSAPIManager lrsapiManager;
    @Autowired
    private DiagnosisReportRepo diagnosisReportRepo;
    @Autowired
    private BookBookmarkRepository bookBookmarkRepository;
    @Autowired
    private VideoBookmarkRepository videoBookmarkRepository;
    @Transactional
    public CMRespDto<?> singUp(SignUpRequestDto signUpRequestDto) {
        if (!emailDuplicateCheck(signUpRequestDto.getEmail())
                && !nickNameDuplicateCheck(signUpRequestDto.getNickname())) {
            UserMaster userMaster = UserMaster.builder().nickname(signUpRequestDto.getNickname())
                    .email(signUpRequestDto.getEmail()).provider(signUpRequestDto.getProvider()).role(Role.USER)
                    .name(signUpRequestDto.getName()).userUuid(UUID.randomUUID().toString())
                    .providerId(signUpRequestDto.getProviderId()).birthday(signUpRequestDto.getBirthday())
                    .older_than_14(true).service_agreement(true).collect_info(true).build();
            userRepository.save(userMaster);

            PrincipalDetails principal = PrincipalDetails.create(userMaster);
            String jwtToken = jwtTokenUtil.generateAccessToken(principal);

            // 현재 시간을 LRS timestamp 포멧에 맞게 변환
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("KST"));
            String text = sdf.format(date);

            // StatementDto빌드
            StatementDTO statementDTO = StatementDTO.builder().userId(userMaster.getUserUuid()).actionType("register")
                    .sourceType("application").timestamp(text).build();

            // LRS에 저장
            List<StatementDTO> statementDTOList = new ArrayList<>();
            statementDTOList.add(statementDTO);
            try {
                lrsapiManager.saveStatementList(statementDTOList);
            } catch (ParseException e) {
                return new CMRespDto<>(500, "LRS 전송 실패", "실패");
            }

            return new CMRespDto<>(200, "회원가입 완료", jwtToken);
        }
        return new CMRespDto<>(201, "회원가입 불가능", "이메일이나 닉네임 중복 회원이 존재합니다.");
    }

    @Transactional(readOnly = true)
    public boolean emailDuplicateCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean nickNameDuplicateCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public String deleteUser(PrincipalDetails principalDetails) {
        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(principalDetails.getUserUuid());
        String userUuid = userMasterOptional.get().getUserUuid();
        List<DiagnosisReport> diagnosisReportList = diagnosisReportRepo.findAllByUserUuid(userUuid);
        List<VideoBookmark> videoBookmarkList = videoBookmarkRepository.findAllByUserUuid(userUuid);
        List<BookBookmark> bookBookmarkList = bookBookmarkRepository.findAllByUserUuid(userUuid);
        bookBookmarkRepository.deleteAll(bookBookmarkList);
        videoBookmarkRepository.deleteAll(videoBookmarkList);
        diagnosisReportRepo.deleteAll(diagnosisReportList);
        userRepository.delete(userMasterOptional.get());


        // 현재 시간을 LRS timestamp 포멧에 맞게 변환
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("KST"));
        String text = sdf.format(date);

        // StatementDto빌드
        StatementDTO statementDTO = StatementDTO.builder().userId(principalDetails.getUserUuid())
                .actionType("withdrawal").sourceType("application").timestamp(text).build();

        // LRS에 저장
        List<StatementDTO> statementDTOList = new ArrayList<>();
        statementDTOList.add(statementDTO);
        try {
            lrsapiManager.saveStatementList(statementDTOList);
        } catch (ParseException e) {
            logger.debug("LRS save error");
        }
        return "True";
    }

    @Transactional
    public CMRespDto<?> login(LoginRequestDto loginRequestDTO) {
        String providerId = loginRequestDTO.getProviderId();
        AuthProvider provider = AuthProvider.valueOf(loginRequestDTO.getProvider());
        Optional<UserMaster> userMasterOptional = userRepository.findByProviderIdAndProvider(providerId, provider);
        if (userMasterOptional.isPresent()) {
            UserMaster userMaster = userMasterOptional.get();
            PrincipalDetails principal = PrincipalDetails.create(userMaster);
            String jwtToken = jwtTokenUtil.generateAccessToken(principal);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userMaster.getEmail());
            userMaster.setRefreshToken(refreshToken);
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .provider(userMaster.getProvider().toString())
                    .jwtToken(jwtToken)
                    .email(userMaster.getEmail())
                    .birthday(userMaster.getBirthday().toString())
                    .providerId(userMaster.getProviderId())
                    .refreshToken(refreshToken)
                    .nickname(userMaster.getNickname())
                    .build();

            // 현재 시간을 LRS timestamp 포멧에 맞게 변환
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("KST"));
            String text = sdf.format(date);
            StatementDTO statementDTO = StatementDTO.builder().userId(userMaster.getUserUuid()).actionType("enter")
                    .sourceType("application").timestamp(text).build();
            List<StatementDTO> statementDTOList = new ArrayList<>();
            statementDTOList.add(statementDTO);
            try {
                lrsapiManager.saveStatementList(statementDTOList);
            } catch (ParseException e) {
                return new CMRespDto<>(500, "LRS 전송 실패", "실패");
            }
            return new CMRespDto<>(200, "jwt 반환", loginResponseDTO);
        }
        return new CMRespDto<>(201, "회원 가입이 안된 유저", null);
    }

    @Transactional
    public CMRespDto<?> logout(PrincipalDetails principalDetails) {
        Optional<UserMaster> userMasterOptional = userRepository.findByEmail(principalDetails.getEmail());
        userMasterOptional.get().setRefreshToken(null);

        // LRS에 로그아웃 정보 저장
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setUserId(principalDetails.getUserUuid());
        statementDTO.setActionType("quit");
        statementDTO.setSourceType("application");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("KST"));
        String text = sdf.format(date);
        statementDTO.setTimestamp(text);
        List<StatementDTO> statementDTOList = new ArrayList<>();
        statementDTOList.add(statementDTO);
        try {
            lrsapiManager.saveStatementList(statementDTOList);
        } catch (ParseException e) {
            return new CMRespDto<>(500, "LRS 전송 실패", "실패");
        }
        return new CMRespDto<>(200, "로그아웃 성공", text);
    }

    @Transactional
    public CMRespDto<?> modifyUserInfo(PrincipalDetails principalDetails, ModifyUserInfoDto modifyUserInfoDto) {
        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(principalDetails.getUserUuid());
        UserMaster userMaster = userMasterOptional.get();
        userMaster.setNickname(modifyUserInfoDto.getNickname());
        userMaster.setBirthday(modifyUserInfoDto.getBirthday());
        userMaster.setEmail(modifyUserInfoDto.getEmail());
        return new CMRespDto<>(200, "회원정보 수정 성공", "성공");
    };

}
