package spring.boot.hr.app.dao;

import org.springframework.stereotype.Repository;
import spring.boot.hr.app.model.Department;
import spring.boot.hr.app.model.Talent;
import spring.boot.hr.sys.base.BaseDao;

@Repository
public interface DepartmentMapper extends BaseDao<Department> {
}
