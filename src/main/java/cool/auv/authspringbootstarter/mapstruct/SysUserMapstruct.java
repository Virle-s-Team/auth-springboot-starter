package cool.auv.authspringbootstarter.mapstruct;

import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.vm.SysUserVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SysUserMapstruct extends BaseSysUserMapstruct{


    @Override
    @Mapping(target = "roleSet", ignore = true)
    public abstract SysUser vmToEntity(SysUserVM sysUserVM);
}

