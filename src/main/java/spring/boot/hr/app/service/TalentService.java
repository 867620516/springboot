package spring.boot.hr.app.service;

import org.springframework.stereotype.Service;
import spring.boot.hr.app.dao.TalentMapper;
import spring.boot.hr.app.model.Talent;
import spring.boot.hr.sys.base.BaseService;

@Service
public class TalentService extends BaseService<Talent, TalentMapper> {
}
