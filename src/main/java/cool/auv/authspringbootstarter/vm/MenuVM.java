package cool.auv.authspringbootstarter.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 菜单视图模型
 * 专门用于前端路由和菜单渲染
 */
@Getter
@Setter
@Accessors(chain = true)
public class MenuVM {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单名称（路由 name）
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 徽标
     */
    private String badge;

    /**
     * 链接跳转
     */
    private String link;

    /**
     * 目标 (_self, _blank)
     */
    private String target;

    /**
     * 是否隐藏：true-隐藏 false-显示
     */
    private Boolean hidden;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 子菜单（递归结构）
     */
    private Set<MenuVM> children;

    /**
     * 元数据（用于存储额外的路由信息）
     */
    private Meta meta;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Meta {
        private String title;
        private String icon;
        private Boolean hidden;
        private Integer sortNo;

        public static Meta from(String title, String icon, Boolean hidden, Integer sortNo) {
            return new Meta()
                    .setTitle(title)
                    .setIcon(icon)
                    .setHidden(hidden)
                    .setSortNo(sortNo);
        }
    }
}
