package cool.auv.authspringbootstarter.service.mapstruct;

import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.vm.SysUserUpdateVM;
import cool.auv.codegeneratorjpa.core.mapstruct.BaseMapstruct;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {}
)
public abstract class SysUserUpdateVMMapstruct extends BaseMapstruct<SysUser, SysUserUpdateVM> {
}
