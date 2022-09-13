package com.peoples.admin.peoples_admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.SecurityUser;
import com.peoples.admin.peoples_admin.dto.response.SecurityUserResponse;
import com.peoples.admin.peoples_admin.exception.ErrorCode;
import com.peoples.admin.peoples_admin.exception.ErrorResponse;
import com.peoples.admin.peoples_admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final AdminService adminService;

    private static final String[] SWAGGER_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] WEB_RESOURCE_LIST = {
            "/css/**",
            "/js/**",
            "/image/**",
            "/font/**",
            "/vendor/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
     * 로그아웃 후 재 로그인 시 로그인 불가 방지
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.adminService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(SWAGGER_WHITE_LIST).permitAll()
                .antMatchers(WEB_RESOURCE_LIST).permitAll()
                .antMatchers("/","/login").permitAll()
                .antMatchers("/adminmgmt").hasRole("SUPERADMIN")
                .antMatchers("/dashboard","/usermgmt","/studymgmt","/log").hasAnyRole("SUPERADMIN","ADMIN")
                .antMatchers("/api/v1/**").hasAnyRole("SUPERADMIN","ADMIN")
                .anyRequest().permitAll()
            .and()
                .csrf()
            .and()
                .formLogin().loginPage("/login")
                .successHandler(new CustomLoginSuccessHandler(objectMapper, adminService))
                .failureHandler(new CustomLoginFailureHandler(objectMapper))
                .usernameParameter("userId")
                .passwordParameter("password")
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me-peoples-admin").permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?error=true&exception=1234")
                .sessionRegistry(this.sessionRegistry());

        http.rememberMe()
                .key("peoplesAdminRememberMeKey")
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("remember-me-peoplesAdmin")
                .tokenValiditySeconds(60 * 60 * 24) // 하루
                .alwaysRemember(true)
                .userDetailsService(adminService);

        return http.build();
    }

    /**
     * 로그인 성공 시 처리
     */
    @RequiredArgsConstructor
    @Slf4j
    static class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        private final ObjectMapper objectMapper;
        private final AdminService adminService;
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            Admin admin = ((SecurityUser)authentication.getPrincipal()).getAdmin();
            log.info("admin login : {}", admin.toString());
            adminService.updateLastLogin(admin.getUserId());
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().print(objectMapper.writeValueAsString(SecurityUserResponse.of(admin)));
            response.getWriter().flush();
        }
    }

    /**
     * 로그인 실패 시 처리
     */
    @RequiredArgsConstructor
    @Slf4j
    static class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        private final ObjectMapper objectMapper;
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            log.error("error : {}", exception);

            ErrorCode errorCode;
            if (exception instanceof UsernameNotFoundException) {
                // 사용자를 찾을 수 없을 경우
                errorCode = ErrorCode.USER_NOT_FOUND;
            } else if (exception instanceof BadCredentialsException) {
                // 비밀번호 틀렸을 경우(별도 처리 필요시 작성 - 현재는 사용자를 찾을 수 없습니다 - 처리)
                errorCode = ErrorCode.USER_NOT_FOUND;
            } else if (exception instanceof SessionAuthenticationException) {
                // 이미 로그인 되어있을 경우
                errorCode = ErrorCode.MAX_SESSION;
            } else {
                // 기타 오류
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
            }
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().print(objectMapper.writeValueAsString(ErrorResponse.toResponseEntity(errorCode)));
            response.getWriter().flush();
        }
    }
}
