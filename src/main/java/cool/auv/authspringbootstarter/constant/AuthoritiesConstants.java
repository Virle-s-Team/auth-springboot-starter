package cool.auv.authspringbootstarter.constant;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    // 认证token
    public static final String AUTHENTICATED_TOKEN = "AUTHENTICATED";

    // 鉴权token
    public static final String AUTHORIZED_TOKEN = "AUTHORIZED";

    /***********token 包含信息 begin **********************/
    public static final String LOGIN_ACCOUNT_ID = "userId";
    public static final String LOGIN_ACCOUNT_NAME = "userName";

    public static final String LOGIN_ACCOUNT_TYPE = "loginType";

    public static final String LOGIN_ACCOUNT_TENANT_ID = "tenantId";

    public static final String LOGIN_ACCOUNT_CAMPUS_ID = "campusId";

    public static final String TOKEN_TYPE = "tokenType";

    public static final String PENETRATION = "penetration";

    /***********token 包含信息 end **********************/
    private AuthoritiesConstants() {
    }
}
