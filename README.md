# 数据库实践仓库

- 所有请求使用post，使用json格式
- 后端在收到操作时要对操作用户的权限进行判断（建议使用动态代理）
  - 对于没登录的用户转跳到登录界面
  - 对于权限不够的用户转跳到权限不足的页面。
- 第一次登录时必须强制修改密码，不然不允许登录（不返回cookie）
- 考试分为模拟考和真实考试
  - 模拟考在:一段时间内都可以考,考试时间按照考生点击进入考试的时间计算，并且在考试结束后直接返回做题情况(细节待补充).
  - 正式考试:考试开始时间在管理员按下考试开始时统一开始计算，考试结束后返回提交成功页面。
- 要调用的函数路径为`./api/函数名`

## cookie约定

- userID:用户登录后获取的用户id
- username:用户登录后获取的用户的教师号\学号\admin
- character:用户角色student/teacher/admin

## 函数约定

- [ ] ### login


- 参数

  - ```json
    {
        "username":"用户名",
        "password":"md5加密后的用户密码"
    }
    ```

- 返回值


  - ```json
    {
        "loginCode":0|1
    }
    ```

- 功能

  - 用户登入
  - 设置userID
  - 设置username
  - 设置character
  - 依据用户组转跳页面
  - 登录成功返回1，否则返回0

- [ ] ### logout


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

## 前端需求

### 主页|index.html

- [x] 登录
- [x] 现实用户信息
- [x] 主页内容

### 登录界面|login.html

- [ ] 登录按钮
  - 调用login

### 考试页面

- [ ] 考试倒计时
- [ ] 时间到自动提交

- [ ] 提交

### 学生界面

- [ ] 开始考试
- [ ] 修改密码

### 教师界面

- [ ] 修改密码
- [ ] 允许指定班级考试
- [ ] 结束指定班级考试
- [ ] 上传指定班级学生名单
- [ ] 导出指定班级成绩
- [ ] 删除指定班级

### 管理员界面

- [ ] 修改密码
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
