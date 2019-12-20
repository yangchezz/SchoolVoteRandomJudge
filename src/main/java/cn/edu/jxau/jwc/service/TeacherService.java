package cn.edu.jxau.jwc.service;

import cn.edu.jxau.jwc.pojo.model.Teacher;
import cn.edu.jxau.jwc.util.JsonResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public interface TeacherService {

    JsonResult<Teacher> insertTeacher(Teacher teacher);

    JsonResult<Teacher> findTeacher(Teacher teacher);

    JsonResult<List<Teacher>> findAcademyAllTeacher(Teacher teacher);

    JsonResult<List<Teacher>> findAcademyAllSelectTeacher(Teacher teacher);

    JsonResult<List<Teacher>> findAllTeacherLimit(int pageSize);

    JsonResult<List<Teacher>> findAllSelectTeacher();

    JsonResult<List<Teacher>> findRandomTeacher(Teacher teacher,Integer randomCount);

    JsonResult resetAllTeacherRandom();

    JsonResult resetAcademyTeacherRandom(String academy);
}
