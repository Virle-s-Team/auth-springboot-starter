package cool.auv.authspringbootstarter.entity;

import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

}
