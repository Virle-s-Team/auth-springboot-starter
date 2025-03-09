package cool.auv.authspringbootstarter.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.enums.ActiveStatusEnum;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author generator
 * @since 2024-03-09
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysUserVM implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 登录账号
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 激活状态
     */
    @Enumerated(EnumType.STRING)
    private ActiveStatusEnum status;

}
