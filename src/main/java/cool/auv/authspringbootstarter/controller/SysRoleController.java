package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.service.SysRoleService;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.authspringbootstarter.vm.request.SysRoleRequest;
import cool.auv.codegeneratorjpa.core.base.AbstractCrudController;
import cool.auv.codegeneratorjpa.core.base.CustomControllerMarker;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sys-role")
@Tag(
        name = "角色管理"
)
public class SysRoleController extends AbstractCrudController<SysRole, Long, SysRoleRequest, SysRoleVM> implements CustomControllerMarker<SysRole> {

    @Autowired
    private SysRoleService sysRoleService;

    @PutMapping("/{roleId}/assign-permission/{permissionId}")
    public ResponseEntity<Void> assignPermissionToRole(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "permissionId") Long permissionId) {
        sysRoleService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roleId}/remove-permission/{permissionId}")
    public ResponseEntity<Void> removePermissionFromRole(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "permissionId") Long permissionId) {
        sysRoleService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }
}
