package com.tmax.eTest.Auth.config;

import com.tmax.eTest.Auth.jwt.JwtCommonAuthorizationFilter;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.PrincipalDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;
    @Autowired
    private CorsConfig corsConfig;
    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
<<<<<<< HEAD
    http
        .addFilter(corsConfig.corsFilter())
        .csrf().disable() //csrf 토큰
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
        .formLogin().disable()
        .httpBasic().disable()
        .addFilter(new JwtCommonAuthorizationFilter(authenticationManager(),userRepository))
        .authorizeRequests()
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
=======
        http.csrf().disable(); // csrf 토큰
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();
        http.addFilter(new JwtCommonAuthorizationFilter(authenticationManager(), userRepository));

        http.authorizeRequests()

                .antMatchers("/user/**").access("hasRole('ROLE_USER')").antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')").anyRequest().permitAll();
>>>>>>> [fix] merge ae1-2/dev
    }
}
