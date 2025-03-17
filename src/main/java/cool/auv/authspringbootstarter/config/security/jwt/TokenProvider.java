package cool.auv.authspringbootstarter.config.security.jwt;

import com.google.common.io.BaseEncoding;
import cool.auv.authspringbootstarter.constant.AuthoritiesConstants;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.service.SysUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class TokenProvider {
    private static final int TIME_UNIT = 1000;

    private final long expiration;
    private final String jwtSecret;
    private final SecretKey secretKey;
    @Lazy
    @Autowired
    private SysUserService sysUserService;

    public TokenProvider(
            @Value("${spring.security.authentication.jwt.expiration:1800000}") long expiration,
            @Value("${spring.security.authentication.jwt.secret:Q2FzZQ5M2NvbXB1dGVyIQ5wcm9ncmFtbWluZw==}") String jwtSecret) {
        this.expiration = expiration <= 0 ? 2 * 60 * 60 * TIME_UNIT : expiration;
        this.jwtSecret = jwtSecret;
        this.secretKey = generateSecretKey(jwtSecret);
    }



    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";
    public boolean validateToken(String authToken, HttpServletRequest request) {
//        if (!redisService.hasKey(CommonConstant.PREFIX_USER_TOKEN + authToken)) {
//            return false;
//        }
        Claims claims = verifyJwt(authToken);
        if (Objects.nonNull(claims)) {
//            //延长token有效期
//            redisService.expire(CommonConstant.PREFIX_USER_TOKEN + authToken, JwtUtil.getExpiration() * 2 / 1000);
            return true;
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        // 解析token
        Claims claims = verifyJwt(token);
//        if (!redisService.hasKey(CommonConstant.PREFIX_USER_TOKEN + token) && Objects.isNull(claims)) {
//            throw new BadCredentialsException(INVALID_JWT_TOKEN);
//        }
        // 获取token类型
        long userId = ((Number) claims.get(AuthoritiesConstants.LOGIN_ACCOUNT_ID)).longValue();
        String username = (String) claims.get(AuthoritiesConstants.LOGIN_ACCOUNT_NAME);
        // 设置认证对象
        SysUser sysUser = sysUserService.loadUserWithAuthorities(userId);
        List<SimpleGrantedAuthority> authorities = sysUser.getAuthorities();
        return new UsernamePasswordAuthenticationToken(sysUser, token, authorities);

    }


    /**
     * Creates a JWT token with the specified claims and expiration time.
     */
    public String createJWT(Map<String, Object> claims) {
        Date now = new Date();
        String jti = UUID.randomUUID().toString(); // Unique token ID
        return Jwts.builder()
                .claims(claims)
                .id(jti)
                .issuedAt(now)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Verifies a JWT token and returns its claims.
     *
     * @throws IllegalArgumentException if the token is invalid or expired
     */
    public Claims verifyJwt(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.JwtException e) {
            log.warn("Failed to verify JWT: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }

    /**
     * Generates a SecretKey from the configured JWT secret.
     */
    private static SecretKey generateSecretKey(String jwtSecret) {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret cannot be null or empty");
        }

        byte[] keyBytes;
        try {
            keyBytes = BaseEncoding.base64().decode(jwtSecret);
            log.debug("Using Base64-decoded secret key, length: {}", keyBytes.length);
        } catch (IllegalArgumentException e) {
            log.debug("Secret is not Base64-encoded, treating as plain text: {}", jwtSecret);
            keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret is too short (" + keyBytes.length + " bytes), minimum 32 bytes required for HS256");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
