package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
    //int id;//题号
    String chapter;//单元
    String title;//题干
    String type;//题型
    String option1;//选项1
    String option2;//选项2
    String option3;//选项3
    String option4;//选项4
    String option5;//选项5
    String option6;//选项6
    String answer;//标准答案
}
