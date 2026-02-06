package cool.auv.authspringbootstarter.utils;

import cool.auv.authspringbootstarter.security.principal.SimpleUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextUtil {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录的 SimpleUser 对象
     */
    public static Optional<SimpleUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }

        if (principal instanceof SimpleUser) {
            return Optional.of((SimpleUser) principal);
        }

        return Optional.empty();
    }

    /**
     * 获取当前用户 ID
     *
     * @return 用户 ID
     * @throws RuntimeException 如果用户未登录
     */
    public static Long getCurrentUserId() {
        return getCurrentUser()
                .map(SimpleUser::getUserId)
                .orElseThrow(() -> new RuntimeException("用户未登录"));
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     * @throws RuntimeException 如果用户未登录
     */
    public static String getCurrentUsername() {
        return getCurrentUser()
                .map(SimpleUser::getUsername)
                .orElseThrow(() -> new RuntimeException("用户未登录"));
    }

    /**
     * 获取当前租户 ID
     *
     * @return 租户 ID
     * @throws RuntimeException 如果用户未登录
     */
    public static String getCurrentTenantId() {
        return getCurrentUser()
                .map(SimpleUser::getTenantId)
                .orElseThrow(() -> new RuntimeException("用户未登录"));
    }

}
