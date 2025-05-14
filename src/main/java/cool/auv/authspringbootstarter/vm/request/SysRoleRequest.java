package cool.auv.authspringbootstarter.vm.request;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysRole_;
import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

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
public class SysRoleRequest extends BaseEntity implements RequestInterface<SysRole> {

    private static final long serialVersionUID = 1L;


    /**
     * 名称
     */
    private String name;


    @Override
    public Specification<SysRole> buildSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotEmpty(name)) {
                predicateList.add(criteriaBuilder.like(root.get(SysRole_.NAME), "%" + name + "%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
