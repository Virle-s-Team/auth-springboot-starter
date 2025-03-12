package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/sys-user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PutMapping("/{userId}/assign-role{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        sysUserService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/remove-role/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "roleId") Long roleId) {
        sysUserService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/assign-permission/{permissionId}")
    public ResponseEntity<Void> assignPermissionToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "permissionId") Long permissionId) {
        sysUserService.assignPermissionToRole(userId, permissionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/remove-permission/{permissionId}")
    public ResponseEntity<Void> removePermissionFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "permissionId") Long permissionId) {
        sysUserService.removePermissionFromRole(userId, permissionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/get-role")
    public ResponseEntity<Set<SysRoleVM>> getRole(@PathVariable(name = "userId") Long userId) {
        Optional<Set<SysRoleVM>> roleSet = sysUserService.getRole(userId);
        return ResponseUtil.wrapOrNotFound(roleSet);
    }
}
