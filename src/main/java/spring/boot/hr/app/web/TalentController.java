package spring.boot.hr.app.web;

import com.github.pagehelper.PageInfo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.boot.hr.app.model.Talent;
import spring.boot.hr.app.service.TalentService;

@RestController
@RequestMapping("/api/talent")
public class TalentController {
    @Autowired
    private TalentService service;
     /**
     * 分页查询
     * @param condition
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("")
    public PageInfo<Talent> page(Talent condition ,Integer pageNum ,Integer pageSize){
        return service.pageInfo(condition,pageNum,pageSize);
    }


    @PostMapping("save")
    public void save(@RequestBody Talent talent){
        service.save(talent);
    }

    /**
     * 删除一条记录
     * @param id
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }
}
