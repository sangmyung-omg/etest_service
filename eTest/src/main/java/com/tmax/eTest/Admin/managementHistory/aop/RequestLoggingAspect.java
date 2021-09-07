package com.tmax.eTest.Admin.managementHistory.aop;

import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import com.tmax.eTest.Admin.managementHistory.service.ManagementHistoryService;
import com.tmax.eTest.Auth.controller.UserController;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
public class RequestLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);
    private final ManagementHistoryService managementHistoryService;
    private final UserController userController;
    private final UserRepository userRepository;

    public RequestLoggingAspect(ManagementHistoryService managementHistoryService, UserController userController, UserRepository userRepository) {
        this.managementHistoryService = managementHistoryService;
        this.userController = userController;
        this.userRepository = userRepository;
    }

    @Pointcut("within(com.tmax.eTest.Admin.dashboard.controller..*)") // 3
    public void onRequest() {}

    @Around("com.tmax.eTest.Admin.managementHistory.aop.RequestLoggingAspect.onRequest()") // 4
    public Object requestLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        long start = System.currentTimeMillis();
        Timestamp currentDateTime = new Timestamp(start);
        ManagementHistory managementHistory = ManagementHistory.builder()
                .adminName(principalDetails.getName())
                .adminId(principalDetails.getEmail())
                .ip(request.getRemoteAddr())
                .taskDate(currentDateTime)
                .build();
        try {
            RequestMapper requestMapper = managementHistoryService.getRequestMapper(
                    request.getMethod(),
                    proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                    proceedingJoinPoint.getSignature().getName()
            );
            managementHistory.setMenu(requestMapper.getMenu());
            managementHistory.setDetail(requestMapper.getContent());
        } catch (java.lang.NullPointerException e) {
            logger.info("RequestMapper for ManagementHistory not found.\nRequest: {}\nController: {}\nMethod: {}",
                    request.getMethod(),
                    proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                    proceedingJoinPoint.getSignature().getName()
            );
            managementHistory.setMenu(proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName());
            managementHistory.setDetail(proceedingJoinPoint.getSignature().getName());
        } finally {
            managementHistoryService.createManagementHistory(managementHistory);
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
    }

    private String paramMapToString(Map<String, String[]> paraStringMap) {
        return paraStringMap.entrySet().stream()
                .map(entry -> String.format("%s : %s",
                        entry.getKey(), Arrays.toString(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}