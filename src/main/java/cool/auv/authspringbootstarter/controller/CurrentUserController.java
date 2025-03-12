package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/current-user")
public class CurrentUserController {


    @GetMapping("/get-role")
    public ResponseEntity<Set<SysRole>> getCurrentRole() {
        Optional<SysUser> currentUser = SecurityContextUtil.getCurrentUser();
        Optional<Set<SysRole>> sysRoles = currentUser.map(SysUser::getRoleSet);
        return ResponseUtil.wrapOrNotFound(sysRoles);
    }
}
