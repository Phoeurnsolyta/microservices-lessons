package kh.edu.istad.platform.iam.init;

import jakarta.annotation.PostConstruct;
import kh.edu.istad.platform.iam.persistence.entity.*;
import kh.edu.istad.platform.iam.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Instant now = Instant.now();

        // 1. Seed all standard system permissions
        List<String[]> permissionsToSeed = List.of(
                new String[]{"user:create", "user", "create", "Create a new user"},
                new String[]{"user:read", "user", "read", "Read user profiles and details"},
                new String[]{"user:update", "user", "update", "Update user profiles"},
                new String[]{"user:delete", "user", "delete", "Delete users"},
                new String[]{"user:assign", "user", "assign", "Assign roles and permissions to users"},
                new String[]{"role:create", "role", "create", "Create a new role"},
                new String[]{"role:read", "role", "read", "Read role details"},
                new String[]{"role:update", "role", "update", "Update role details"},
                new String[]{"role:delete", "role", "delete", "Delete roles"},
                new String[]{"role:assign", "role", "assign", "Assign permissions to roles"},
                new String[]{"permission:create", "permission", "create", "Create a new permission"},
                new String[]{"permission:read", "permission", "read", "Read permission details"},
                new String[]{"permission:update", "permission", "update", "Update permission details"},
                new String[]{"permission:delete", "permission", "delete", "Delete permissions"}
        );

        List<PermissionEntity> allPermissions = new ArrayList<>();
        for (String[] permData : permissionsToSeed) {
            String name = permData[0];
            PermissionEntity perm = permissionJpaRepository.findByName(name)
                    .orElseGet(() -> permissionJpaRepository.save(
                            PermissionEntity.builder()
                                    .uuid(UUID.randomUUID())
                                    .name(name)
                                    .resource(permData[1])
                                    .action(permData[2])
                                    .description(permData[3])
                                    .isSystem(true)
                                    .isActive(true)
                                    .isDeleted(false)
                                    .createdAt(now)
                                    .createdBy("system")
                                    .updatedAt(now)
                                    .updatedBy("system")
                                    .build()
                    ));
            allPermissions.add(perm);
        }

        // 2. Ensure ADMIN and USER roles exist
        RoleEntity adminRole = roleJpaRepository.findByName("ADMIN")
                .orElseGet(() -> roleJpaRepository.save(
                        RoleEntity.builder()
                                .uuid(UUID.randomUUID())
                                .name("ADMIN")
                                .description("Super administrator with full system permissions")
                                .isSystem(true)
                                .isActive(true)
                                .isDeleted(false)
                                .createdAt(now)
                                .createdBy("system")
                                .updatedAt(now)
                                .updatedBy("system")
                                .build()
                ));

        RoleEntity userRole = roleJpaRepository.findByName("USER")
                .orElseGet(() -> roleJpaRepository.save(
                        RoleEntity.builder()
                                .uuid(UUID.randomUUID())
                                .name("USER")
                                .description("Standard application user")
                                .isSystem(true)
                                .isActive(true)
                                .isDeleted(false)
                                .createdAt(now)
                                .createdBy("system")
                                .updatedAt(now)
                                .updatedBy("system")
                                .build()
                ));

        // 3. Link ALL permissions to the ADMIN role
        for (PermissionEntity p : allPermissions) {
            if (rolePermissionJpaRepository.findActiveAssignment(adminRole.getId(), p.getId()).isEmpty()) {
                rolePermissionJpaRepository.save(
                        RolePermissionEntity.builder()
                                .roleId(adminRole.getId())
                                .permissionId(p.getId())
                                .assignedAt(now)
                                .assignedBy("system")
                                .reason("Default admin permissions")
                                .build()
                );
            }
        }

        // 4. Link user:read to USER role
        permissionJpaRepository.findByName("user:read").ifPresent(readPerm -> {
            if (rolePermissionJpaRepository.findActiveAssignment(userRole.getId(), readPerm.getId()).isEmpty()) {
                rolePermissionJpaRepository.save(
                        RolePermissionEntity.builder()
                                .roleId(userRole.getId())
                                .permissionId(readPerm.getId())
                                .assignedAt(now)
                                .assignedBy("system")
                                .reason("Default user read permission")
                                .build()
                );
            }
        });

        // 5. Ensure phoeurnsolyta user exists and has ADMIN role assigned
        UserEntity user = userJpaRepository.findByUsername("phoeurnsolyta")
                .orElseGet(() -> userJpaRepository.save(
                        UserEntity.builder()
                                .uuid(UUID.randomUUID())
                                .username("phoeurnsolyta")
                                .email("lyta07807@gmail.com")
                                .emailVerified(true)
                                .passwordHash(passwordEncoder.encode("secret"))
                                .familyName("Phoeurn")
                                .givenName("Solyta")
                                .gender("Female")
                                .locale("en")
                                .phoneNumberVerified(false)
                                .enabled(true)
                                .accountNonExpired(true)
                                .accountNonLocked(true)
                                .credentialsNonExpired(true)
                                .failedLoginAttempts(0)
                                .isDeleted(false)
                                .createdAt(now)
                                .createdBy("system")
                                .updatedAt(now)
                                .updatedBy("system")
                                .build()
                ));

        if (userRoleJpaRepository.findActiveAssignment(user.getId(), adminRole.getId()).isEmpty()) {
            userRoleJpaRepository.save(
                    UserRoleEntity.builder()
                            .userId(user.getId())
                            .roleId(adminRole.getId())
                            .assignedAt(now)
                            .assignedBy("system")
                            .reason("Initial administrator setup")
                            .build()
            );
        }
    }
}