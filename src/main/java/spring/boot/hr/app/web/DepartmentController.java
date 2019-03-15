package spring.boot.hr.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.hr.app.model.Department;
import spring.boot.hr.app.model.Talent;
import spring.boot.hr.app.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;


    @RequestMapping("all")
    public List<Department> findAll(){
        return departmentService.findAll();
    }

}
