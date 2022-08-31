package com.peoples.admin.peoples_admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peoples.admin.peoples_admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserService userService;

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
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(SWAGGER_WHITE_LIST).permitAll()
                .antMatchers(WEB_RESOURCE_LIST).permitAll()
                .antMatchers("/","/login").permitAll()
                .anyRequest().authenticated()
            .and()
                .csrf()
            .and()
                .formLogin().loginPage("/login")
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
                .key("oncueAdmin2RememberMeKey")
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("remember-me-admin2")
                .tokenValiditySeconds(60 * 60 * 24) // 하루
                .alwaysRemember(true)
                .userDetailsService(userService);

        return http.build();
    }
}
