# Auth Spring Boot Starter

一个基于Spring Boot的认证授权启动器，提供JWT token认证、权限控制等功能。

## 功能特性

- 基于JWT的token认证
- 基于Spring Security的权限控制
- 支持自定义认证逻辑
- 支持自定义权限验证
- 提供完整的用户认证流程
- 支持数据库持久化
- 提供RESTful API接口
- 集成Swagger文档

## 技术栈

- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- Lombok
- MapStruct
- SpringDoc OpenAPI (Swagger)

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+

### 添加依赖

在你的`pom.xml`中添加以下依赖：

```xml
<dependency>
    <groupId>cool.auv</groupId>
    <artifactId>auth-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 配置说明

在`application.yml`中添加以下配置：

```yaml
auth:
  jwt:
    secret: your-secret-key
    expiration: 86400000  # 24小时
  security:
    enabled: true
```

## 使用示例

### 1. 用户认证

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
```

### 2. 权限控制

```java
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    // 控制器方法
}
```

## 项目结构

```
src/main/java/cool/auv/authspringbootstarter/
├── aspect/          # 切面类
├── config/          # 配置类
├── constant/        # 常量定义
├── controller/      # 控制器
├── entity/         # 实体类
├── enums/          # 枚举类
├── mapstruct/      # 对象映射
├── service/        # 服务层
├── utils/          # 工具类
└── vm/             # 视图模型
```

## 开发计划

- [ ] 添加OAuth2支持
- [ ] 添加多租户支持
- [ ] 添加缓存支持
- [ ] 添加更多的安全特性

## 贡献指南

欢迎提交Issue和Pull Request来帮助改进这个项目。