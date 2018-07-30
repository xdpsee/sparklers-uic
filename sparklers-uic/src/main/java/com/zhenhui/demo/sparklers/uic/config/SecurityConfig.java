package com.zhenhui.demo.sparklers.uic.config;

import com.zhenhui.demo.sparklers.uic.security.AccessDeniedHandlerImpl;
import com.zhenhui.demo.sparklers.uic.security.HTTP403ForbiddenEntryPoint;
import com.zhenhui.demo.sparklers.uic.security.JsonWebTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonWebTokenAuthenticationFilter authenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .authenticationEntryPoint(new HTTP403ForbiddenEntryPoint())
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth/token", "auth/token/captcha", "/captcha", "/auth/weibo", "/auth/weibo/bind")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/user")
                .permitAll()
            .anyRequest()
                .authenticated();

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().cacheControl();
    }
}
