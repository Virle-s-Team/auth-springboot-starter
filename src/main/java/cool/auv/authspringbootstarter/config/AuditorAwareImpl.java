package cool.auv.authspringbootstarter.config;

import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityContextUtil.getCurrentUser()
                .map(user -> user.getUsername());
    }
}
