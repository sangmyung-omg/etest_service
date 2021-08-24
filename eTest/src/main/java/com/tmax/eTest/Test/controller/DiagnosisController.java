package com.tmax.eTest.Test.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Test.service.ProblemService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DiagnosisController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	ProblemService problemService;
	
	@GetMapping(value = "/diagnosis/tendency", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisTendencyProblems() throws Exception {

		try{
			Map<String, Object> res = problemService.getDiagnosisTendencyProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (Exception E){
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/diagnosis/knowledge", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisKnowledgeProblems() throws Exception {
		try{
			Map<String, Object> res = problemService.getDiagnosisKnowledgeProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (Exception E){
			Map<String, Object> ret = new HashMap<String, Object>();
			logger.info(E.getMessage());
			E.printStackTrace();
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* 8/13 auth 쪽 백에서 접속한 시간 정보 포함하는 jwt 토큰 미리 발행. (세션 개념)
	*  그 토큰으로 홈페이지 쪽에서 etest 쪽으로 토큰(현재 로그인 중인 사람 정보)과 함께 진입.
	*  etest 프론트에서 해당 토큰과 함께 백으로 자가진단 요청하면, 이쪽 백에서 Authentication 동작 : jwt를 decode해서 그 안의 email 정보로 식별해 USER_MASTER 로부터 유저 정보들 다 조회해옴.
	*  이미 instance에 가지고 있는 유저 정보는 원래 정보 그대로 PrincipalDetails 라는 DTO(?)에 넣어서 들고 있음.
	*  우리는 그 PrincipalDetails에 담겨있는 유저 아이디만 꺼내서 사용하면 됨.
	*
	*  결론 : 프론트에서는 따로 유저 아이디를 위해 jwt 만들 필요 없이 홈페이지에서 발급된 jwt 토큰만 넘겨주면 됨. 백에서는 아래와 같이 받아서 유저 아이디 꺼내서 사용하면 됨.
	*  검증은 추후 프론트 붙이면서 진행
	*/
	@GetMapping(value = "/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getMinitestProblems(@RequestHeader("token") String token,
													  @AuthenticationPrincipal PrincipalDetails principalDetails,			
													 @RequestParam String userId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("User UUID from token : " + principalDetails.getUserUuid());
		// try {
		// 	userId = getUserId(token);
		// } catch (Exception e) {
		// 	logger.info("Error decoding JWT token : " + e.getMessage());
		// }

		// search minitest with setnum
		Map<String, Object> re = problemService.getMinitestProblems(userId);
		if (re.containsKey("error")) {
			map.put("resultMessage", re.get("error"));
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			map.put("minitestProblems", re.get("minitestProblems"));
		}
		
		map.put("resultMessage", "Successfully returned");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	private String getUserId(String token) {
		final String USER_ID_FIELD = "userId";
		final String SUB_FIELD = "sub";
		
		DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        }
        catch(JWTDecodeException e){
            throw new JWTDecodeException(e.getLocalizedMessage());
        }

		// //If not verified. throw
        // if(doVerify){
        //     JWTVerifyCode resultCode = verifyToken(jwt);

        //     if(resultCode != JWTVerifyCode.SUCCESS)
        //         throw new JWTInvalidException("Token verification failed. Check token validity. " + resultCode.getMessage());
        // }

		//Try user id field => "userID" -> "sub"
        Claim userIDClaim = jwt.getClaim(USER_ID_FIELD);
        if(!userIDClaim.isNull())
            return userIDClaim.asString();

        Claim subClaim = jwt.getClaim(SUB_FIELD);
        if(!subClaim.isNull())
            return subClaim.asString();

        //Else? => none of the userID is found
        throw new JwtException(String.format("No user ID field found in body. Nor %s or %s", USER_ID_FIELD, SUB_FIELD));
	}
}
