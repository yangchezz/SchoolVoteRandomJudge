package cn.edu.jxau.jwc.pojo.model;

import cn.edu.jxau.jwc.pojo.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public class Teacher extends BaseModel {
    private Date workTime;
    private String telephone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8",locale = "zh")
    public Date getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Date workTime) {
        this.workTime = workTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "workTime=" + workTime +
                ", telephone='" + telephone + '\'' + super.toString() +
                '}';
    }
}
