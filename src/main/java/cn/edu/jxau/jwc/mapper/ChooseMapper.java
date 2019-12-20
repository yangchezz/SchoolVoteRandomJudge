package cn.edu.jxau.jwc.mapper;


import cn.edu.jxau.jwc.pojo.model.Choose;
import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.pojo.model.Teacher;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public interface ChooseMapper {
    int insertNewChoose(Choose choose);

    int deleteChoose(Choose choose);

    int deleteAllChoose();

    Choose findChoose(@Param("chooseId") Long id,@Param("chooseType") int type);

    int deleteTypeChoose(Choose choose);

    int deleteAcademyStudentChoose(Student student);

    int deleteAcademyTeacherChoose(Teacher teacher);
}
