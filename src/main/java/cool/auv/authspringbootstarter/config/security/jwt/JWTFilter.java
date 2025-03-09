package cool.auv.authspringbootstarter.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends OncePerRequestFilter {


    public final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {

        this.tokenProvider = tokenProvider;
    }

    private void tokenCheckLogic(HttpServletRequest httpServletRequest, String token) {
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token, httpServletRequest)) {
            Authentication adminAuthentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
        }
    }


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        Optional<String> tokenOpt = Optional.empty();
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            tokenOpt = Optional.of(bearerToken.substring(7));
        }
        String token = tokenOpt.orElseThrow(() -> new RuntimeException("Token is not present"));
        tokenCheckLogic(request, token);
        filterChain.doFilter(request, response);
    }
}
