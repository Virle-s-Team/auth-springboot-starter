package cool.auv.authspringbootstarter.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SysUserUpdateVM extends SysUserVM {

    private String password;
}
