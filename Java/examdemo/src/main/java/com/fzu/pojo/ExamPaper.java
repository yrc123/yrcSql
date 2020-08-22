package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
/*一份试卷*/
public class ExamPaper {
    int examId;//试卷编号
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date date;//考试结束的时间
    int []Qtype;//各类题目的数目
    List<Qdata> qdataList;//题目集


}
