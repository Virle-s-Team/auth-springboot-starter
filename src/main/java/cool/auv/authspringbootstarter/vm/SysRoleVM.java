package cool.auv.authspringbootstarter.vm;

import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
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
public class SysRoleVM extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    private String code;
    /**
     * 备注
     */
    private String remark;

    private Boolean isSuperAdmin;

    private Set<SysPermissionVM> permissionSet;


}
