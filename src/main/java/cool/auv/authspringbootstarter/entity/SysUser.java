package cool.auv.authspringbootstarter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.enums.ActiveStatusEnum;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.tenant.TenantBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author generator
 * @since 2024-03-09
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "sys_user")
@AutoEntity(
        basePath = "/api/v1/sys-user",
        docTag = "用户管理",
        enableMapperAnnotation = false
)
public class SysUser extends TenantBaseEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 登录账号
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码（BCrypt 哈希值）
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 激活状态
     */
    @Enumerated(EnumType.STRING)
    private ActiveStatusEnum status;

    @ManyToMany
    @JoinTable(
            name = "sys_user_role", // 中间表名
            joinColumns = @JoinColumn(name = "user_id"), // 当前实体外键
            inverseJoinColumns = @JoinColumn(name = "role_id") // 关联实体外键
    )
    private Set<SysRole> roleSet;

    @ManyToMany
    @JoinTable(
            name = "sys_user_permission", // 中间表名
            joinColumns = @JoinColumn(name = "user_id"), // 当前实体外键
            inverseJoinColumns = @JoinColumn(name = "permission_id") // 关联实体外键
    )
    private Set<SysPermission> permissionSet;

    /**
     * 获取用户的所有权限（包括直接权限和角色权限）
     *
     * @return 用户的所有有效权限
     */
    public Set<SysPermission> getAllPermission() {
        Set<SysPermission> rolePermission = roleSet.stream().flatMap(role -> role.getPermissionSet().stream()).collect(Collectors.toSet());
        Set<SysPermission> allPermission = new HashSet<>();
        allPermission.addAll(permissionSet);
        allPermission.addAll(rolePermission);
        return allPermission;
    }

    /**
     * 获取 Spring Security 需要的权限列表
     * 将权限标识转换为 SimpleGrantedAuthority 对象
     *
     * @return 权限列表，如果用户没有权限则返回空列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAllPermission().stream()
                .map(SysPermission::getPermission)
                .filter(permission -> permission != null && !permission.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     * 根据用户状态判断，ACTIVE 状态返回 true
     *
     * @return true 如果账户未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return status == ActiveStatusEnum.ACTIVE;
    }

    /**
     * 凭证（密码）是否未过期
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     * 根据用户状态判断，ACTIVE 状态返回 true
     *
     * @return true 如果账户启用
     */
    @Override
    public boolean isEnabled() {
        return status == ActiveStatusEnum.ACTIVE;
    }

}
