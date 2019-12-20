package cn.edu.jxau.jwc.test;

import cn.edu.jxau.jwc.Application;
import cn.edu.jxau.jwc.pojo.model.Student;
import cn.edu.jxau.jwc.pojo.model.Teacher;
import cn.edu.jxau.jwc.service.StudentService;
import cn.edu.jxau.jwc.service.TeacherService;
import cn.edu.jxau.jwc.util.poi.POIUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class MainTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;


    @Autowired
    private POIUtil poiUtil;

    @Test
//    @Transactional
    public void insertStudent() {
        File excel = new File("C:\\Users\\26332\\Documents\\WeChat Files\\yfh2633218009\\FileStorage\\File\\2019-12\\老師學生數據\\在校学生.xls");
        try {
            List<List<Object>> list = poiUtil.readExcel(excel, "student");
            int index = 0;

            for (List li : list) {
                try {
                    Student student = new Student();
                    student.setId(Long.parseLong((String) li.get(0)));
                    student.setName((String) li.get(1));
                    student.setGender((String) li.get(2));
                    student.setAcademy((String) li.get(4));
                    student.setMajor((String) li.get(6));
                    student.setClassName((String) li.get(9));
                    studentService.insertStudent(student);
                    index++;
                    if (index % 100 == 0) {
                        System.out.println(index);
                    }
                } catch (Exception e) {
                    System.err.println(li);
                }
            }
            System.out.print(index);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }


    @Test
//    @Transactional
    public void insertTeacher() {
        File excel = new File("C:\\Users\\26332\\Documents\\WeChat Files\\yfh2633218009\\FileStorage\\File\\2019-12\\老師學生數據\\老师数据.xls");
        try {
            List<List<Object>> list = poiUtil.readExcel(excel, "Sheet1");
            int index = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (List li : list) {
                try {
                    Teacher teacher = new Teacher();
                    teacher.setId(Long.parseLong((String) li.get(0)));
                    teacher.setName((String) li.get(1));
                    teacher.setGender(((String) li.get(2)).equals("1") ? "男" : ((String) li.get(2)).equals("2") ? "女" : "不详");
                    teacher.setAcademy((String) li.get(4));

                    teacher.setWorkTime(((String) li.get(7)).equals("NULL") ? new Date() : simpleDateFormat.parse((String) li.get(7)));
                    teacher.setTelephone((String) li.get(9));
                    index++;
                    if (index % 100 == 0) {
                        System.out.println(index);
                    }

                    if ("军体部".equals(teacher.getAcademy())) {
                        if(teacherService.findTeacher(teacher).getData() == null){
                            teacherService.insertTeacher(teacher);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.print(index);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void Test() throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:/excels/").getPath();
        System.out.print(path);
    }
}
