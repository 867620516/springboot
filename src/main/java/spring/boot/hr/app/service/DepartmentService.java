package spring.boot.hr.app.service;

import org.springframework.stereotype.Service;
import spring.boot.hr.app.dao.DepartmentMapper;
import spring.boot.hr.app.model.Department;
import spring.boot.hr.sys.base.BaseService;

@Service
public class DepartmentService extends BaseService<Department, DepartmentMapper> {
}
