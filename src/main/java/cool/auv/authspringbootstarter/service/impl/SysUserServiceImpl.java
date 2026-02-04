package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.config.security.jwt.TokenProvider;
import cool.auv.authspringbootstarter.constant.AuthoritiesConstants;
import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.service.SysUserService;
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.vm.*;
import cool.auv.authspringbootstarter.vm.request.SysUserRequest;
import cool.auv.codegeneratorjpa.core.base.AbstractAutoService;
import cool.auv.codegeneratorjpa.core.base.BaseAutoMapstruct;
import cool.auv.codegeneratorjpa.core.base.BaseRepository;
import cool.auv.codegeneratorjpa.core.base.CustomServiceMarker;
import cool.auv.codegeneratorjpa.core.exception.AppException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends AbstractAutoService<SysUser, Long, SysUserRequest, SysUserVM> implements SysUserService, CustomServiceMarker<SysUser> {

    @PersistenceContext
    private EntityManager entityManager;

    @Lazy
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private BaseAutoMapstruct<SysRole, SysRoleVM> baseSysRoleMapstruct;

    @Autowired
    private BaseRepository<SysUser, Long> baseSysUserRepository;

    @Value("${app.reset-password:123456}")
    private String resetPassword;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String login(LoginVM loginVM) {
        String username = loginVM.getUsername();
        String password = loginVM.getPassword();

        SysUser user = baseSysUserRepository.findOne(Example.of(new SysUser().setUsername(username)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationServiceException("用户名或密码错误");
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthoritiesConstants.LOGIN_ACCOUNT_NAME, user.getUsername());
        claims.put(AuthoritiesConstants.LOGIN_ACCOUNT_TENANT_ID, user.getTenantId());
        claims.put(AuthoritiesConstants.LOGIN_ACCOUNT_ID, user.getId());
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Set<SysRoleVM>> getRole(Long userId) {
        return baseSysUserRepository.findById(userId).map(user -> user.getRoleSet().stream().map(baseSysRoleMapstruct::entityToVM).collect(Collectors.toSet()));
    }

    @Override
    @Transactional(readOnly = true)
    public SysUser loadUserWithAuthorities(Long userId) {
        SysUser sysUser = baseSysUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        sysUser.getAuthorities(); // 触发懒加载
        return sysUser;
    }

    @Transactional(readOnly = true)
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
    public void save(SysUserUpdateVM sysUserVM) {
        SysUser sysUser = mapstruct.vmToEntity(sysUserVM);

        if (StringUtils.isNotEmpty(sysUser.getPassword())) {
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        } else {
            sysUser.setPassword(passwordEncoder.encode(resetPassword));
        }

        baseSysUserRepository.save(sysUser);
    }

    @Override
    @Transactional
    public void update(SysUserUpdateVM sysUserVM) {
        baseSysUserRepository.findById(sysUserVM.getId()).ifPresent(sysUser -> {
            // 只在提供新密码时更新
            if (StringUtils.isNotEmpty(sysUserVM.getPassword())) {
                sysUser.setPassword(passwordEncoder.encode(sysUserVM.getPassword()));
            }
//            sysUserUpdateVMMapstruct.updateEntityFromVM(sysUserVM, sysUser);
        });
    }

    @Override
    @Transactional
    public void resetPassword(Long userId) {
        baseSysUserRepository.findById(userId).ifPresent(sysUser -> {
            sysUser.setPassword(passwordEncoder.encode(resetPassword));
            baseSysUserRepository.save(sysUser);
        });
    }

    @Override
    @Transactional
    public void updatePassword(String oldPassword, String newPassword) throws AppException {
        SysUser sysUser = SecurityContextUtil.getCurrentUser()
                .orElseThrow(() -> new AppException("当前用户信息获取失败"));

        if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
            throw new AppException("旧密码错误");
        }

        sysUser.setPassword(passwordEncoder.encode(newPassword));
        baseSysUserRepository.save(sysUser);
    }
}
