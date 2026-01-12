package cool.auv.authspringbootstarter.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置属性
 */
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /**
     * 公开访问的路径列表
     */
    private List<String> publicPaths = new ArrayList<>();

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
