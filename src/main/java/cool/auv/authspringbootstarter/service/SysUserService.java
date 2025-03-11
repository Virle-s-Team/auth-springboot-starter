package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.vm.LoginVM;

public interface SysUserService extends BaseSysUserService{

    String login(LoginVM loginVM) throws Exception;

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

    void assignPermissionToRole(Long userId, Long permissionId);

    void removePermissionFromRole(Long userId, Long permissionId);

}
