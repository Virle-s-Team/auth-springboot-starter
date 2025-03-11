package cool.auv.authspringbootstarter.vm.request;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;


@Getter
@Setter
@Accessors(chain = true)
public class SysPermissionRequest implements Serializable, RequestInterface<SysPermission> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父id
     */
    private Long parent;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 组件名称
     */
    private String name;

    /**
     * _self,_blank
     */
    private String target;

    /**
     * 路径
     */
    private String path;

    /**
     * 菜单权限编码
     */
    private String permission;

    /**
     * 徽标
     */
    private String badge;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 链接跳转
     */
    private String link;

    /**
     * 组件
     */
    private String component;

    /**
     * 是否展示：0否1是
     */
    private Boolean renderMenu;

    /**
     * 菜单排序
     */
    private Integer sortNo;

    /**
     * 描述
     */
    private String description;

    /**
     * 按钮权限状态
     */
    private Boolean status;

    /**
     * 菜单图标
     */
    private String icon;


    @Override
    public Specification<SysPermission> buildSpecification() {
        return null;
    }
}
