# 数据库实践仓库

- ![image-20200821223553458](https://gitee.com/lin_haoran/Picgo/raw/master/img/image-20200821223553458.png)
- 后端在收到操作时要对操作用户的权限进行判断（过滤器）
  - 对于没登录的用户转跳到登录界面
  - 对于权限不够的用户转跳到权限不足的页面。
- 第一次登录时必须强制修改密码，不允许其他操作，在用户操作其他方法时强制下线(删除userId,username,character,注销userId)（过滤器）
- 考试分为模拟考和真实考试
  - 模拟考在:一段时间内都可以考,考试时间按照考生点击进入考试的时间计算，并且在考试结束后直接返回做题情况(细节待补充).
  - 正式考试:考试开始时间在管理员按下考试开始时统一开始计算，考试结束后返回提交成功页面。
- 要调用的函数路径为`./函数名`

## cookie约定

- userId:用户登录后获取的用户id
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
        "status":0|1|2|3
    }
    ```

  - status：代表登录状态，0代表登录失败，1代表登录成功，2代表第一次登录未修改密码，3代表重复登录


- 功能

  - 用户登入
  - 设置userId，每次用户登录后获得的userId都是随机的
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
  - 删除userId
  - 删除username
  - 删除character
  - 后端注销userId
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

### examStatus

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

### getPaper

- 参数

  - void

- 返回值

  - ```json
    {
        "paperId":string,
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
    
  - examId:考卷的唯一id，为随机值
  
  - date：代表考试剩余的时间,返回结束时的时间string "YYYY/MM/DD hh:mm:ss"
  
  - Qtype：分别代表单选，多选，判断的题目数量
  
  - 不同类型的题目有不同格式，data里必须严格按照单选，多选，判断的顺序排列

- 功能

  - 返回考试试题

### submitPaper

- 参数

  - ```json
    [
        [ number, number, number],
     	[ number, number, ...]
    ]
    ```

  - 第一个数组：分别代表单选，多选，判断的题目数量

  - 第二个数组：代表考生选的选项，范围为0-15，使用二进制表示：

    - 0代表未选
    - 1代表A选项或正确
    - 2代表B选项或错误
    - 4代表C选项
    - 8代表D选项
    - 多选题则是选项的累加

- 返回

  - ```json
    [
        [ number, number, number],
     	[ number, number, ...]
    ]
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
    [
        {
            "className":string,
            "classId":sting,
            "classStatus":0|1|2,
            "examTime":string
        }
        ...
    ]
    ```
    
  - className：班级名称，如"软件工程三班"
  
  - classId：班级唯一id
    
  - classStatus：班级状态，是否在考试中，0代表不在考试中，1代表在模拟考试，2代表在正式考试
    
  - examTime：如果班级在考试，返回班级考试时间段，格式"YYYY/MM/DD HH:mm:ss ~ YYYY/MM/DD HH:mm:ss"
  
- 功能
  
  - 查询对应教师账号的所有班级考试状态

### setClassInExam

- 参数

  - ```json
    [
        {
            "className":string,
            "classId":sting,
            "classStatus":0|1|2,
            "examTime":string
        }
        ...
    ]
    ```
    
  - className：班级名称，如"软件工程三班"
  
  - classId：班级唯一id
  
  - classStatus：班级状态，是否在考试中，0代表不在考试中，1代表在模拟考试，2代表在正式考试
  
  - examTime：如果设置班级在考试，返回班级考试时间段，格式"YYYY/MM/DD HH:mm:ss ~ YYYY/MM/DD HH:mm:ss"
  
- 返回

  - ```json
    {
        "status":0|1
    }
    ```

  - status：0设置失败，1设置成功

- 功能

  - 设置指定班级的考试时间范围
  - 如果该班级考试已开放，再次设置考试时间应当返回设置失败

### doImportStudentExcel

- 参数

  - ```json
    //受到layui限制，数据格式为FormData
    "file":上传的文件
    "className":上传的班级名称
    ```

- 返回

  - ```json
    {
        "status":0|1
    }
    ```

  - status：0设置失败，1设置成功

  - 对于参数不够返回失败

- 功能

  - 上传班级学生名单，为xls或xlsx

### delectClass

- 参数

  - ```json
    [string,string,...]
    ```
    
  - 一个包含删除班级classId的数组
  
- 返回

  - ```json
    {
        "status":0|1
    }
    ```

  - status：0设置失败，1设置成功

- 功能

  - 删除指定班级

### getStudentInfo

- 参数

  - ```json
    {
        "classId":string,
    }
    ```

  - classId：要获取的学生信息的班级id,"ALL"代表获取所有学生信息

- 返回

  - ```json
    [
        {
            "studentName":string,
            "studentNo":number,
            "studentScore":number,
        }
    ]
    ```
    
  - studetnName:代表学生姓名
  
- studentNo：代表学生学号
  
- studentScore：代表学生成绩
  
- 功能

  - 获取指定班级的学生信息

### getTeacherList

- 参数

  - void

- 返回

  - ```json
    [
        {
            "teacherId":string,
            "teacherName":sting
        }
        ...
    ]
    ```
  
- teacherId：教师唯一id

- teacherName:教师姓名

- teacherStatus：教师状态，false表示教师被删除

- 功能

  - 查询所有的教师账号

### resetPassword

* 参数

  * ```json
    [
        String
        ...
    ]
    ```

  * String:代表要重置密码的学生或教师的ID

* 返回

  * void

* 功能
  * 重置密码

### doImportQuestionExcel

* 参数

  * ```json
    //受到layui限制，数据格式为FormData
    "file":上传的文件
    ```

* 返回

  - ```json
    {
        "status":0|1
    }
    ```

  - status：0设置失败，1设置成功

  - 对于参数不够返回失败

* 功能
  * 上传题库文件

### setOfficialExam

* 功能
  * 设置正式考试

### deleteTeacher

* 参数

  * ```json
    [
        String
    ]
    ```

  * String:将要删除的教师ID

* 返回

  * void

* 功能
  * 删除教师

### doImportTeacherExcel

* 参数

  * ```json
    //受到layui限制，数据格式为FormData
    "file":上传的文件
    ```

* 返回

  - ```json
    {
        "status":0|1
    }
    ```

  - status：0设置失败，1设置成功

  - 对于参数不够返回失败

* 功能
  * 上传教师文件

### getServerIP

* 参数

  * void

* 返回

  * ```json
    {
        "IP":String,
        "Port":String
    }
    ```

* 功能

  * 返回当前服务器的IP和端口号

### 

## 前端需求

### 主页|index.html

- [x] 登录
- [x] 现实用户信息
- [x] 主页内容

### 登录界面|login.html

- [x] 登录按钮

### 考试页面

- [x] 考试倒计时
- [x] 时间到自动提交
- [x] 提交
- [x] 保存未提交选项
- [ ] 修改：题目选项增加
- [ ] ~~答题卡(优先度低)~~

### 学生界面

- [x] 开始正式考试
- [x] 开始模拟考试
- [x] 修改密码

### 教师界面

- [x] 修改密码
- [x] 允许指定班级考试
- [x] 结束指定班级考试
- [x] 上传指定班级学生名单
- [x] 导出指定班级成绩
- [x] 删除指定班级
- [ ] 设定单场考试的时间
- [ ] 设定单场考试的题目构成

### 管理员界面

- [x] 修改密码
- [ ] 导入教师名单
- [ ] 按学号清除学生密码
- [ ] 按教师号清除教师密码
- [ ] ~~全部开始考试~~
- [ ] ~~全部关闭考试(关闭考试提交)~~
- [ ] 设置正式考试时间段
- [ ] 导入考试题目
- [ ] 删除指定教师
- [ ] 删除指定学生
- [ ] ~~显示非法登录ip(轮询)~~

## 权限不足|permission_denied.html

- [x] 权限不足弹窗

## 后端需求
