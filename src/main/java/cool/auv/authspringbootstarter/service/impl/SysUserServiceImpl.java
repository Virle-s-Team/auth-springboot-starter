package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.config.security.jwt.TokenProvider;
import cool.auv.authspringbootstarter.constant.AuthoritiesConstants;
import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.mapstruct.BaseSysRoleMapstruct;
import cool.auv.authspringbootstarter.mapstruct.SysUserUpdateVMMapstruct;
import cool.auv.authspringbootstarter.service.BaseSysUserServiceImpl;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.PasswordUtil;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.vm.LoginVM;
import cool.auv.authspringbootstarter.vm.SysPermissionTreeVM;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.authspringbootstarter.vm.SysUserUpdateVM;
import cool.auv.codegeneratorjpa.core.exception.AppException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends BaseSysUserServiceImpl implements SysUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private BaseSysRoleMapstruct baseSysRoleMapstruct;

    @Autowired
    private SysUserUpdateVMMapstruct sysUserUpdateVMMapstruct;

    @Value("${app.reset-password}")
    private String resetPassword = "123456";

    public String login(LoginVM loginVM) throws Exception {
        String username = loginVM.getUsername();
        String password = loginVM.getPassword();

        SysUser user = baseSysUserRepository.findOne(Example.of(new SysUser().setUsername(username))).orElseThrow(() -> new AuthenticationException("User not found: " + username));

        String genPwd = PasswordUtil.encrypt(password, user.getSecretKey(), user.getSalt(), user.getIv());
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

    @Override
    public Optional<Set<SysRoleVM>> getRole(Long userId) {
        return baseSysUserRepository.findById(userId).map(user -> user.getRoleSet().stream().map(baseSysRoleMapstruct::entityToVm).collect(Collectors.toSet()));
    }

    @Override
    @Transactional(readOnly = true)
    public SysUser loadUserWithAuthorities(Long userId) {
        SysUser sysUser = baseSysUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        sysUser.getAuthorities(); // 触发懒加载
        return sysUser;
    }

    @Override
    public Optional<Set<SysPermissionTreeVM>> getCurrentUserPermission() {
        // 获取用户的所有权限
        Optional<SysUser> currentUser = SecurityContextUtil.getCurrentUser();
        // 构建树状结构
        return currentUser.map(sysUser -> {

            Set<SysPermission> allPermission = sysUser.getAllPermission();
            // 过滤出顶级权限
            Set<SysPermission> topPermission = allPermission.stream().filter(permission -> permission.getParent() == null).collect(Collectors.toSet());

            return topPermission.stream().map(permission -> {
                // 根据当前的permission获取子permission
                Set<SysPermission> children = allPermission.stream().filter(item -> item.getParent() != null && permission.getId().equals(item.getParent().getId())).collect(Collectors.toSet());
                // 构建vm
                return new SysPermissionTreeVM().setPermission(permission).setChildren(children);
            }).collect(Collectors.toSet());

        });
    }


    @Override
    @Transactional
    public void save(SysUserUpdateVM sysUserVM) throws AppException {
        SysUser sysUser = sysUserUpdateVMMapstruct.vmToEntity(sysUserVM);
        if (StringUtils.isNotEmpty(sysUser.getPassword())) {
            try {
                String salt = PasswordUtil.generateSalt();
                String key = PasswordUtil.generateKey();
                String iv = PasswordUtil.generateIV();

                String encrypt = PasswordUtil.encrypt(sysUser.getPassword(), key, salt, iv);
                sysUser.setSalt(salt);
                sysUser.setSecretKey(key);
                sysUser.setPassword(encrypt);
                sysUser.setIv(iv);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        baseSysUserRepository.save(sysUser);
    }

    @Override
    @Transactional
    public void update(SysUserUpdateVM sysUserVM) throws AppException {
        baseSysUserRepository.findById(sysUserVM.getId()).ifPresent(sysUser -> {
            sysUserUpdateVMMapstruct.updateEntityFromVM(sysUserVM, sysUser);
            if (StringUtils.isNotEmpty(sysUser.getPassword())) {
                try {
                    String salt = sysUser.getSalt();
                    String key = sysUser.getSecretKey();
                    String iv = sysUser.getIv();

                    String encrypt = PasswordUtil.encrypt(sysUserVM.getPassword(), key, salt, iv);
                    sysUser.setPassword(encrypt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    @Transactional
    public void resetPassword(Long userId) {
        baseSysUserRepository.findById(userId).ifPresent(sysUser -> {
            if (StringUtils.isNotEmpty(sysUser.getPassword())) {
                try {
                    String salt = sysUser.getSalt();
                    String key = sysUser.getSecretKey();
                    String iv = sysUser.getIv();

                    String encrypt = PasswordUtil.encrypt(resetPassword, key, salt, iv);
                    sysUser.setPassword(encrypt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
