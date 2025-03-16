package cool.auv.authspringbootstarter.entity;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author generator
 * @since 2024-03-30
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "sys_role")
@AutoEntity(basePath = "/api/v1/sys-role", docTag = "角色管理")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    private Boolean isSuperAdmin;

    @ManyToMany
    @JoinTable(
            name = "sys_role_permission", // 中间表名
            joinColumns = @JoinColumn(name = "role_id"), // 当前实体外键
            inverseJoinColumns = @JoinColumn(name = "permission_id") // 关联实体外键
    )
    private Set<SysPermission> permissionSet;

}
