package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.config.security.jwt.TokenProvider;
import cool.auv.authspringbootstarter.constant.AuthoritiesConstants;
import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.service.BaseSysUserServiceImpl;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.PasswordUtil;
import cool.auv.authspringbootstarter.vm.LoginVM;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserServiceImpl extends BaseSysUserServiceImpl implements SysUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TokenProvider tokenProvider;


    public String login(LoginVM loginVM) throws Exception {
        String username = loginVM.getUsername();
        String password = loginVM.getPassword();

        SysUser user = baseSysUserRepository.findOne(Example.of(new SysUser().setUsername(username))).orElseThrow(() -> new AuthenticationException("User not found: " + username));

        String genPwd = PasswordUtil.encrypt(username, password, user.getSalt().getBytes());
        String origin = user.getPassword();

        if (!origin.equals(genPwd)) {
            throw new AuthenticationException("用户名或密码错误");
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        //用户名
        claims.put(AuthoritiesConstants.LOGIN_ACCOUNT_NAME, user.getUsername());
        //用户id
        claims.put(AuthoritiesConstants.LOGIN_ACCOUNT_ID, user.getId());
        // 设置token缓存有效时间
        return tokenProvider.createJWT(claims);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        SysUser user = entityManager.getReference(SysUser.class, userId);
        SysRole role = entityManager.getReference(SysRole.class, roleId);
        user.getRoleSet().add(role);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        SysUser user = entityManager.getReference(SysUser.class, userId);
        SysRole role = entityManager.getReference(SysRole.class, roleId);
        user.getRoleSet().remove(role);
    }

    @Override
    public void assignPermissionToRole(Long userId, Long permissionId) {
        SysUser user = entityManager.getReference(SysUser.class, userId);
        SysPermission permission = entityManager.getReference(SysPermission.class, permissionId);
        user.getPermissionSet().remove(permission);
    }

    @Override
    public void removePermissionFromRole(Long userId, Long permissionId) {
        SysUser user = entityManager.getReference(SysUser.class, userId);
        SysPermission permission = entityManager.getReference(SysPermission.class, permissionId);
        user.getPermissionSet().remove(permission);
    }
}
