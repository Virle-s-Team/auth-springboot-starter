package cool.auv.authspringbootstarter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnumProperties.class)
public class EnumAutoConfiguration {
}