# 多人分享博客平台
[![Build Status](https://www.travis-ci.org/chelegen/pipemm-blog.svg?branch=master)](https://www.travis-ci.org/chelegen/pipemm-blog)

这是基于Spring Boot的多人博客平台的后端实现
- 效果展示：[在线预览](http://139.196.161.65/)
- 从零开始搭建博客需要的材料：[前后端接口约定文档](https://github.com/chelegen/pipemm-blog/blob/master/%E5%89%8D%E5%90%8E%E7%AB%AF%E6%8E%A5%E5%8F%A3%E7%BA%A6%E5%AE%9A.md) + [前端页面代码](https://github.com/jirengu-inc/vue-blog-preview)

### 项目结构
- Controller层：处理接收到的HTTP请求，对获取的请求参数进行验证清洗，并将参数传递给业务逻辑Service层。
- Service层：主要处理业务逻辑的方法实现，依赖于Dao层对数据库的操作。
- Dao层：提供访问数据库所需操作的方法，Dao的优势在于它实现了两次隔离：隔离了数据访问代码和业务逻辑代码，隔离了不同的数据源实现。
- Entity：主要存放各个实体类。
- Configuration：用于存放WebSecurity配置。


### 自动化测试
- **代码质量** <br>
在Maven的compile周期绑定了CheckStyle插件，在verify周期绑定了SpotBugs插件，以保证代码的质量。

- **单元测试** <br>
提供对Dao层、Service层和Controller层的Junit单元测试，使用Mockito mock相关依赖对象的行为，在不涉及依赖关系的情况下完成对代码的测试。

- **集成测试** <br>
对整个项目对外暴露的登录接口进行集成测试，使用httpClient模拟发送http请求，验证用户的正常登录操作以及登陆状态的维持情况。使用Flyway实现在每次测试前自动启动一个H2数据库来存储临时的测试数据，之后能够使用Flyway来自动对数据库进行初始化，测试完自动销毁测试数据库，为项目对外接口的功能提供保障。

- **自动化测试**<br>
配置TravisCI对Github仓库管理，每次commit都将自动测试代码，保证项目可演进性。

    
### How to Build
clone项目至本地目录：
```
git clone https://github.com/chelegen/pipemm-blog.git
```
从Docker启动MySQL数据库：
- [官方文档：如何使用Docker](https://docs.docker.com/get-started/)
- [个人博客：Docker的使用](https://www.cnblogs.com/pipemm/p/12300761.html)
```
docker run --name mysql -v `pwd`/docker/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql:8.0.18
```
数据库初始化
- Flyway不支持自动创建数据库 (初始化的前提，必须有这个数据库)
```
mvn flyway:migrate
```
项目测试
- 测试日志过长可能会使进程崩坏，可将日志重定向到文件中
```
mvn verify > Log.txt
```
运行项目
- 运行
- 访问```localhost:8080```

项目部署
- 可根据需求自行查询资料
- [个人博客：如何部署项目](https://www.cnblogs.com/pipemm/p/12308373.html)



---
博客地址：[嗑瓜子的猫-博客园](https://www.cnblogs.com/pipemm/)
，分享了一些流程和遇到的问题等
