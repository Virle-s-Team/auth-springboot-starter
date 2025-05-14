package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.service.SysRoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRoleServiceImpl extends BaseSysRoleServiceImpl implements SysRoleService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        SysRole role = entityManager.getReference(SysRole.class, roleId);
        SysPermission permission = entityManager.getReference(SysPermission.class, permissionId);
        role.getPermissionSet().add(permission);
    }

    @Override
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        SysRole role = entityManager.getReference(SysRole.class, roleId);
        SysPermission permission = entityManager.getReference(SysPermission.class, permissionId);
        role.getPermissionSet().remove(permission);
    }
}
