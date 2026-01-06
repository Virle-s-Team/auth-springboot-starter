# Auth Spring Boot Starter 快速使用指南

基于 `auth-spring-boot-starter` 和 `code-generator-jpa` 快速开发企业级应用。

---

## 目录

1. [项目简介](#项目简介)
2. [前置条件](#前置条件)
3. [安装依赖](#安装依赖)
4. [创建项目](#创建项目)
5. [配置项目](#配置项目)
6. [开发实体](#开发实体)
7. [生成代码](#生成代码)
8. [测试运行](#测试运行)
9. [核心功能](#核心功能)
10. [配置参考](#配置参考)

---

## 项目简介

**Auth Spring Boot Starter** 是一个开箱即用的 Spring Boot Starter，提供：

- ✅ **用户认证授权**：基于 JWT + Spring Security
- ✅ **代码自动生成**：通过 `@AutoEntity` 注解生成全套 CRUD 代码
- ✅ **多租户支持**：自动租户隔离
- ✅ **权限管理**：用户-角色-权限完整体系
- ✅ **API 文档**：集成 Swagger/OpenAPI

**技术栈**：Spring Boot 3.4.3、Java 21、MySQL 8.0+

---

## 前置条件

- **JDK 21+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **IDE**：IDEA / Eclipse / VS Code

---

## 安装依赖

首先需要将依赖安装到本地 Maven 仓库：

```bash
# 1. 安装 code-generator-jpa
cd D:/workspace/java/code-generator-jpa
mvn clean install

# 2. 安装 auth-spring-boot-starter
cd D:/workspace/java/auth-springboot-starter
mvn clean install
```

---

## 创建项目

### 方式一：Spring Initializr（推荐）

1. 访问 https://start.spring.io/
2. 填写项目信息：
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.4.3
   - **Java**: 21
   - **Group**: `com.example`
   - **Artifact**: `demo`
3. 添加依赖：`Spring Web`、`Spring Data JPA`、`MySQL Driver`
4. 下载并解压

### 方式二：命令行创建

```bash
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,mysql \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=3.4.3 \
  -d groupId=com.example \
  -d artifactId=demo \
  -d javaVersion=21 \
  -d baseDir=demo -o demo.zip

unzip demo.zip && cd demo
```

---

## 配置项目

### 1. 修改 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.3</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <!-- Auth Starter（包含认证、代码生成、多租户） -->
        <dependency>
            <groupId>cool.auv</groupId>
            <artifactId>auth-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!-- MySQL 驱动 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <encoding>UTF-8</encoding>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>cool.auv</groupId>
                            <artifactId>code-generator-jpa</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. 创建数据库

```sql
CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置 application.yml

```yaml
spring:
  application:
    name: demo

  # 数据源配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_db?characterEncoding=UTF-8&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2

  # JPA 配置
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    show-sql: true

  # JWT 配置
  security:
    authentication:
      jwt:
        secret: VGhpc0lzQVNlY3JldEtleUZvckpXdFZlcmlmaWNhdGlvbldpdGhCYXNlNjRFbmNvZGluZw==
        expiration: 604800000  # 7天

# 应用配置
app:
  reset-password: 123456
  default-tenantId: default

# 日志配置
logging:
  level:
    cool.auv: DEBUG

# Swagger 文档
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  api-docs:
    path: /v3/api-docs
    enabled: true
```

### 4. 创建 generator.yml

在 `src/main/resources/` 下创建 `generator.yml`：

```yaml
auto-processor:
  entity:
    package-name: com.example.demo.entity
  vm:
    package-name: com.example.demo.vm
  request:
    package-name: com.example.demo.vm.request
  repository:
    package-name: com.example.demo.repository
  mapstruct:
    package-name: com.example.demo.mapstruct
  service:
    package-name: com.example.demo.service
    impl-package-name: com.example.demo.service.impl
  controller:
    package-name: com.example.demo.controller
```

---

## 开发实体

### 创建第一个实体类

创建 `src/main/java/com/example/demo/entity/Product.java`：

```java
package com.example.demo.entity;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 商品实体
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "product")
@AutoEntity(
    basePath = "/api/v1/product",
    docTag = "商品管理"
)
public class Product extends BaseEntity {

    /**
     * 商品名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 价格（分）
     */
    @Column(name = "price", nullable = false)
    private Long price;

    /**
     * 库存
     */
    @Column(name = "stock")
    private Integer stock;

    /**
     * 描述
     */
    @Column(name = "description", length = 500)
    private String description;
}
```

**关键点**：
- 继承 `BaseEntity`：自动包含 id、createdAt、updatedAt 字段
- 添加 `@AutoEntity` 注解：告诉代码生成器生成 CRUD 代码
- `basePath`：API 基础路径
- `docTag`：Swagger 文档分组名称

---

## 生成代码

### 编译项目

```bash
mvn clean compile
```

编译成功后，会在 `target/generated-sources/annotations/` 自动生成：

```
target/generated-sources/annotations/
└── com/example/demo/
    ├── controller/
    │   └── BaseProductController.java    # REST API
    ├── service/
    │   ├── BaseService.java             # 服务接口
    │   └── impl/
    │       └── BaseProductServiceImpl.java
    ├── repository/
    │   └── ProductRepository.java        # JPA Repository
    └── mapstruct/
        └── ProductMapper.java            # 对象映射
```

### 生成的 API 端点

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/v1/product` | 创建商品 |
| PUT | `/api/v1/product` | 更新商品 |
| GET | `/api/v1/product/{id}` | 根据 ID 查询 |
| GET | `/api/v1/product/findByPage` | 分页查询 |
| DELETE | `/api/v1/product/{id}` | 删除商品 |

---

## 测试运行

### 1. 启动项目

```bash
mvn spring-boot:run
```

### 2. 访问 Swagger 文档

打开浏览器：http://localhost:8080/swagger-ui.html

### 3. 创建测试用户

```sql
INSERT INTO sys_user (username, password, real_name, tenant_id, created_at, updated_at)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Ik50pcQbPEVkbUvZZV6JNFfj3mM42q', '管理员', 'default', NOW(), NOW());
```

### 4. 登录获取 Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

### 5. 使用 Token 创建商品

```bash
curl -X POST http://localhost:8080/api/v1/product \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name":"iPhone","price":799900,"stock":100}'
```

---

## 核心功能

### 1. 认证授权

#### 登录

```bash
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "123456"
}
```

#### 使用 Token

所有需要认证的接口都需要在 Header 中携带：

```
Authorization: Bearer {token}
```

#### 获取当前用户

```java
import cool.auv.authspringbootstarter.utils.SecurityContextUtil;
import cool.auv.authspringbootstarter.entity.SysUser;

@RestController
public class MyController {

    @GetMapping("/api/v1/current-user")
    public SysUser getCurrentUser() {
        return SecurityContextUtil.getCurrentUser()
            .orElseThrow(() -> new RuntimeException("未登录"));
    }
}
```

### 2. 多租户

#### 启用多租户

继承 `TenantBaseEntity` 而非 `BaseEntity`：

```java
import cool.auv.codegeneratorjpa.core.entity.tenant.TenantBaseEntity;

@Entity
@Table(name = "product")
@AutoEntity(basePath = "/api/v1/product", docTag = "商品管理")
public class Product extends TenantBaseEntity {  // 继承多租户基类
    private String name;
    // ...
}
```

#### 租户自动处理

- **创建**：自动从 Token 提取租户 ID
- **查询**：自动过滤当前租户数据
- **更新/删除**：自动验证租户权限

#### 获取当前租户 ID

```java
import cool.auv.authspringbootstarter.config.TenantContext;

String tenantId = TenantContext.getTenantId();
```

### 3. 自定义业务

#### 自定义 Controller

```java
@RestController
@RequestMapping("/api/v1/custom/product")
public class ProductController extends BaseProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public List<Product> search(@RequestParam String name) {
        return productService.searchByName(name);
    }
}
```

#### 排除生成的接口

```java
@AutoEntity(
    basePath = "/api/v1/product",
    docTag = "商品管理",
    controllerExclude = {
        AutoEntity.ControllerExclude.save,     // 不生成创建接口
        AutoEntity.ControllerExclude.update    // 不生成更新接口
    }
)
public class Product extends BaseEntity {
    // ...
}
```

---

## 配置参考

### generator.yml 配置

| 配置项 | 说明 | 示例 |
|--------|------|------|
| `entity.package-name` | 实体类所在包 | `com.example.demo.entity` |
| `controller.package-name` | 生成 Controller 的包 | `com.example.demo.controller` |
| `service.package-name` | 生成 Service 的包 | `com.example.demo.service` |
| `service.impl-package-name` | 生成 Service 实现的包 | `com.example.demo.service.impl` |
| `repository.package-name` | 生成 Repository 的包 | `com.example.demo.repository` |
| `mapstruct.package-name` | 生成 Mapper 的包 | `com.example.demo.mapstruct` |
| `request.package-name` | 生成 Request 的包 | `com.example.demo.vm.request` |

**注意**：`entity.extend-from` 不需要配置，实体在代码中直接继承即可。

### application.yml 配置

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `spring.datasource.url` | 数据库连接地址 | - |
| `spring.datasource.username` | 数据库用户名 | - |
| `spring.datasource.password` | 数据库密码 | - |
| `spring.jpa.hibernate.ddl-auto` | DDL 模式 | `update` |
| `spring.security.authentication.jwt.secret` | JWT 密钥（Base64） | - |
| `spring.security.authentication.jwt.expiration` | Token 过期时间（毫秒） | `604800000` |
| `app.reset-password` | 默认密码 | `123456` |
| `app.default-tenantId` | 默认租户 ID | `default` |

---

## 常见问题

### Q1：编译失败 "找不到 auth-spring-boot-starter"

**原因**：依赖未安装到本地仓库

**解决**：
```bash
cd D:/workspace/java/auth-springboot-starter
mvn clean install
```

### Q2：代码没有生成

**原因**：
1. 注解处理器未配置
2. 实体类没有 `@AutoEntity` 注解
3. 包名配置不匹配

**解决**：检查 `pom.xml` 的 `annotationProcessorPaths` 配置

### Q3：如何重置用户密码？

调用重置接口（需要认证）：

```bash
PUT /api/v1/sys-user/resetPassword/{userId}
```

密码会被重置为 `app.reset-password` 配置的值。

### Q4：生产环境如何配置？

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # 使用 validate，不用 update
  datasource:
    hikari:
      maximum-pool-size: 20
```

```yaml
springdoc:
  swagger-ui:
    enabled: false  # 生产环境禁用 Swagger
```

### Q5：如何修改 Token 过期时间？

```yaml
spring:
  security:
    authentication:
      jwt:
        expiration: 604800000  # 7天
```

### Q6：如何禁用多租户？

实体继承 `BaseEntity` 而非 `TenantBaseEntity`。

---

## 项目结构

```
demo/
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java           # 启动类
│   │   ├── entity/
│   │   │   └── Product.java               # 实体类
│   │   └── controller/
│   │       └── CustomController.java      # 自定义 Controller
│   └── resources/
│       ├── application.yml                # 配置文件
│       └── generator.yml                  # 代码生成配置
└── target/generated-sources/annotations/  # 自动生成的代码
```

---

## 下一步

- 📖 阅读 API 文档：http://localhost:8080/swagger-ui.html
- 🔐 添加更多实体：重复「开发实体」步骤
- 🎨 自定义权限：使用 `SysPermission` 配置权限控制
- 📊 多租户开发：继承 `TenantBaseEntity`

---

## 技术支持

- 项目仓库：https://github.com/your-org/auth-springboot-starter
- 问题反馈：提交 Issue
