package cn.edu.jxau.jwc.web.controller;

import cn.edu.jxau.jwc.pojo.model.Teacher;
import cn.edu.jxau.jwc.service.TeacherService;
import cn.edu.jxau.jwc.util.JsonResult;
import cn.edu.jxau.jwc.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/Teacher")
public class TeacherController {


    @Autowired
    private TeacherService teacherService;

    public JsonResult randomAllAcademyTeacher() {
        return new JsonResult("暂未开发", ResultCode.SUCCESS, true);
    }

    @RequestMapping("/randomAcademyTeacher")
    public JsonResult randomAcademyTeacher(Teacher teacher,@RequestParam("randomCount") Integer randomCount) {
        return teacherService.findRandomTeacher(teacher,randomCount);
    }


    @RequestMapping("/findAllSelectTeacher")
    public JsonResult findAllSelectTeacher() {
        return teacherService.findAllSelectTeacher();
    }

    @RequestMapping("/findAcademySelectTeacher")
    public JsonResult findAcademyAllSelectTeacher(Teacher teacher) {
        return teacherService.findAcademyAllSelectTeacher(teacher);
    }

    @RequestMapping("/resetAcademyTeacher")
    public JsonResult resetAcademyTeacher(String academy) {
        return teacherService.resetAcademyTeacherRandom(academy);
    }

    @RequestMapping("/resetAllTeacher")
    public JsonResult resetAllTeacher() {
        return teacherService.resetAllTeacherRandom();
    }


}
