package cn.edu.jxau.jwc.pojo.model;

import cn.edu.jxau.jwc.pojo.BaseModel;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public class Student extends BaseModel {
    private String className;
    private String major;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "Student{" +
                "className='" + className + '\'' +
                ", major='" + major + '\'' + super.toString() +
                '}';
    }
}
