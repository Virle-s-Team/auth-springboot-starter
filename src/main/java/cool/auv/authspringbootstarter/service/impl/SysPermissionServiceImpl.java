package cool.auv.authspringbootstarter.service.impl;

import cool.auv.authspringbootstarter.entity.SysPermission;
import cool.auv.authspringbootstarter.service.SysPermissionService;
import cool.auv.authspringbootstarter.vm.SysPermissionVM;
import cool.auv.authspringbootstarter.vm.request.SysPermissionRequest;
import cool.auv.codegeneratorjpa.core.base.AbstractAutoService;
import cool.auv.codegeneratorjpa.core.base.BaseAutoMapstruct;
import cool.auv.codegeneratorjpa.core.base.BaseRepository;
import cool.auv.codegeneratorjpa.core.base.CustomServiceMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysPermissionServiceImpl extends AbstractAutoService<SysPermission, Long, SysPermissionRequest, SysPermissionVM>
        implements SysPermissionService, CustomServiceMarker<SysPermission> {

    @Autowired
    private BaseRepository<SysPermission, Long> baseSysPermissionRepository;

    @Autowired
    private BaseAutoMapstruct<SysPermission, SysPermissionVM> baseSysPermissionMapstruct;

    @Override
    public Set<SysPermissionVM> getPermissionTree() {
        // 获取所有权限
        List<SysPermission> allPermissions = baseSysPermissionRepository.findAll();

        // 转换为 VM
        List<SysPermissionVM> allPermissionVMs = allPermissions.stream()
                .map(baseSysPermissionMapstruct::entityToVM)
                .collect(Collectors.toList());

        // 构建树状结构：过滤出根节点（parent 为 null）
        Set<SysPermissionVM> rootPermissions = allPermissionVMs.stream()
                .filter(vm -> vm.getParent() == null)
                .collect(Collectors.toSet());

        // 为每个根节点设置子节点
        rootPermissions.forEach(root -> buildChildren(root, allPermissionVMs));

        return rootPermissions;
    }

    /**
     * 递归构建子节点树
     *
     * @param parent         当前父节点
     * @param allPermissions 所有权限列表
     */
    private void buildChildren(SysPermissionVM parent, List<SysPermissionVM> allPermissions) {
        List<SysPermissionVM> children = allPermissions.stream()
                .filter(vm -> vm.getParent() != null && vm.getParent().getId().equals(parent.getId()))
                .toList();

        // 清空 parent 的引用，避免 JSON 序列化循环
        children.forEach(child -> {
            child.setParent(null);
            buildChildren(child, allPermissions);
        });
        // 设置子节点
        parent.setChildren(new HashSet<>(children));
    }
}
