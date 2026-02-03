package cool.auv.authspringbootstarter.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class RelationBinder {

    @PersistenceContext
    private EntityManager em;

    public <T> T bind(Class<T> type, Long id) {

        if (id == null) {
            return null;
        }

        return em.getReference(type, id);
    }
}
