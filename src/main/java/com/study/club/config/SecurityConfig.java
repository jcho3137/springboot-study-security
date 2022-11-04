package com.study.club.config;

import com.study.club.security.filter.ApiCheckFilter;
import com.study.club.security.filter.ApiLoginFilter;
import com.study.club.security.handler.ApiLoginFailHandler;
import com.study.club.security.handler.ClubLoginSuccessHandler;
import com.study.club.security.service.ClubUserDetailsService;
import com.study.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user1")
                .password("$2a$10$CJwJ4yskegGISiP2cj6WJeqGuuKQB17IASiFqB4E1jxr9FOLNzN96")
                .roles("USER");
    }
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();
        http.csrf().disable();
        http.logout();

        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    };

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
        return apiLoginFilter;
    }

    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }
}
