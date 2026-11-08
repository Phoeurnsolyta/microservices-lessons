package kh.edu.istad.platform.iam.init;

import jakarta.annotation.PostConstruct;

import kh.edu.istad.platform.iam.persistence.entity.*;
import kh.edu.istad.platform.iam.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        if (!userJpaRepository.existsByUsername("phoeurnsolyta")) {
            Instant now = Instant.now();

            PermissionEntity permission = permissionJpaRepository.findByName("user:read")
                    .orElseGet(() -> permissionJpaRepository.save(
                            PermissionEntity.builder()
                                    .uuid(UUID.randomUUID())
                                    .name("user:read")
                                    .resource("user")
                                    .action("read")
                                    .description("Read all users")
                                    .isSystem(true)
                                    .isActive(true)
                                    .isDeleted(false)
                                    .createdAt(now)
                                    .createdBy("system")
                                    .updatedAt(now)
                                    .updatedBy("system")
                                    .build()
                    ));

            RoleEntity role = roleJpaRepository.findByName("ADMIN")
                    .orElseGet(() -> roleJpaRepository.save(
                            RoleEntity.builder()
                                    .uuid(UUID.randomUUID())
                                    .name("ADMIN")
                                    .description("Admin")
                                    .isSystem(true)
                                    .isActive(true)
                                    .isDeleted(false)
                                    .createdAt(now)
                                    .createdBy("system")
                                    .updatedAt(now)
                                    .updatedBy("system")
                                    .build()
                    ));

            UserEntity user = UserEntity.builder()
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
                    .build();

            UserEntity savedUser = userJpaRepository.save(user);

            UserRoleEntity userRole = UserRoleEntity.builder()
                    .userId(savedUser.getId())
                    .roleId(role.getId())
                    .assignedAt(now)
                    .assignedBy("system")
                    .reason("Initial administrator setup")
                    .build();
            userRoleJpaRepository.save(userRole);

            UserPermissionEntity userPermission = UserPermissionEntity.builder()
                    .userId(savedUser.getId())
                    .permissionId(permission.getId())
                    .effect("GRANT")
                    .assignedAt(now)
                    .assignedBy("system")
                    .reason("Initial administrator setup")
                    .build();
            userPermissionJpaRepository.save(userPermission);
        }
    }
}
