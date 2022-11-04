package com.study.club.security.filter;

import com.study.club.security.dto.ClubAuthMemberDTO;
import com.study.club.security.util.JWTUtil;
import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("================================================ apiloginfilter=============");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, pw);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("================================== API LOGIN FILTER");
        log.info("successfulAuthentication : " + authResult);
        log.info("===================================================");

        log.info(authResult.getPrincipal());

        String email = ((ClubAuthMemberDTO) authResult.getPrincipal()).getUsername();
        String token = null;
        try {
            token = jwtUtil.generateToken(email);
            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
