package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.vm.SysPermissionVM;
import cool.auv.authspringbootstarter.vm.request.SysPermissionRequest;
import cool.auv.codegeneratorjpa.core.base.BaseAutoService;

import java.util.Set;

public interface SysPermissionService extends BaseAutoService<SysPermission, Long, SysPermissionRequest, SysPermissionVM> {
    /**
     * 获取完整的权限树结构
     *
     * @return 权限树的根节点集合
     */
    Set<SysPermissionVM> getPermissionTree();
}
