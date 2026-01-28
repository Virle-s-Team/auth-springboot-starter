package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.authspringbootstarter.vm.request.SysRoleRequest;
import cool.auv.codegeneratorjpa.core.base.BaseAutoService;

public interface SysRoleService extends BaseAutoService<SysRole, Long, SysRoleRequest, SysRoleVM> {
    void assignPermissionToRole(Long roleId, Long permissionId);

    void removePermissionFromRole(Long roleId, Long permissionId);
}
