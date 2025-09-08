package co.com.bancolombia.api.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

}
