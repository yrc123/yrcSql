package com.fzu.pojo;
/*与教师表对应的实体类*/
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TTable {
    String teacherId;//工号
    String teacherName;//姓名
}
