package kh.edu.istad.platform.iam.persistence.enitity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    private Set<kh.edu.istad.platform.iam.persistence.enitity.PermissionEntity> permissions;

    @ManyToMany(mappedBy = "roles")
    private List<kh.edu.istad.platform.iam.persistence.enitity.UserEntity> users;
}
