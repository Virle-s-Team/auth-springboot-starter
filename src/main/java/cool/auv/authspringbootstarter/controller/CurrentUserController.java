package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.security.principal.SimpleUser;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.vm.MenuVM;
import cool.auv.codegeneratorjpa.core.exception.AppException;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/current-user")
public class CurrentUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取当前用户角色标识列表
     * 从 SimpleUser 直接获取，无需查询数据库
     */
    @GetMapping("/get-role")
    public ResponseEntity<Set<String>> getCurrentRole() {
        SimpleUser user = SecurityContextUtil.getCurrentUser()
                .orElseThrow(() -> new AppException("用户未登录"));
        return ResponseEntity.ok(user.getRoles());
    }

    /**
     * 获取当前用户权限标识列表
     * 从 SimpleUser 直接获取，无需查询数据库
     */
    @GetMapping("/permissions")
    public ResponseEntity<Set<String>> getPermission() {
        SimpleUser user = SecurityContextUtil.getCurrentUser()
                .orElseThrow(() -> new AppException("用户未登录"));
        return ResponseEntity.ok(user.getPermissions());
    }

    /**
     * 获取当前用户菜单树
     * 从所有权限中筛选出 renderMenu=true 的菜单，构建递归树状结构
     */
    @GetMapping("/menus")
    public ResponseEntity<Set<MenuVM>> getMenus() {
        Optional<Set<MenuVM>> menus = sysUserService.getCurrentUserMenu();
        return ResponseUtil.wrapOrNotFound(menus);
    }

    public record UpdatePasswordRequest(String oldPassword, String newPassword) {
    }

    @PutMapping("/update-password")
    public HttpEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) throws AppException {
        sysUserService.updatePassword(request.oldPassword, request.newPassword);
        return ResponseEntity.EMPTY;
    }
}
