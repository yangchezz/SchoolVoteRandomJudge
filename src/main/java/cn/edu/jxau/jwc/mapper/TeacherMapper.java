package cn.edu.jxau.jwc.mapper;

import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.pojo.model.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public interface TeacherMapper {

    int insertTeacher(Teacher teacher);

    Teacher findTeacher(Teacher teacher);

    List<Teacher> findAcademyAllTeacher(Teacher teacher);

    List<Teacher> findAcademyAllSelectTeacher(Teacher teacher);

    List<Teacher> findAllTeacherLimit(@Param("lineIndex") int lineIndex, @Param("pageSize") int pageSize);

    List<Teacher> findAllSelectTeacher();

    List<Teacher> findRandomTeacher(@Param("lineStart") int lineStart, @Param("randomSize") int randomSize, @Param("academyName") String academyName);

    int countAcademyTeacher(String academyName);
}
