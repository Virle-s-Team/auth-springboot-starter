package cool.auv.authspringbootstarter.service;

import cool.auv.authspringbootstarter.config.EnumProperties;
import cool.auv.codegeneratorjpa.core.enums.ISelectable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnumService {

    private static final Logger log = LoggerFactory.getLogger(EnumService.class);
    private static final String BASE_PACKAGE = "cool.auv.authspringbootstarter.enums";

    private final Map<String, Class<? extends Enum>> enumMap = new ConcurrentHashMap<>();
    private final EnumProperties enumProperties;

    public EnumService(EnumProperties enumProperties) {
        this.enumProperties = enumProperties;
    }

    @PostConstruct
    public void init() {
        log.debug("Scanning for ISelectable enums");
        Set<String> packagesToScan = new HashSet<>(enumProperties.getEnumScanPackages());
        packagesToScan.add(BASE_PACKAGE);

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(ISelectable.class));

        for (String packageToScan : packagesToScan) {
            for (BeanDefinition bd : provider.findCandidateComponents(packageToScan)) {
                try {
                    Class<?> clazz = Class.forName(bd.getBeanClassName());
                    if (clazz.isEnum()) {
                        log.info("Found ISelectable enum: {}", clazz.getName());
                        enumMap.putIfAbsent(clazz.getSimpleName(), (Class<? extends Enum>) clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Error loading class for enum scanning", e);
                }
            }
        }
    }

    public Class<? extends Enum> findEnum(String simpleName) {
        return enumMap.get(simpleName);
    }
}