package cool.auv.authspringbootstarter.security.principal;

import cool.auv.authspringbootstarter.entity.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证用户对象 - 轻量级，不依赖 JPA
 * 在 Token 验证时一次性加载所有数据，后续无数据库查询
 */
@Getter
public class SimpleUser implements UserDetails {

    // 基本信息
    private final Long userId;
    private final String username;
    private final String realName;
    private final String avatar;
    private final String tenantId;

    // 权限标识集合（预聚合，避免级联访问导致的 LazyInitializationException）
    private final Set<String> permissions;

    // 角色标识集合
    private final Set<String> roles;

    /**
     * 私有构造函数，使用静态工厂方法创建
     */
    private SimpleUser(Long userId, String username, String realName, String avatar,
                       String tenantId, Set<String> permissions, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.avatar = avatar;
        this.tenantId = tenantId;
        this.permissions = permissions != null ? permissions : new HashSet<>();
        this.roles = roles != null ? roles : new HashSet<>();
    }

    /**
     * 从 SysUser 构建 SimpleUser（必须在事务内调用）
     * 此时会预加载所有权限和角色信息，后续访问不再依赖 JPA Session
     *
     * @param sysUser 已加载权限的 SysUser 实体
     * @return SimpleUser 对象
     */
    public static SimpleUser fromSysUser(SysUser sysUser) {
        // 预聚合所有权限标识
        Set<String> allPermissions = new HashSet<>();

        // 用户直接权限
        sysUser.getPermissionSet().forEach(p -> {
            if (p.getPermission() != null) {
                allPermissions.add(p.getPermission());
            }
        });

        // 角色和角色权限
        Set<String> roles = new HashSet<>();
        sysUser.getRoleSet().forEach(role -> {
            if (role.getCode() != null) {
                roles.add(role.getCode());
            }
            // 在事务内访问 role.getPermissionSet()
            role.getPermissionSet().forEach(p -> {
                if (p.getPermission() != null) {
                    allPermissions.add(p.getPermission());
                }
            });
        });

        return new SimpleUser(
                sysUser.getId(),
                sysUser.getUsername(),
                sysUser.getRealName(),
                sysUser.getAvatar(),
                sysUser.getTenantId(),
                allPermissions,
                roles
        );
    }

    /**
     * 检查是否有指定权限
     *
     * @param permission 权限标识
     * @return true 如果有该权限
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    /**
     * 检查是否有指定角色
     *
     * @param roleCode 角色编码
     * @return true 如果有该角色
     */
    public boolean hasRole(String roleCode) {
        return roles.contains(roleCode);
    }

    /**
     * 检查是否有任一权限
     *
     * @param perms 权限标识数组
     * @return true 如果有任一权限
     */
    public boolean hasAnyPermission(String... perms) {
        return Arrays.stream(perms).anyMatch(permissions::contains);
    }

    /**
     * 检查是否有所有权限
     *
     * @param perms 权限标识数组
     * @return true 如果拥有所有权限
     */
    public boolean hasAllPermissions(String... perms) {
        return Arrays.stream(perms).allMatch(permissions::contains);
    }

    // ========== UserDetails 接口实现 ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null; // 认证成功后不需要密码
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
