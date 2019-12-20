package cn.edu.jxau.jwc.service.impl;

import cn.edu.jxau.jwc.mapper.ChooseMapper;
import cn.edu.jxau.jwc.mapper.StudentMapper;
import cn.edu.jxau.jwc.mapper.TeacherMapper;
import cn.edu.jxau.jwc.pojo.model.Choose;
import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.pojo.model.Teacher;
import cn.edu.jxau.jwc.service.StudentService;
import cn.edu.jxau.jwc.util.JsonResult;
import cn.edu.jxau.jwc.util.ResultCode;
import cn.edu.jxau.jwc.util.poi.POIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ChooseMapper chooseMapper;


    @Autowired
    private POIUtil poiUtil;

    @Override
    public JsonResult<Student> insertStudent(Student student) {
        int res = studentMapper.insertStudent(student);
        if (res > 0) {
            return new JsonResult<>("插入成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult<>("插入失败", student, ResultCode.FAIL, false);
    }

    @Override
    public JsonResult<Student> findStudentById(Long studentId) {
        Student student = studentMapper.findStudentById(studentId);
        if (student == null) {
            return new JsonResult<>("数据不存在", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("数据查询成功", student, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Student>> findAllStudentLimit(int pageIndex) {
        int lineStart = (pageIndex - 1) * 10;
        List<Student> list = studentMapper.findAllStudentLimit(lineStart, 10);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("数据查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Student>> fineAllSelectStudent() {
        List<Student> list = studentMapper.fineAllSelectStudent();
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("数据查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Student>> findAcademyAllSelectStudent(Student student) {
        List<Student> list = studentMapper.findAcademyAllSelectStudent(student);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", list, ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("数据查询成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult<List<Student>> findAcademyAllStudent(Student student) {
        List<Student> list = studentMapper.findAcademyAllStudent(student);
        if (list.isEmpty()) {
            return new JsonResult<>("数据不存在", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("数据查询成功", list, ResultCode.SUCCESS, true);
    }


    @Transactional
    @Override
    public JsonResult<List<Student>> findRandomStudent(Student student,Integer randomCount) {
        int count = studentMapper.countAcademyStudent(student.getAcademy());
        if (count <= 0) {
            return new JsonResult<>("暂无数据", ResultCode.NOT_FOUND, false);
        }
        Random random = new Random();
        int step = count / randomCount;
        for (int i = 0; i < randomCount; i++) {
            int start = random.nextInt(step) + step * i;
            List<Student> res = studentMapper.findRandomStudent(start, 1, student.getAcademy());
            for (Student stu : res) {
                Choose choose = chooseMapper.findChoose(stu.getId(), 1);
                //没有被选中
                if (choose == null) {
                    choose = new Choose();
                    choose.setChooseId(stu.getId());
                    choose.setChooseType(1);
                    chooseMapper.insertNewChoose(choose);
                }
            }
        }
        //查询全部随机生成的学生数据
        List<Student> list = this.findAcademyAllSelectStudent(student).getData();
        if (list.isEmpty()) {
            return new JsonResult<>("随机失败", ResultCode.FAIL, false);
        }
        return new JsonResult<>("操作成功", list, ResultCode.SUCCESS, true);
    }

    @Override
    public JsonResult resetAllStudentRandom() {
        Choose choose = new Choose();
        choose.setChooseType(1);
        int res = chooseMapper.deleteTypeChoose(choose);
        if (res > 0) {
            return new JsonResult("删除成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult("删除失败", ResultCode.FAIL, false);
    }

    @Override
    public JsonResult resetAcademyStudentRandom(String academy) {
        Student student = new Student();
        student.setAcademy(academy);
        int res = chooseMapper.deleteAcademyStudentChoose(student);
        if (res > 0) {
            return new JsonResult("删除成功", ResultCode.SUCCESS, true);
        }
        return new JsonResult("删除失败", ResultCode.FAIL, false);
    }

    @Override
    public JsonResult<List<String>> findAllAcademy() {
        List<String> list = studentMapper.findAllAcademy();
        if (list.isEmpty()) {
            return new JsonResult<>("暂无数据", ResultCode.NOT_FOUND, false);
        }
        return new JsonResult<>("查询成功", list, ResultCode.SUCCESS, true);
    }


    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public JsonResult randomAllData(Integer randomCount) {
        List<String> list = this.findAllAcademy().getData();
        List<Student> students = new ArrayList<>(list.size() * randomCount);
        List<Teacher> teachers = new ArrayList<>(list.size() * randomCount);
        Random random = new Random();
        for (String academy : list) {
            //获取全部院系的信息
            int count = studentMapper.countAcademyStudent(academy);
            if (count > 0) {
                int step = count / randomCount;
                for (int i = 0; i < randomCount; i++) {
                    int start = random.nextInt(step) + step * i;
                    List<Student> res = studentMapper.findRandomStudent(start, 1, academy);
                    students.addAll(res);
                }
            }
            //生成老师数据
            count = teacherMapper.countAcademyTeacher(academy);
            if (count <= randomCount) {
                Teacher teacher = new Teacher();
                teacher.setAcademy(academy);
                List<Teacher> res = teacherMapper.findAcademyAllTeacher(teacher);
                teachers.addAll(res);
            }else{
                int step = count / randomCount;
                for (int i = 0; i < randomCount; i++) {
                    int start = random.nextInt(step) + step * i;
                    List<Teacher> res = teacherMapper.findRandomTeacher(start, 1, academy);
                    teachers.addAll(res);
                }
            }

        }
        List<Object> result = new ArrayList<>(students.size() + teachers.size());
        result.addAll(students);
        result.addAll(teachers);
        return new JsonResult<>("生成成功", result, ResultCode.SUCCESS, true);
    }

    @Override
    @Transactional
    public JsonResult saveAllRandomResult(long[] students, long[] teachers) {
        //清空全部数据
        chooseMapper.deleteAllChoose();
        //清空表数据
        for (long studentId : students) {
            Choose choose = new Choose();
            choose.setChooseType(1);
            choose.setChooseId(studentId);
            chooseMapper.insertNewChoose(choose);
        }
        for (long teacherId : teachers) {
            Choose choose = new Choose();
            choose.setChooseType(2);
            choose.setChooseId(teacherId);
            chooseMapper.insertNewChoose(choose);
        }
        return new JsonResult("保存成功", ResultCode.SUCCESS, true);
    }

    @Override
    public void downloadStudentExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Student> students = this.fineAllSelectStudent().getData();
        String path = URLDecoder.decode(ResourceUtils.getURL("classpath:/excels/").getPath(),"utf-8");
        File studentFile = new File(path + "student.xls");
        try {
            poiUtil.writeExcel(exchangeStudent(students), studentFile, "student", false);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        response.setContentType("multipart/form-data");
        // 设置响应头，控制浏览器下载该文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(studentFile.getName(), "UTF-8"));
        this.printFile(response.getOutputStream(),studentFile);
    }

    @Override
    public void downloadTeacherExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = URLDecoder.decode(ResourceUtils.getURL("classpath:/excels/").getPath(),"utf-8");
        List<Teacher> teachers = teacherMapper.findAllSelectTeacher();
        File teacherFile = new File(path + "teacher.xls");
        try {
            poiUtil.writeExcel(exchangeTeacher(teachers), teacherFile, "teacher", false);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        response.setContentType("multipart/form-data");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(teacherFile.getName(), "UTF-8"));
        this.printFile(response.getOutputStream(),teacherFile);
    }

    private void printFile(OutputStream outputStream, File file) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        BufferedInputStream bufferedInputStream  = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[1024*200];
        int len = 0;
        while((len = bufferedInputStream.read(bytes)) > 0){
            bufferedOutputStream.write(bytes,0,len);
        }
        bufferedOutputStream.flush();
    }


    private List<List<Object>> exchangeStudent(List<Student> students) {
        List<List<Object>> res = new ArrayList<>(students.size());
        res.add(getStudentTitle());
        for (Student student : students) {
            List<Object> li = new ArrayList<>(res.get(0).size());
            li.add(student.getId());
            li.add(student.getName());
            li.add(student.getName());
            li.add(student.getGender());
            li.add(student.getAcademy());
            li.add(student.getClassName());
            li.add(student.getMajor());
            res.add(li);
        }
        return res;
    }

    private List<List<Object>> exchangeTeacher(List<Teacher> teachers) {
        List<List<Object>> res = new ArrayList<>(teachers.size());
        res.add(getTeacherTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Teacher teacher : teachers) {
            List<Object> li = new ArrayList<>(res.get(0).size());
            li.add(teacher.getId());
            li.add(teacher.getName());
            li.add(teacher.getGender());
            li.add(teacher.getAcademy());
            li.add(simpleDateFormat.format(teacher.getWorkTime()));
            li.add(teacher.getTelephone());
            res.add(li);
        }
        return res;
    }

    private List<Object> getStudentTitle() {
        String[] titles = new String[]{"学号", "姓名", "性别", "院系", "班级", "专业"};
        return new ArrayList<>(Arrays.asList(titles));
    }

    private List<Object> getTeacherTitle() {
        String[] titles = new String[]{"工号", "姓名", "性别", "院系", "工作时间", "电话"};
        return new ArrayList<>(Arrays.asList(titles));
    }
}
