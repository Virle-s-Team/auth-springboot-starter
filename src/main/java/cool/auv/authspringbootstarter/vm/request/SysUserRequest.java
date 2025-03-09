package cool.auv.authspringbootstarter.vm.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.Specification;
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
public class SysUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
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
     * 机构编码
     */
    private String orgCode;

    /**
     * 是否冻结(1-正常,2-冻结)
     */
    private Boolean status;

    /**
     * 第三方登录的唯一标识
     */
    private String thirdId;

    /**
     * 第三方类型
     */
    private String thirdType;

    /**
     * 同步工作流引擎(1-同步,0-不同步)
     */
    private Boolean activitiSync;

    /**
     * 工号，唯一键
     */
    private String workNo;

    /**
     * 职务，关联职务表
     */
    private String post;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 身份(1普通成员 2上级）
     */
    private Boolean userIdentity;

    /**
     * 负责部门
     */
    private String departIds;

    /**
     * 多租户标识
     */
    private String relTenantIds;

    /**
     * 设备ID
     */
    private String clientId;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 微信唯一识别
     */
    private String wxOpenid;

    /**
     * 用户等级
     */
    private Integer memberLevel;

    /**
     * 用户类别
     */
    private String memberType;

    /**
     * 删除状态(0-正常,1-已删除)
     */
    private Boolean isDeleted;

    public Specification<SysUser> buildSpecification() {
        return null;
    }
}
