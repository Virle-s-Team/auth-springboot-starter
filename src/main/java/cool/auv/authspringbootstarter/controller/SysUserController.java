package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{userId}/assign-permission{permissionId}")
    public ResponseEntity<Void> assignPermissionToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "permissionId") Long permissionId) {
        sysUserService.assignPermissionToRole(userId, permissionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/remove-permission/{permissionId}")
    public ResponseEntity<Void> removePermissionFromUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = "permissionId") Long permissionId) {
        sysUserService.removePermissionFromRole(userId, permissionId);
        return ResponseEntity.ok().build();
    }
}
