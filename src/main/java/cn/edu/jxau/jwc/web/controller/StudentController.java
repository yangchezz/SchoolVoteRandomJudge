package cn.edu.jxau.jwc.web.controller;

import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.service.StudentService;
import cn.edu.jxau.jwc.util.JsonResult;
import cn.edu.jxau.jwc.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/Student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @RequestMapping("/randomAcademyStudent")
    public JsonResult<List<Student>> randomAcademyStudent(Student student,@RequestParam("randomCount") Integer randomCount) {
        return studentService.findRandomStudent(student, randomCount);
    }


    @RequestMapping("/randomAllStudent")
    public JsonResult randomAllAcademyStudent() {
        return new JsonResult("暂未开发", ResultCode.FAIL, false);
    }

    @RequestMapping("/academySelectStudent")
    public JsonResult getAcademyAllSelectStudent(Student student) {
        return studentService.findAcademyAllSelectStudent(student);
    }

    @RequestMapping("/allSelectStudent")
    public JsonResult getAllSelectStudent() {
        return studentService.fineAllSelectStudent();
    }

    @RequestMapping("/resetAcademyStudent")
    public JsonResult resetAcademyStudent(String academy) {
        return studentService.resetAcademyStudentRandom(academy);
    }

    @RequestMapping("/resetAllStudent")
    public JsonResult resetAllStudent() {
        return studentService.resetAllStudentRandom();
    }

    @RequestMapping("/findAllAcademy")
    public JsonResult getAllAcademyName() {
        return studentService.findAllAcademy();
    }

    @RequestMapping("/randomAllData")
    public JsonResult randomAllData(@RequestParam("randomCount") Integer randomCount) {
        return studentService.randomAllData(randomCount);
    }


    @RequestMapping("/saveAllRandom")
    public JsonResult saveAllRandomData(@RequestParam("students") long[] students, @RequestParam("teachers") long[] teachers) {
        return studentService.saveAllRandomResult(students, teachers);
    }


    @RequestMapping("/downloadStudentExcel")
    public void downloadStudentExcel(HttpServletResponse servletResponse, HttpServletRequest servletRequest) throws FileNotFoundException {
        try {
            studentService.downloadStudentExcel(servletRequest, servletResponse);
        } catch (IOException e) {
            try {
                servletResponse.getWriter().println(new JsonResult<>("下载失败",ResultCode.FAIL,false));
            } catch (IOException ex) {
                System.err.print("下载失败，发生异常"  + e.getMessage());
            }
        }
    }


    @RequestMapping("/downloadTeacherExcel")
    public void downloadTeacherExcel(HttpServletResponse servletResponse, HttpServletRequest servletRequest) throws FileNotFoundException {
        try {
            studentService.downloadTeacherExcel(servletRequest, servletResponse);
        } catch (IOException e) {
            try {
                servletResponse.getWriter().println(new JsonResult<>("下载失败",ResultCode.FAIL,false));
            } catch (IOException ex) {
                System.err.print("下载失败，发生异常"  + e.getMessage());
            }
        }
    }
}
