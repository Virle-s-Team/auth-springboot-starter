package cool.auv.authspringbootstarter.vm.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import cool.auv.authspringbootstarter.entity.SysUser;
import cool.auv.authspringbootstarter.entity.SysUser_;
import cool.auv.authspringbootstarter.enums.ActiveStatusEnum;
import cool.auv.authspringbootstarter.enums.GenderEnum;
import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class SysUserRequest implements RequestInterface<SysUser> {


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
    private String realName;

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


    @Override
    public Specification<SysUser> buildSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotEmpty(realName)) {
                predicateList.add(criteriaBuilder.like(root.get(SysUser_.REAL_NAME), "%" + this.realName + "%"));
            }
            if (StringUtils.isNotEmpty(phone)) {
                predicateList.add(criteriaBuilder.like(root.get(SysUser_.PHONE), "%" + this.phone + "%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
