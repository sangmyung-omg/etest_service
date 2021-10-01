package com.tmax.eTest.Admin.managementHistory.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import com.tmax.eTest.Admin.managementHistory.service.ManagementHistoryService;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Component
public class ManagementHistoryFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ManagementHistoryFilter.class);
    private final ObjectMapper mapper;
    private final ManagementHistoryService managementHistoryService;

    public ManagementHistoryFilter(ObjectMapper mapper, ManagementHistoryService managementHistoryService) {
        this.mapper = mapper;
        this.managementHistoryService = managementHistoryService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappingRequest, wrappingResponse);

        Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        Authentication authentication;
        String adminName = null;
        String adminId = null;
        String postMethod = wrappingRequest.getMethod();
        String requestUrl = wrappingRequest.getRequestURI().substring(request.getContextPath().length());
        String parameter = "0";
        String reason = null;

        try {
            if (requestUrl.equals("/login")) {
                JsonNode responseJson = mapper.readTree(wrappingResponse.getContentAsByteArray());
                parameter = responseJson.get("code").toString();
                if (parameter.equals("203")) {
                    adminName = responseJson.get("data").get("name").toString();
                    adminName = adminName.substring(1, adminName.length() - 1);
                    adminId = responseJson.get("data").get("email").toString();
                    adminId = adminId.substring(1, adminId.length() - 1);
                }
            } else if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
                if (requestUrl.equals("/master/history/download")) {
                    JsonNode responseJson = mapper.readTree(wrappingResponse.getContentAsByteArray());
                    reason = responseJson.get("reason").toString();
                    reason = reason.substring(1, reason.length() - 1);
                }
                authentication = SecurityContextHolder.getContext().getAuthentication();
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                adminName = principalDetails.getName();
                adminId = principalDetails.getEmail();
            }
            RequestMapper requestMapper = managementHistoryService.getRequestMapper(postMethod, requestUrl, parameter);
            ManagementHistory managementHistory = ManagementHistory.builder()
                    .adminName(adminName)
                    .adminId(adminId)
                    .ip(wrappingRequest.getRemoteAddr())
                    .taskDate(currentDateTime)
                    .link(requestUrl)
                    .menu(requestMapper.getMenu())
                    .detail(requestMapper.getContent())
                    .build();
            if (requestUrl.equals("/master/history/download")) {
                managementHistory.setReason(reason);
            }
            managementHistoryService.createManagementHistory(managementHistory);
            logger.debug("Management history inserted.\n" +
                            "============================================================" +
                            "\npost: {}\nurl: {}\nparameter: {}\nadmin_name: {}\n" +
                            "============================================================",
                    postMethod, requestUrl, parameter, adminName);
        } catch (NullPointerException e) {
            logger.debug("Request mapper not found.\n" +
                            "============================================================" +
                            "\npost: {}\nurl: {}\nparameter: {}\nadmin_name: {}\n" +
                            "============================================================",
                    postMethod, requestUrl, parameter, adminName);
        } finally {
            wrappingResponse.copyBodyToResponse();
        }
    }
}
