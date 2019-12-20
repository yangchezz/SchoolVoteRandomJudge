package cn.edu.jxau.jwc.service.impl;

import cn.edu.jxau.jwc.mapper.ChooseMapper;
import cn.edu.jxau.jwc.mapper.TeacherMapper;
import cn.edu.jxau.jwc.pojo.model.Choose;
import cn.edu.jxau.jwc.pojo.model.Teacher;
import cn.edu.jxau.jwc.service.TeacherService;
import cn.edu.jxau.jwc.util.JsonResult;
import cn.edu.jxau.jwc.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;


    @Autowired
    private ChooseMapper chooseMapper;

    @Override
    public JsonResult<Teacher> insertTeacher(Teacher teacher) {
        int res = teacherMapper.insertTeacher(teacher);
        if (res > 0) {
            return new JsonResult<>("插入成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult<>("插入失败", ResultCode.FAIL, false);
    }

    @Override
    public JsonResult<Teacher> findTeacher(Teacher teacher) {
        Teacher res = teacherMapper.findTeacher(teacher);
        if (res == null) {
            return new JsonResult<>("数据不存在", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", res, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Teacher>> findAcademyAllTeacher(Teacher teacher) {
        List<Teacher> list = teacherMapper.findAcademyAllTeacher(teacher);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Teacher>> findAcademyAllSelectTeacher(Teacher teacher) {
        List<Teacher> list = teacherMapper.findAcademyAllSelectTeacher(teacher);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Teacher>> findAllTeacherLimit(int pageSize) {
        int lineStart = (pageSize - 1) * 5;
        List<Teacher> list = teacherMapper.findAllTeacherLimit(lineStart, 5);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Teacher>> findAllSelectTeacher() {
        List<Teacher> list = teacherMapper.findAllSelectTeacher();
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }


    @Transactional
    @Override
    public JsonResult<List<Teacher>> findRandomTeacher(Teacher teacher,Integer randomCount) {
        /*
            查询次学院的随机人数
            获取随机值
         */
        int count = teacherMapper.countAcademyTeacher(teacher.getAcademy());
        if (count <= randomCount) {
            List<Teacher> list = this.findAcademyAllTeacher(teacher).getData();
            if (list.isEmpty()) {
                return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
            }
            for (Teacher t : list) {
                Choose choose = chooseMapper.findChoose(t.getId(), 2);
                //没有被选中
                if (choose == null) {
                    choose = new Choose();
                    choose.setChooseId(t.getId());
                    choose.setChooseType(2);
                    chooseMapper.insertNewChoose(choose);
                }
            }
            return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
        }
        Random random = new Random();
        int step = count / randomCount;
        for (int i = 0; i < randomCount; i++) {
            int start = random.nextInt(step) + step * i;
            List<Teacher> res = teacherMapper.findRandomTeacher(start, 1, teacher.getAcademy());
            for (Teacher tea : res) {
                Choose choose = chooseMapper.findChoose(tea.getId(), 2);
                //没有被选中
                if (choose == null) {
                    choose = new Choose();
                    choose.setChooseId(tea.getId());
                    choose.setChooseType(2);
                    chooseMapper.insertNewChoose(choose);
                }
            }
        }

        List<Teacher> list = this.findAcademyAllSelectTeacher(teacher).getData();
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult resetAllTeacherRandom() {
        Choose choose = new Choose();
        choose.setChooseType(2);
        int res = chooseMapper.deleteTypeChoose(choose);
        if (res > 0) {
            return new JsonResult("删除成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult("删除失败", ResultCode.FAIL, false);
    }

    @Override
    public JsonResult resetAcademyTeacherRandom(String academy) {
        Teacher teacher = new Teacher();
        teacher.setAcademy(academy);
        int res = chooseMapper.deleteAcademyTeacherChoose(teacher);
        if (res > 0) {
            return new JsonResult("删除成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult("删除失败", ResultCode.FAIL, false);
    }
}
