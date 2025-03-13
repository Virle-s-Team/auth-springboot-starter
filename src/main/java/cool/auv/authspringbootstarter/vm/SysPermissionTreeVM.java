package cool.auv.authspringbootstarter.vm;

import cool.auv.authspringbootstarter.entity.SysPermission;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class SysPermissionTreeVM {

    private SysPermission permission;

    private Set<SysPermission> children;
}
