package cool.auv.authspringbootstarter.utils;

import cool.auv.authspringbootstarter.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextUtil {

    public static Optional<SysUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }

        if (principal instanceof SysUser) {
            return Optional.of((SysUser) principal);
        }

        return Optional.empty();
    }

}
