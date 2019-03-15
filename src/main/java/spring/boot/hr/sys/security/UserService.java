package spring.boot.hr.sys.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.boot.hr.app.dao.UserMapper;
import spring.boot.hr.sys.base.BaseService;

/**
 * spring security核心组件 服务，通过该类和username查询对应的用户信息
 */
@Service
public class UserService extends BaseService<User, UserMapper>  implements UserDetailsService {
    /**
     * 通过用户名获得用户对象
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User condition = new User();
        condition.setUsername(s);
        return this.dao.find(condition);
    }
}
