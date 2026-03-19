
### 1. 图书管理系统（LibraryManagementSystem）

```markdown
# 图书管理系统（前后端分离风格）

基于 **Spring Boot + MySQL + Thymeleaf + Tailwind CSS** 的现代 Web 图书管理系统，支持用户与管理员两种角色。

## 功能模块一览

### 用户端（普通读者）

- 注册 / 登录 / 登出
- 图书模糊搜索（书名 / 作者 / ISBN / 类别）
- 图书列表展示（含封面、库存状态）
- 借阅图书（需登录）
- 查看我的借阅记录（含应还日期、逾期标记）
- 归还图书

### 管理员端（/admin/*）

- 仪表盘（待完善）
- 图书管理（增删改查 + 类别选择 + ISBN 唯一校验）
- 类别管理（增删改查）
- 用户管理（查看列表、编辑基本信息、角色调整）
- 所有借阅记录查看（分页 + 排序）

## 技术栈

- **后端**：Spring Boot 3.x + Spring Data JPA + MySQL 8
- **前端**：Thymeleaf + Tailwind CSS + Font Awesome
- **数据库**：MySQL 8.0（含外键约束）
- **安全**：Session + 简单角色控制（后续可升级 Spring Security）
- **构建工具**：Maven

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- （推荐）IntelliJ IDEA

### 2. 数据库准备

1. 创建数据库 `library_db`
2. 执行项目根目录下的 SQL 脚本（或参考文档中的建表语句）
3. 修改 `src/main/resources/application.properties` 中的数据库用户名/密码

```properties
spring.datasource.username=root
spring.datasource.password=你的密码

### 3. 运行项目
## IDE 直接运行 LibraryManagementSystemApplication 主类

默认端口：8081
访问地址： 
- 前台首页：http://localhost:8081/
- 登录页：http://localhost:8081/login
- 管理后台：http://localhost:8081/admin/dashboard （需管理员账号）

- 默认账号（建议立即修改）
- 管理员：admin / 123456 （role=ADMIN）
- 普通用户：可自行注册

项目目录结构
src/main/java/com/example/librarysystem
├── config
├── controller          # 控制层（Admin / User 分开）
├── dto                 # 数据传输对象
├── entity              # JPA 实体
├── exception
├── repository
└── service

src/main/resources
├── static              # css / js / 图片
├── templates           # Thymeleaf 模板
│   ├── admin
│   ├── auth
│   ├── books
│   ├── fragments
│   └── user
└── application.properties

未来改进方向（欢迎讨论 & PR）

集成 Spring Security + JWT / OAuth2
添加图书封面上传功能
增加借阅逾期自动标记 & 罚款逻辑
添加图书预约 / 续借功能
前端升级为 Vue / React（可选）
添加日志、异常统一处理、全局响应格式


