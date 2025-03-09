package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.repository.BaseSysUserRepository;
import cool.auv.authspringbootstarter.vm.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SimpleUserService implements UserDetailsService {

    @Autowired
    private BaseSysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Example<SysUser> condition = Example.of(new SysUser().setUsername(username));
        Optional<SysUser> sysUserOpt = sysUserRepository.findOne(condition);
        return sysUserOpt.map(sysUser -> new SimpleUser(sysUser.getUsername(), sysUser.getPassword())).orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));
    }
}
