package cool.auv.authspringbootstarter.mapstruct;

import cool.auv.authspringbootstarter.entity.SysRole;
import cool.auv.authspringbootstarter.vm.SysRoleVM;
import cool.auv.codegeneratorjpa.core.base.BaseAutoMapstruct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SysRoleMapstruct extends BaseAutoMapstruct<SysRole, SysRoleVM> {

    @Override
    public abstract SysRoleVM entityToVM(SysRole entity);
}
