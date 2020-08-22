package com.fzu.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
/*与前端交互的类*/
public class Qdata {
    String title;//标题
    List<String> choice;
}
