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


    /**
     * 标准答案转换成数值
     * @return 答案数值
     */
    public int getAnswer(){
        int sum = 0;
        for (int i = 0; i < answer.length() ; i++) {
            switch (answer.charAt(i)){
                case 'Y':
                    sum+=1;break;
                case 'N':
                    sum+=2;break;
                case 'A':
                    sum+=1;break;
                case 'B':
                    sum+=2;break;
                case 'C':
                    sum+=4;break;
                case 'D':
                    sum+=8;break;
            }
        }
        return sum;
    }

    /**
     * 对比学生答案与标准答案
     * @param stuAns 学生答案
     * @return 对/错
     */
    public boolean checkAnswer(int stuAns){
        return stuAns==getAnswer();
    }
}
