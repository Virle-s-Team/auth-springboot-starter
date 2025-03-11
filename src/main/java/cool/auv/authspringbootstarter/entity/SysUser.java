package cool.auv.authspringbootstarter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.enums.ActiveStatusEnum;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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
@AutoEntity(basePath = "/api/v1/sys-user")
public class SysUser extends BaseEntity implements Serializable {

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
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

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
}
