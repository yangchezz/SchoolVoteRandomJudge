create table sc_teacher
(
    teacher_id        bigint primary key comment '老师ID',
    teacher_name      char(10) not null comment '老师的名字',
    teacher_gender    char(2) default '男' comment '老师的性别',
    teacher_academy   char(20) not null comment '老师的学院信息',
    teacher_telephone char(12) not null comment '老师的电话信息',
    teacher_work_time date    default null comment '老师的参加工作的时间'
) charset utf8;


create table sc_student
(
    student_id         bigint primary key comment '学生ID',
    student_name       char(10) not null comment '学生的名字',
    student_gender     char(2) default '男' comment '学生性别',
    student_academy    char(20) not null comment '学生的学院信息',
    student_class_name char(20) not null comment '学生的班级信息',
    student_major      char(12) not null comment '学生的专业'
) charset utf8;


create table bu_choose
(
    choose_index int auto_increment primary key comment '逻辑主键',
    choose_id    bigint not null comment '被选择者',
    choose_type  int default 1 comment '选择的类型'
) charset utf8;