package cool.auv.authspringbootstarter.constant;

public interface CommonConstant {

    /**
     * 正常状态
     */
    public static final Boolean STATUS_NORMAL = false;

    /**
     * 禁用状态
     */
    public static final Boolean STATUS_DISABLE = true;

    /**
     * 删除标志
     */
    public static final Boolean DEL_FLAG_1 = true;

    /**
     * 未删除
     */
    public static final Boolean DEL_FLAG_0 = false;

    /**
     * 系统日志类型： 登录
     */
    public static final int LOG_TYPE_CLIENT_LOGIN = 1;

    /**
     * 系统日志类型： 操作
     */
    public static final int LOG_TYPE_CLIENT_OPERATE = 2;

    public static final int LOG_TYPE_ADMIN_LOGIN = 3;

    /**
     * 系统日志类型： 操作
     */
    public static final int LOG_TYPE_ADMIN_OPERATE = 4;

    /**
     * 操作日志类型： 查询
     */
    public static final int OPERATE_TYPE_1 = 1;

    /**
     * 操作日志类型： 添加
     */
    public static final int OPERATE_TYPE_2 = 2;

    /**
     * 操作日志类型： 更新
     */
    public static final int OPERATE_TYPE_3 = 3;

    /**
     * 操作日志类型： 删除
     */
    public static final int OPERATE_TYPE_4 = 4;

    /**
     * 操作日志类型： 倒入
     */
    public static final int OPERATE_TYPE_5 = 5;

    /**
     * 操作日志类型： 导出
     */
    public static final int OPERATE_TYPE_6 = 6;


    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 403
     */
    public static final Integer SC_NO_AUTHZ = 403;

    /**
     * 登录用户Token令牌缓存KEY前缀
     */
    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";

    public static final String PREFIX_CLIENT_USER_TOKEN = "prefix_client_user_token_";


    public final static String X_ACCESS_TOKEN = "Authorization";
    public final static String X_SIGN = "X-Sign";
    public final static String X_TIMESTAMP = "X-TIMESTAMP";

    public final static String TENANT_HEADQUARTERS_ID = "headquarters";
    public final static String TENANT_HEADQUARTERS_AME = "总部";
    long SCHEDULED_DELAY = 1000 * 60 * 3;
}
