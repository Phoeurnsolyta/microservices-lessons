package kh.edu.istad.iam.domain;

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
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    @ManyToMany(mappedBy = "permissions")
    private List<User> users;
}
