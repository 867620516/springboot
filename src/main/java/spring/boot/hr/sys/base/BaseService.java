package spring.boot.hr.sys.base;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService<T extends BaseModel,D extends BaseDao<T>> {

    @Autowired
    protected D dao;

    public void save(T t){
        if(t.getId()!=null){
            dao.update(t);
        }else{
            dao.insert(t);
        }
    }

    public void delete(T t){
        dao.delete(t);
    }
    public void delete(Long id){
        dao.delete(id);
    }


    public T findById(Long id){
        return dao.findById(id);
    }

    public T find(T t){
        return  dao.find(t);
    }
    public List<T> findList(T t){
        return dao.findList(t);
    }
    public List<T> findAll(){
        return dao.findAll();
    }

    /**
     * 专门用来返回分页数据的方法
     * @param t
     * @param pageNum 当前页（第几页）
     * @param pageSiza
     * @return
     */
    public PageInfo<T> pageInfo(T t, Integer pageNum, Integer pageSiza){
        //采用静态方法
        PageHelper.startPage(pageNum,pageSiza);
        //调用具体的查询方法
        List<T> list = dao.findList(t);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
