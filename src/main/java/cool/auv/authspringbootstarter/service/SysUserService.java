package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.vm.LoginVM;
import cool.auv.authspringbootstarter.vm.SysPermissionTreeVM;
import cool.auv.authspringbootstarter.vm.SysRoleVM;

import java.util.Optional;
import java.util.Set;

public interface SysUserService extends BaseSysUserService{

    String login(LoginVM loginVM) throws Exception;

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

    void assignPermissionToRole(Long userId, Long permissionId);

    void removePermissionFromRole(Long userId, Long permissionId);

    Optional<Set<SysRoleVM>> getRole(Long userId);

    SysUser loadUserWithAuthorities(Long userId);

    Optional<Set<SysPermissionTreeVM>> getCurrentUserPermission();
}
