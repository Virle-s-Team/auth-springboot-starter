package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.service.SysRoleService;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.authspringbootstarter.vm.request.SysRoleRequest;
import cool.auv.codegeneratorjpa.core.base.AbstractAutoService;
import cool.auv.codegeneratorjpa.core.base.CustomServiceMarker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRoleServiceImpl extends AbstractAutoService<SysRole, Long, SysRoleRequest, SysRoleVM> implements SysRoleService, CustomServiceMarker<SysRole> {

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
