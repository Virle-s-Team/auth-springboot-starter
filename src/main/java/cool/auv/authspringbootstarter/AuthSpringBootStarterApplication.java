package cool.auv.authspringbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuthSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSpringBootStarterApplication.class, args);
    }

}
