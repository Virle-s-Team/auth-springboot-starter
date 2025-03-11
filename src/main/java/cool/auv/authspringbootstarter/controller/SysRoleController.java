package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sys-role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PutMapping("/{roleId}/assign-permission{permissionId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "roleId") Long permissionId) {
        sysRoleService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roleId}/remove-permission/{permissionId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "permissionId") Long permissionId) {
        sysRoleService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }
}
