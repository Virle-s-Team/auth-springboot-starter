# Auth Spring Boot Starter

这是一个功能强大的 Spring Boot Starter，旨在为您的新项目提供**双重加速**：

1.  **开箱即用的认证授权**：集成了完整的用户、角色、权限管理功能，基于 JWT 和 Spring Security，即插即用。
2.  **内建业务代码生成器**：无缝集成了 `code-generator-jpa`。您只需为一个新的 JPA 实体（如“商品”、“订单”）添加一个注解，即可在编译时自动生成全套生产级的后端代码 (Controller, Service, Repository)。

使用本 Starter，您不仅能省去搭建权限系统的重复工作，更能极大地提升业务功能的开发效率。

## 核心功能

*   **认证授权模块**
    *   基于 JWT 的无状态 Token 认证。
    *   完整的用户、角色、权限（菜单）管理 RESTful API。
    *   基于 Spring Security 的精细化接口权限控制。
    *   开箱即用的 `CurrentUserController`，方便获取当前登录用户信息。
    *   集成了 Swagger (SpringDoc)，API 文档自动生成。

*   **代码生成模块**
    *   **零配置代码生成**：为任意 JPA 实体添加 `@AutoEntity` 注解，自动生成 CRUD 后端代码。
    *   **约定优于配置**：只需提供 `Entity`, `VM`, `Request` 三个核心类，其余自动搞定。
    *   **高度可定制**：可通过 `generator.yml` 自定义生成代码的包路径和基类。
    *   **无缝集成**：您无需关心注解处理器的配置，本 Starter 已全部为您处理好。

## 快速开始

### 第1步：添加依赖

在您的 `pom.xml` 中添加本 Starter 的依赖。

```xml
<dependency>
    <groupId>cool.auv</groupId>
    <artifactId>auth-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version> <!-- 请使用最新版本 -->
</dependency>
```
**重要**：本 Starter 已经为您配置好了 `maven-compiler-plugin` 和所有必需的注解处理器（包括 `code-generator-jpa` 和 `mapstruct`）。您**无需**再手动添加 `annotationProcessorPaths`。

### 第2步：配置数据库和JWT

在您的 `application.yml` 中配置数据库连接和 JWT 密钥。

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password

# JWT 配置
auth:
  jwt:
    secret: 'your-super-secret-key-that-is-long-enough' # 强烈建议使用一个复杂的密钥
    expiration: 86400000  # Token 有效期，单位毫秒 (例如: 24小时)
```

### 第3步：启动项目

启动您的 Spring Boot 应用。JPA 会自动根据本 Starter 中定义的 `SysUser`, `SysRole` 等实体在您的数据库中创建相应的表。

您现在已经拥有了一套完整的权限管理API，例如：
*   `POST /api/v1/auth/login`：用户登录。
*   `GET /api/v1/sys-user`：分页查询用户。
*   `POST /api/v1/sys-role`：创建新角色。
*   ... 更多接口请参考启动后生成的 Swagger 文档。

## 使用代码生成器开发新功能

这是本 Starter 的核心魅力所在。假设您需要开发一个“商品管理”功能。

**1. 创建核心类**

首先，手动创建 `Product` 实体、`ProductVM` 视图模型和 `ProductRequest` 查询模型。

*   `Product.java` (Entity)
    ```java
    package com.yourcompany.project.domain;
    import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
    import jakarta.persistence.*;
    import lombok.Data;

    @Data
    @Entity
    @AutoEntity // <-- 核心注解！
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private Double price;
    }
    ```

*   `ProductVM.java` (View Model)
    ```java
    package com.yourcompany.project.vm;
    import lombok.Data;

    @Data
    public class ProductVM {
        private Long id;
        private String name;
        private Double price;
    }
    ```

*   `ProductRequest.java` (Request Model)
    ```java
    package com.yourcompany.project.vm.request;
    import cool.auv.codegeneratorjpa.core.service.RequestInterface;
    import com.yourcompany.project.domain.Product;
    import lombok.Data;
    // ... import other necessary classes

    @Data
    public class ProductRequest implements RequestInterface<Product> {
        private String name;
        // 实现 buildSpecification() 方法来支持动态查询
        @Override
        public Specification<Product> buildSpecification() { ... }
    }
    ```

**2. 编译项目**

使用 Maven 编译您的项目：
```bash
mvn clean compile
```

**完成！**

编译后，`code-generator-jpa` 会自动在 `target/generated-sources` 目录下为您生成 `BaseProductController`, `BaseProductService` 等全套代码。您的项目现在已经拥有了一套完整的 `/api/v1/product` 的 CRUD API。

#### 生成的标准API端点

假设您在`@AutoEntity`注解中设置了`basePath`，例如 `basePath = "/api/v1/product"`，那么生成的标准接口如下：

##### 1. 创建实体 (Create)
*   **方法**: `POST`
*   **路径**: `{basePath}`
*   **请求体**: 一个对应实体VM的JSON对象，`id`字段为空。
*   **成功响应**: `200 OK`

##### 2. 更新实体 (Update)
*   **方法**: `PUT`
*   **路径**: `{basePath}`
*   **请求体**: 一个对应实体VM的JSON对象，`id`字段有值。
*   **成功响应**: `200 OK`

##### 3. 根据ID查询 (Find by ID)
*   **方法**: `GET`
*   **路径**: `{basePath}/{id}`
*   **成功响应**: `200 OK`，响应体为对应实体的VM对象。

##### 4. 分页和条件查询 (Find by Page)
*   **方法**: `GET`
*   **路径**: `{basePath}/findByPage`
*   **查询参数**:
    *   来自`Request`类的所有字段 (用于条件查询)。
    *   `page=0&size=10&sort=id,desc` (用于分页和排序)。
*   **成功响应**: `200 OK`，响应体为VM对象的数组，响应头中包含分页信息。

##### 5. 根据ID删除 (Delete)
*   **方法**: `DELETE`
*   **路径**: `{basePath}/{id}`
*   **成功响应**: `200 OK`

### (可选) 自定义生成路径

如果您想改变代码生成的包路径，只需在 `src/main/resources` 下创建 `generator.yml` 文件并进行配置即可。本 Starter 会自动加载它。
