package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.service.SysPermissionService;
import cool.auv.authspringbootstarter.vm.SysPermissionVM;
import cool.auv.authspringbootstarter.vm.request.SysPermissionRequest;
import cool.auv.codegeneratorjpa.core.base.AbstractCrudController;
import cool.auv.codegeneratorjpa.core.base.CustomControllerMarker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/sys-permission")
@Tag(
        name = "权限管理"
)
public class SysPermissionController extends AbstractCrudController<SysPermission, Long, SysPermissionRequest, SysPermissionVM>
        implements CustomControllerMarker<SysPermission> {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 获取完整的权限树结构
     * 用于前端权限管理界面，支持树状选择器
     *
     * @return 权限树的根节点集合
     */
    @GetMapping("/tree")
    @Operation(
            summary = "获取权限树",
            description = "返回完整的权限树结构，包含父子关系，用于前端权限管理"
    )
    public ResponseEntity<Set<SysPermissionVM>> getPermissionTree() {
        Set<SysPermissionVM> tree = sysPermissionService.getPermissionTree();
        return ResponseEntity.ok(tree);
    }
}
