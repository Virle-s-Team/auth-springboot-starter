package cool.auv.authspringbootstarter.config.security;

import cool.auv.authspringbootstarter.config.TenantIdFilter;
import cool.auv.authspringbootstarter.config.security.jwt.JWTFilter;
import cool.auv.authspringbootstarter.config.security.jwt.TokenProvider;
import cool.auv.authspringbootstarter.service.SimpleUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@ConditionalOnClass(SecurityFilterChain.class)
@ComponentScan(basePackages = "cool.auv.authspringbootstarter")
@EnableJpaRepositories(basePackages = "cool.auv.authspringbootstarter.repository")
@EntityScan(basePackages = "cool.auv.authspringbootstarter.entity")
public class SecurityAutoConfiguration {

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
                .addFilterAfter(new TenantIdFilter(), SecurityContextHolderFilter.class)
                .securityMatcher("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
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
