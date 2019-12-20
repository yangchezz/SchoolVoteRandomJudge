package cn.edu.jxau.jwc.service;

import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.util.JsonResult;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public interface StudentService {

    JsonResult<Student> insertStudent(Student student);

    JsonResult<Student> findStudentById(Long studentId);

    JsonResult<List<Student>> findAllStudentLimit(int pageIndex);

    JsonResult<List<Student>> fineAllSelectStudent();

    JsonResult<List<Student>> findAcademyAllSelectStudent(Student student);

    JsonResult<List<Student>> findAcademyAllStudent(Student student);

    JsonResult<List<Student>> findRandomStudent(Student student,Integer randomCount);

    JsonResult resetAllStudentRandom();

    JsonResult resetAcademyStudentRandom(String academy);

    JsonResult<List<String>> findAllAcademy();

    JsonResult randomAllData(Integer randomData);

    JsonResult saveAllRandomResult(long[] students, long[] teachers);

    void downloadStudentExcel(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void downloadTeacherExcel(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
