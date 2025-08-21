package cool.auv.authspringbootstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "cool.auv.auth")
public class EnumProperties {

    private final List<String> enumScanPackages = new ArrayList<>();

    public List<String> getEnumScanPackages() {
        return enumScanPackages;
    }
}