# 数据库实践仓库

- 所有请求使用post，使用json格式
- 后端在收到操作时要对操作用户的权限进行判断（过滤器）
  - 对于没登录的用户转跳到登录界面
  - 对于权限不够的用户转跳到权限不足的页面。
- 第一次登录时必须强制修改密码，不允许其他操作，在用户操作其他方法时强制下线(删除userID,username,character,注销userID)（过滤器）
- 考试分为模拟考和真实考试
  - 模拟考在:一段时间内都可以考,考试时间按照考生点击进入考试的时间计算，并且在考试结束后直接返回做题情况(细节待补充).
  - 正式考试:考试开始时间在管理员按下考试开始时统一开始计算，考试结束后返回提交成功页面。
- 要调用的函数路径为`./api/函数名`

## cookie约定

- userID:用户登录后获取的用户id
- username:用户登录后获取的用户的教师号/学号/admin
- character:用户角色student/teacher/admin

## 函数约定

### login

- 参数

  - ```json
    {
        "username":"用户名",
        "password":"md5加密后的用户密码"
    }
    ```
    
  - username：代表用户名

  - password：代表md5加密后的用户密码

- 返回值

  - ```json
    {
        "statusCode":0|1|2|3
    }
    ```

  - loginCode：代表登录状态，0代表登录失败，1代表登录成功，2代表第一次登录未修改密码，3代表重复登录


- 功能

  - 用户登入
  - 设置userID，每次用户登录后获得的userID都是随机的
  - 设置username
  - 设置character
  - 依据用户组转跳页面
  - 对于第一次登录的用户，其只能进行修改密码的操作，其他的操作可认为其未登录

### logout

- 参数
  
  - void
- 返回值
  
  - void
- 功能
  - 删除userID
  - 删除username
  - 删除character
  - 后端注销userID
  - 转跳到index.html

### changePassword

- 参数

  - ```json
    {
        "newPassword":"md5加密后的新密码"
    }
    ```

  - newPassword：代表用户md5加密后的新密码

- 返回值

  - void

- 功能

  - 设置新密码

### examStart

- 参数

  - ```
    {
    	"examType":0|1
    }
    ```

  - examType代表考试类型，0代表正式考试，1代表模拟考试

- 返回值

  - ```
    {
    	"examStart":0|1
    }
    ```

  - examStart：代表考试状态，0代表未开始，1代表开始

- 功能


  - 根据考试类型，返回对应考试状态

### getExam

- 参数

  - void

- 返回值

  - ```json
    {
        "examID":string,
        "date":string,
        "Qtype":[ number, number, number],
        "data":[
            {
                //单选
                "title":string,
                "choice":[ string, string, string, string ]
            },
            ...
            {
                //多选
                "title":string,
                "choice":[ string, string, string, string ]
            },
            ...
            {
                //判断
                "title":string
            },
            ...
        ]
    }
    ```
    
  - examID:考卷的唯一id，为随机值
  
  - date：代表考试剩余的时间,返回结束时的时间string "YYYY/MM/DD hh:mm:ss"
  
  - Qtype：分别代表单选，多选，判断的题目数量
  
  - 不同类型的题目有不同格式，data里必须严格按照单选，多选，判断的顺序排列

- 功能

  - 返回考试试题

### submitExam

- 参数

  - ```json
    {
        "Qtype":[ number, number, number],
        "data":[ number, number, ...]
    }
    ```

  - Qtype：分别代表单选，多选，判断的题目数量

  - data：代表考生选的选项，范围为0-15，使用二进制表示：

    - 0代表未选
    - 1代表A选项或正确
    - 2代表B选项或错误
    - 4代表C选项
    - 8代表D选项
    - 多选题则是选项的累加

- 返回

  - ```json
    {
        //如果是模拟考试，返回正确答案
        "Qtype":[ number, number, number],
        "data":[ number, number, ...]
    }
    ```

  - 格式同上

- 功能

  - 提交学生选项
  - 如果是模拟考则返回答案，正式考试返回null
  - 一定要用服务器数据来判断是否是模拟考，不能用cookie
  
### getClassInExam

- 参数

  - void

- 返回

  - ```json
    {
        "classNumber":number,
        "data":[
            {
                "className":string,
                "classID":sting,
                "classStatus":bool,
                "examTime":string
            }
            ...
        ]
    }
    ```

  - classNumber：班级数量

  - className：班级名称，如"软件工程三班"

  - classID：班级唯一id

  - classStatus：班级状态，是否在考试中，返回bool值

  - examTime：如果班级在考试，返回班级考试时间段，格式"YYYY/MM/DD HH:mm:ss ~ YYYY/MM/DD HH:mm:ss"

- 功能
  
  - 查询对应教师账号的所有班级考试状态

### setClassInExam

- 参数

  - ```json
    {
        "classNumber":number,
        "data":[
            {
                "classID":sting,
                "classStatus":bool,
              "examTime":string
            }
            ...
        ]
    }
    ```
    
  - classID：班级唯一id

  - classStatus：班级状态，是否在考试中，如果为true，则设置班级为考试状态

  - examTime：如果设置班级在考试，返回班级考试时间段，格式"YYYY/MM/DD HH:mm:ss ~ YYYY/MM/DD HH:mm:ss"

- 返回

  - ```json
    {
        "statusCode":0|1
    }
    ```

  - statusCode：0设置失败，1设置成功

- 功能

  - 设置指定班级的考试时间范围
  - 如果该班级考试已开放，再次设置考试时间应当返回设置失败

### uploadClassForm

- 参数

  - ```json
    //受到layui限制，数据格式为FormData
    "studentForm":上传的文件
    "className":上传的班级名称
    ```

- 返回

  - ```json
    {
        "statusCode":0|1
    }
    ```

  - statusCode：0设置失败，1设置成功

  - 对于参数不够返回失败

- 功能

  - 上传班级学生名单，为xls或xlsx

### delectClass

- 参数

  - ```json
    {
        "classNumber":number,
        "data":[string,string,...]
    }
    ```

  - classNumber:班级数量

  - data：一个包含删除班级classID的数组

- 返回

  - ```json
    {
        "statusCode":0|1
    }
    ```

  - statusCode：0设置失败，1设置成功

- 功能

  - 删除指定班级

### getStudentInfo

- 参数

  - ```json
    {
        "classID":string,
    }
    ```

  - classID：要获取的学生信息的班级id

- 返回

  - ```json
    {
        "studentNumber":number,
        "data":[
            {
                "studentName":string,
                "studentNo":number,
                "studentScore":number,
            }
        ]
    }
    ```

  - studetnName:代表学生姓名

  - studentNo：代表学生学号

  - studentScore：代表学生成绩

- 功能

  - 获取指定班级的学生信息

## 前端需求

### 主页|index.html

- [x] 登录
- [x] 现实用户信息
- [x] 主页内容

### 登录界面|login.html

- [x] 登录按钮

### 考试页面

- [ ] 考试倒计时
- [x] 时间到自动提交
- [x] 提交
- [x] 保存未提交选项
- [ ] 答题卡(优先度低)

### 学生界面

- [x] 开始正式考试
- [x] 开始模拟考试
- [x] 修改密码

### 教师界面

- [x] 修改密码
- [x] 允许指定班级考试
- [x] 结束指定班级考试
- [x] 上传指定班级学生名单
- [ ] 导出指定班级成绩
- [x] 删除指定班级

### 管理员界面

- [x] 修改密码
- [ ] 导入教师名单
- [ ] 按学号清除学生密码
- [ ] 按教师号清除教师密码
- [ ] 全部开始考试
- [ ] 全部关闭考试(关闭考试提交)
- [ ] 显示非法登录ip(轮询)

## 权限不足|permission_denied.html

- [ ] 返回按钮

## 后端需求

- 
