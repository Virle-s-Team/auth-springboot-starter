package cool.auv.authspringbootstarter.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    @Override
    public String resolveCurrentTenantIdentifier() {
        if (TenantContext.getTenantId() == null) {
            return "default-tenant";
        }
        return TenantContext.getTenantId();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }


    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
                "hibernate.tenant_identifier_resolver",
                this
        );
    }
}
