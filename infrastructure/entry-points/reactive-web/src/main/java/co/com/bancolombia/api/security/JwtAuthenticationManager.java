package co.com.bancolombia.api.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationManager(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        if (tokenProvider.validateToken(token)) {
            return Mono.just(new JwtAuthenticationToken(
                    tokenProvider.getUsernameFromToken(token),
                    tokenProvider.getAuthorities(token)
            ));
        }
        return Mono.empty();
    }
}
