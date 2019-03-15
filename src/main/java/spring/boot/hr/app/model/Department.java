package spring.boot.hr.app.model;

import lombok.Data;
import spring.boot.hr.sys.base.BaseModel;

@Data
public class Department extends BaseModel {
    private String departName;
    private String departPhone;
}
