package cool.auv.authspringbootstarter.service;

public interface SysRoleService extends BaseSysRoleService {
    void assignPermissionToRole(Long roleId, Long permissionId);

    void removePermissionFromRole(Long roleId, Long permissionId);
}
