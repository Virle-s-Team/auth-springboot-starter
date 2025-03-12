package cool.auv.authspringbootstarter.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.enums.ActiveStatusEnum;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class SysUserVM {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

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
    private ActiveStatusEnum status;
}
