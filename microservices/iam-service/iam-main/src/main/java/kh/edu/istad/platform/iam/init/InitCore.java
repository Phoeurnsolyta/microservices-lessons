package kh.edu.istad.platform.iam.init;

import jakarta.annotation.PostConstruct;

import kh.edu.istad.platform.iam.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        kh.edu.istad.platform.iam.persistence.enitity.PermissionEntity permission = kh.edu.istad.platform.iam.persistence.enitity.PermissionEntity.builder()
                .permission("user:read:all")
                .description("Read all users")
                .build();

        kh.edu.istad.platform.iam.persistence.enitity.RoleEntity role = kh.edu.istad.platform.iam.persistence.enitity.RoleEntity.builder()
                .role("ADMIN")
                .description("Admin")
                .build();

        kh.edu.istad.platform.iam.persistence.enitity.UserEntity user = kh.edu.istad.platform.iam.persistence.enitity.UserEntity.builder()
                .familyName("Phoeurn")
                .givenName("Solyta")
                .email("lyta07807@gmail.com")
                .gender("Female")
                .emailVerified(true)
                .username("phoeurnsolyta")
                .password(passwordEncoder.encode("secret"))
                .build();

        if(!userJpaRepository.existsByUsername("phoeurnsolyta")){
            role = roleJpaRepository.save(role);
            permission = permissionJpaRepository.save(permission);
            user.setRoles(Set.of(role));
            user.setPermissions(Set.of(permission));
            userJpaRepository.save(user);
        }
    }
}
