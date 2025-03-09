package cool.auv.authspringbootstarter.vm.request;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.Specification;

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
public class SysRoleRequest extends BaseEntity {

    private static final long serialVersionUID = 1L;

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

    public Specification<SysRole> buildSpecification() {
        return null;
    }

}
