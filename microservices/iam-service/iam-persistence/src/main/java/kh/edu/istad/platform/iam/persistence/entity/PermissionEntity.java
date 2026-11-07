package kh.edu.istad.platform.iam.persistence.enitity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "permissions")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<kh.edu.istad.platform.iam.persistence.enitity.RoleEntity> roles;

    @ManyToMany(mappedBy = "permissions")
    private List<kh.edu.istad.platform.iam.persistence.enitity.UserEntity> users;
}
