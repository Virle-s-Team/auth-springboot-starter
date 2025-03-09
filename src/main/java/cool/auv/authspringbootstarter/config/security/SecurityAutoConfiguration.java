package cool.auv.authspringbootstarter.config.security;

import cool.auv.authspringbootstarter.config.security.jwt.JWTFilter;
import cool.auv.authspringbootstarter.config.security.jwt.TokenProvider;
import cool.auv.authspringbootstarter.service.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@ConditionalOnClass(SecurityFilterChain.class)
public class SecurityAutoConfiguration {

    @Autowired
    private TokenProvider tokenProvider;

    @Bean
    @ConditionalOnMissingBean
    public SimpleUserService simpleUserService() {
        return new SimpleUserService();
    }

    private HttpSecurity configureCommon(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::permitAll);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        http = configureCommon(http);
        http
                .securityMatcher("/api/auth/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http, TokenProvider tokenProvider) throws Exception {
        http = configureCommon(http);
        http
                .addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
