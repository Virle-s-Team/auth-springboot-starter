package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.vm.*;
import cool.auv.authspringbootstarter.vm.request.SysUserRequest;
import cool.auv.codegeneratorjpa.core.base.BaseAutoService;
import cool.auv.codegeneratorjpa.core.exception.AppException;

import java.util.Optional;
import java.util.Set;

public interface SysUserService extends BaseAutoService<SysUser, Long, SysUserRequest, SysUserVM> {

    String login(LoginVM loginVM) throws Exception;

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

    void assignPermissionToRole(Long userId, Long permissionId);

    void removePermissionFromRole(Long userId, Long permissionId);

    Optional<Set<SysRoleVM>> getRole(Long userId);

    SysUser loadUserWithAuthorities(Long userId);

    Optional<Set<SysPermissionTreeVM>> getCurrentUserPermission();

    void save(SysUserUpdateVM sysUserVM) throws AppException;

    void update(SysUserUpdateVM sysUserVM) throws AppException;

    void resetPassword(Long userId);

    void updatePassword(String oldPassword, String newPassword) throws AppException;
}
