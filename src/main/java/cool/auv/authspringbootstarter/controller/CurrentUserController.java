package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.vm.SysPermissionTreeVM;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/current-user")
public class CurrentUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/get-role")
    public ResponseEntity<Set<SysRole>> getCurrentRole() {
        Optional<SysUser> currentUser = SecurityContextUtil.getCurrentUser();
        Optional<Set<SysRole>> sysRoles = currentUser.map(SysUser::getRoleSet);
        return ResponseUtil.wrapOrNotFound(sysRoles);
    }

    @GetMapping("/permissions")
    public ResponseEntity<Set<SysPermissionTreeVM>> getPermission() {
        Optional<Set<SysPermissionTreeVM>> currentUserPermission = sysUserService.getCurrentUserPermission();
        return ResponseUtil.wrapOrNotFound(currentUserPermission);
    }
}
