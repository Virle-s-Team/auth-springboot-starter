# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个 **Spring Boot Starter** 项目，提供两大核心功能：

1. **认证授权模块**：基于 JWT 和 Spring Security 的完整用户、角色、权限管理系统
2. **代码生成器**：集成 `code-generator-jpa`，通过 `@AutoEntity` 注解自动生成 CRUD 后端代码

**技术栈**：Spring Boot 3.4.3、Spring Security、Spring Data JPA、JWT (jjwt 0.12.6)、SpringDoc OpenAPI 2.8.5
**Java 版本**：17

## 构建与开发命令

```bash
# 编译项目（会触发代码生成器）
./mvnw clean compile

# 打包
./mvnw clean package

# 安装到本地仓库
./mvnw clean install

# 跳过测试打包（当前默认配置）
./mvnw clean package -DskipTests
```

**注意**：项目中默认配置了 `<skipTests>true</skipTests>`，测试会被跳过。

## 核心架构

### 双 FilterChain 安全配置

项目在 `SecurityAutoConfiguration` 中配置了两个 `SecurityFilterChain`：

1. **Order(1) - 公开链**：匹配 `/api/v1/auth/**`、`/swagger-ui/**`、`/v3/api-docs/**`，无需认证
2. **Order(2) - 认证链**：其他所有请求需要 JWT 认证，在 `UsernamePasswordAuthenticationFilter` 之前添加 `JWTFilter`

### 多租户架构

- **TenantContext**：使用 `ThreadLocal` 管理当前请求的租户 ID
- **TenantIdFilter**：在认证链中从请求提取租户信息并设置到 TenantContext
- **TenantBaseEntity**：所有核心实体（SysUser、SysRole）继承此类，自动支持多租户
- **TokenProvider**：JWT Claims 中包含 `LOGIN_ACCOUNT_TENANT_ID`，解析时自动设置租户上下文

### 实体关系设计

```
SysUser (用户) ←→ SysRole (角色) [多对多，通过 sys_user_role]
SysUser (用户) ←→ SysPermission (权限) [多对多，通过 sys_user_permission]
SysRole (角色) ←→ SysPermission (权限) [多对多，通过 sys_role_permission]
```

**权限聚合**：`SysUser.getAllPermission()` 合并用户直接权限 + 角色继承权限

### 代码生成器配置

代码生成规则由 `src/main/resources/generator.yml` 定义：

```yaml
auto-processor:
  entity:
    package-name: cool.auv.authspringbootstarter.entity
    extend-from: cool.auv.gen.entity.BaseEntity
  vm: cool.auv.authspringbootstarter.vm
  request: cool.auv.authspringbootstarter.vm.request
  repository: cool.auv.authspringbootstarter.repository
  service: cool.auv.authspringbootstarter.service
  controller: cool.auv.authspringbootstarter.controller
```

### 自动配置

Spring Boot 自动配置通过 `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 激活：
- `SecurityAutoConfiguration`
- `EnumAutoConfiguration`

## 使用 @AutoEntity 注解

为实体添加 `@AutoEntity` 注解后，编译时会自动生成全套 CRUD 代码：

```java
@AutoEntity(
    basePath = "/api/v1/sys-user",
    docTag = "用户管理",
    controllerExclude = {AutoEntity.ControllerExclude.save, AutoEntity.ControllerExclude.update}
)
public class SysUser extends TenantBaseEntity { ... }
```

生成的标准 API 端点：
- `POST {basePath}` - 创建
- `PUT {basePath}` - 更新
- `GET {basePath}/{id}` - 根据 ID 查询
- `GET {basePath}/findByPage` - 分页查询（支持 Request 类字段的动态查询）
- `DELETE {basePath}/{id}` - 删除

生成的代码位于 `target/generated-sources/`，包括：
- `Base{Entity}Controller` - 基础控制器
- `Base{Entity}Service` - 基础服务接口
- `Base{Entity}ServiceImpl` - 基础服务实现
- `{Entity}Repository` - JPA Repository
- `{Entity}Mapper` - MapStruct 映射器

## JWT Token 处理

**TokenProvider** 职责：
- 创建 JWT：`createJWT(Map<String, Object> claims)`
- 验证 JWT：`validateToken(String authToken, HttpServletRequest request)`
- 解析认证：`getAuthentication(String token)` - 从 Token 中提取用户 ID 和租户 ID，加载用户权限

**密钥处理**：
- 支持 Base64 编码或纯文本密钥
- 最小长度 32 字节（HS256 算法要求）
- 配置路径：`spring.security.authentication.jwt.secret`

## API 路径约定

- 认证相关：`/api/v1/auth/**`（公开）
- 实体管理：`/api/v1/{entity-name}`（需认证）
- Swagger 文档：`/swagger-ui/**`（公开）
- OpenAPI Spec：`/v3/api-docs/**`（公开）

## 重要依赖

### 自身核心依赖

- **Spring Boot Starter Security**：认证授权核心框架
- **Spring Boot Autoconfigure**：自动配置支持
- **JWT (jjwt 0.12.6)**：Token 生成与验证（api/impl/jackson 三个模块）
- **SpringDoc OpenAPI 2.8.5**：API 文档生成（springdoc-openapi-starter-webmvc-ui）

### 来自 code-generator-jpa 的传递依赖

**框架与数据库**：
- **Spring Boot Starter Data JPA**：JPA 数据访问
- **MySQL Connector 8.0.33**：MySQL 数据库驱动
- **Spring Context/Web/WebMVC/Beans 6.2.3**：Spring 核心模块（显式版本）

**工具库**：
- **Apache Commons Lang3 3.17.0**：通用工具类
- **JetBrains Annotations 24.0.1**：IDE 注解支持
- **Jackson (Databind 2.18.2 / Annotations 2.18.2 / Dataformat YAML 2.17.2)**：JSON/YAML 处理
- **Swagger Annotations 2.2.28**：OpenAPI 3 注解（用于生成代码中的 API 文档）
- **Logback Classic 1.5.16**：日志实现

**代码生成相关**：
- **Auto Service 1.0**（Google）：注解处理器自动注册
- **Auto Value 1.7.4**（Google）：不可变对象生成

### 注解处理器配置

**已配置在 maven-compiler-plugin 中**：
- **code-generator-jpa 0.0.1-SNAPSHOT**：核心代码生成注解处理器
- **Lombok 1.18.24**：代码简化注解处理器
- **MapStruct Processor 1.5.5.Final**：对象映射注解处理器
- **hibernate-jpamodelgen**：JPA Metamodel 生成器

**注意**：
- 使用本 Starter 的项目无需重复配置上述注解处理器
- code-generator-jpa 内部已包含 auto-service、mapstruct 等注解处理器配置

## 配置文件位置

- `application.yml`：应用配置（数据源、JPA、JWT 过期时间）
- `generator.yml`：代码生成器配置（包路径、基类）
- 自动配置导入：`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## 开发注意事项

1. **编译触发代码生成**：每次 `mvn clean compile` 会重新生成 `@AutoEntity` 标记实体的代码
2. **多租户字段**：新增实体如需多租户支持，继承 `TenantBaseEntity` 而非普通 `BaseEntity`
3. **JWT 过期时间**：默认 1800000ms（30分钟），可通过 `spring.security.authentication.jwt.expiration` 覆盖
4. **密码重置默认值**：`app.reset-password=123456`
5. **默认租户 ID**：`app.default-tenantId=default-tenant`
