package cool.auv.authspringbootstarter.entity;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "sys_permission")
@AutoEntity(basePath = "/api/v1/sys-permission", docTag = "权限管理")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父id
     */
    @OneToOne
    private SysPermission parent;

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


}
