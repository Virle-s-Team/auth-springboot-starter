package cool.auv.authspringbootstarter.controller;

import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.vm.LoginVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginVM loginVM) throws Exception {
        String token = sysUserService.login(loginVM);
        return ResponseEntity.ok(token);
    }
}
