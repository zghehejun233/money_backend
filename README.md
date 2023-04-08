# SDU-WEB课程设计

## RUN IT!

### 启动Nacos

以单机或集群的方式启动Nacos，并导入配置文件`DEFAULT_GROUP.zip`。

### 安装依赖

本项目使用Maven进行依赖管理，只需要运行
    
    ```bash
    mvn clean install
    ```

即可安装依赖。

### 启动项目

使用IDEA或者Eclipse等IDE启动项目，或者使用命令行启动项目都可以，这里通过Maven打包来运行。

执行命令
    
    ```bash
    mvn clean package
    ```

即可打包项目，打包完成后会在`target`目录下生成`web-0.0.1-SNAPSHOT.jar`文件，执行命令
    
    ```bash
    java -jar web-0.0.1-SNAPSHOT.jar
    ```

将会以默认的开发环境启动项目。在默认开发环境下，项目会连接到本地的Nacos，如果需要连接到其他Nacos，请在启动命令中添加参数
    
    ```bash
    java -jar web-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
    ```

即可连接到生产环境的Nacos。

或者，也可以使用Docker来运行项目，首先需要构建Docker镜像，执行命令
    
    ```bash
    docker build -t web .
    ```

即可构建Docker镜像，构建完成后，执行命令
    
    ```bash
    docker run -p 8080:8080 web
    ```

即可启动项目。

如果要使用配置好的其他生产环境，可以携带参数
    
    ```bash
    docker run -p 8080:8080 web --spring.profiles.active=prod
    ```

即可启动项目。

依照这种方式，可以启动多个项目，以实现负载均衡。

同时，请按照`docker-compose.yml`文件中的配置，启动MySQL、Redis、Nacos等服务，并调整相关生产环境下的配置。

最后，我们建议按照网关的方式启动项目，以实现统一的入口。

如果您要使用Docker-Compose来启动项目，请自行编写启动脚本。

### 访问项目

项目启动后，可以通过`http://localhost:8080`来访问项目。

我们的项目默认通过网关提供Swagger文档，可以通过`http://localhost:8080/swagger/index.html`来访问Swagger文档。

## 相关约定

### 用户权限

应前端要求，用户权限规定如下：

```javascript
const DEFAULT_ROLE = -1;

const LOGIN_ROLE = 0;

const TEACHER_ROLE = 1;

const STUDENT_ROLE = 2;

const ADMIN_ROLE = 3;
```

数据库内所有新用户的权限默认为`LOGIN_ROLE`即`0`，管理员可以进行权限管理。

### 全局请求过滤

过滤器配置暂时写死在过滤器`http/AccessFilter`内，后续使用`ConfigProperty`配合`Nacos`动态下发。

## 仓库规范

为了便于区分模块和Jenkins构建：

1. 禁止直接在master/main分支上开发。
2. 每个模块开一条单独的develop branch，命名使用`dev-<module name>`，后续开发需要在此基础上拉分枝后合并，再合并到主分支。

commit message需要区分一下修改的模块，格式使用：

```shell
<module name>-<type>: description
<blank line>
detail
```

`type`暂时支持：

```shell
feat: 新功能
fix: 修复
doc: 添加文档或注释
refact: 重构
chore: 其他
```

例如：

```shell
stu-feat: 添加了选课功能的实现

1. xxxx
2. xxxx
```

构建版本统一交给主分支管理，每次主分支有`0.x.0` 形式更新时需要更新各个dev分支。

## 技术细节

### 架构

本项目使用 Spring Cloud Alibaba 实现微服务，Nacos提供服务发现、服务治理和配置中心；Spring Gateway提供网关层转发、负载均衡和统一校验，聚合Swagger服务；
OpenFeign提供远程服务调用。

#### 配置中心

#### 鉴权系统

Gateway提供全局token解析和路径过滤。

#### Swagger文档中心

Gateway提供所有文档业务聚合。

### 持久化层

#### MyBatis Plus

除基本的`insert()`等方法外，均使用自定义SQL提供实现；用户信息使用后面的用户信息注入机制提供数据。

#### 数据库字段设计

数据库表参考阿里内部开发规范设计字段。

### 业务注入和AOP

#### 自定义注解注入信息

业务服务提供自定义注解，用于注入`UserInfo`信息供使用。

实现主要依靠一个全局拦截器、请求方法参数解析器和自定义注解提供支持。对指定映射路径执行拦截器判断是否有指定注解；符合要求的进行参数解析，远程调用鉴权服务提供
的接口获取信息并写入请求`Header`。

### 支撑服务

#### 邮件和短信服务

#### 文件上传服务

文件上传服务主要提供两种实现，对于头像直接上传到数据库使用`blob`类型字段保存；大文件使用阿里云COS对象存储提供服务支持。

### 系统部署与运维

#### Vercel前端部署

前端使用vercel进行部署。

#### Nginx静态网关

使用Nginx提供Gateway端口映射和负载均衡，对外暴露Swagger文档服务中心和Nacos后台系统。

#### Docker集群部署

## 参考资料

