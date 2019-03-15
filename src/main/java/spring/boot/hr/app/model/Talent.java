package spring.boot.hr.app.model;

import lombok.Data;
import spring.boot.hr.sys.base.BaseModel;

import java.util.Date;

@Data
public class Talent extends BaseModel {
    private String name;
    private String sexual;
    private Date employDate;
    private String graduation;

    private Department department;
}
