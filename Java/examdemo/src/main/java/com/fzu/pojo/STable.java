package com.fzu.pojo;
/*与学生表对应的实体类*/
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class STable {
    String studentId;//学号
    String name;//姓名
    String classInfo;//班级信息
}
