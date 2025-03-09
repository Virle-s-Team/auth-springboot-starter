package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.vm.LoginVM;

public interface SysUserService extends BaseSysUserService{

    public String login(LoginVM loginVM) throws Exception;
}
