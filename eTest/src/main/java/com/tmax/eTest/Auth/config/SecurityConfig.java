// package com.tmax.eTest.Auth.config;

// import com.tmax.eTest.Auth.service.OAuth2DetailsService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @RequiredArgsConstructor
// @EnableWebSecurity //해당 파일로 security를 활성화 시킨다
// @Configuration //IOC

// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//     private final OAuth2DetailsService oAuth2DetailsService;

//     @Autowired
//     private CorsConfig corsConfig;

//     @Bean
//     public BCryptPasswordEncoder encode() {
//         return new BCryptPasswordEncoder();
//     }

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http
//                 .addFilter(corsConfig.corsFilter())
//                 .csrf().disable()
//                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//         http.authorizeRequests()
//                 .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**","/api/**").authenticated()
//                 .anyRequest().permitAll()
//                 .and()
//                 .formLogin().disable()
//                 .httpBasic().disable()
//                 .oauth2Login()//form 로그인도하고, oauth2도 한다.
//                 .userInfoEndpoint()//최종응답으로 회원정보를 바로 받는다.
//                 .userService(oAuth2DetailsService);
//     }
// }
