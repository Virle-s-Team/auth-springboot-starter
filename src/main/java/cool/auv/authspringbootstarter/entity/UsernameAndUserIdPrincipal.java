package cool.auv.authspringbootstarter.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UsernameAndUserIdPrincipal implements Serializable {

    private Long id;

    private String username;

    private String tenantId;

    private String campusId;

    private List<String> permissions;


    public UsernameAndUserIdPrincipal() {
    }

    public UsernameAndUserIdPrincipal(Long id, String username, String tenantId, String campusId, List<String> permissions) {
        this.id = id;
        this.username = username;
        this.tenantId = tenantId;
        this.campusId = campusId;
        this.permissions = permissions;
    }
}
