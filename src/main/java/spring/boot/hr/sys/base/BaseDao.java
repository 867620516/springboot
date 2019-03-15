package spring.boot.hr.sys.base;

import java.util.List;

public interface BaseDao<T> {
    void insert(T t);
    void update(T t);
    void delete(T t);
    void delete(Long id);
    T find(T t);
    List<T> findList(T t);
    List<T> findAll();
    T findById(Long id);

}
