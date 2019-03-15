package spring.boot.hr.app.dao;

import org.springframework.stereotype.Repository;
import spring.boot.hr.sys.base.BaseDao;
import spring.boot.hr.sys.security.User;

@Repository
public interface UserMapper extends BaseDao<User> {
}
