package cn.edu.jxau.jwc.mapper;

import cn.edu.jxau.jwc.pojo.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */

public interface StudentMapper {

    int insertStudent(Student student);

    Student findStudentById(Long studentId);

    List<Student> findAllStudentLimit(@Param("lineIndex") int lineIndex, @Param("pageSize") int pageSize);

    List<Student> fineAllSelectStudent();

    List<Student> findAcademyAllSelectStudent(Student student);

    List<Student> findAcademyAllStudent(Student student);

    List<Student> findRandomStudent(@Param("lineStart") int lineStart, @Param("randomSize") int randomSize,@Param("academyName") String academyName);

    int countAcademyStudent(String academyName);

    List<String> findAllAcademy();
}
