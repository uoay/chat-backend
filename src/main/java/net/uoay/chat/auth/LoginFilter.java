package net.uoay.chat.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.uoay.chat.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod()
            );
        }

        if (!request.getContentType().equals("application/json")) {
            throw new AuthenticationServiceException("Illegal content type");
        }

        LoginRequest loginRequest;
        try {
            var stream = request.getInputStream();
            loginRequest = new ObjectMapper().readValue(stream, LoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Illegal request body");
        }

        var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(),
            loginRequest.password()
        );

        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

}
