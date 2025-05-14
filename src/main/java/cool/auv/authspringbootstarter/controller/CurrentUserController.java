package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.mapstruct.BaseSysRoleMapstruct;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.vm.SysPermissionTreeVM;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.codegeneratorjpa.core.exception.AppException;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/current-user")
public class CurrentUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BaseSysRoleMapstruct baseSysRoleMapstruct;

    @GetMapping("/get-role")
    public ResponseEntity<Set<SysRoleVM>> getCurrentRole() {
        Optional<SysUser> currentUser = SecurityContextUtil.getCurrentUser();
        Optional<Set<SysRole>> sysRoles = currentUser.map(SysUser::getRoleSet);
        Optional<Set<SysRoleVM>> sysRoleVMS = sysRoles.map(roleSet -> roleSet.stream().map(sysRole -> {
            sysRole.setPermissionSet(new HashSet<>());
            return baseSysRoleMapstruct.entityToVm(sysRole);
        }).collect(Collectors.toSet()));
        return ResponseUtil.wrapOrNotFound(sysRoleVMS);
    }

    @GetMapping("/permissions")
    public ResponseEntity<Set<SysPermissionTreeVM>> getPermission() {
        Optional<Set<SysPermissionTreeVM>> currentUserPermission = sysUserService.getCurrentUserPermission();
        return ResponseUtil.wrapOrNotFound(currentUserPermission);
    }

    public record UpdatePasswordRequest(String oldPassword, String newPassword) {
    }

    @PutMapping("/update-password")
    public HttpEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) throws AppException {
        sysUserService.updatePassword(request.oldPassword, request.newPassword);
        return ResponseEntity.EMPTY;
    }
}
