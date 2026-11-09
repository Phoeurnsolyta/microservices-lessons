package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.RegisterUserCommand;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final RoleRepository roleRepository;

    public UserResult execute(RegisterUserCommand command) {
        log.info("Processing user self-registration for username: {}", command.username());
        if (!command.password().equals(command.confirmPassword())) {
            throw new IamDomainException("Password and confirmation password do not match");
        }
        if (userRepository.existsByUsername(command.username())) {
            throw new IamDomainException("Username is already taken");
        }
        if (userRepository.existsByEmail(command.email().toLowerCase())) {
            throw new IamDomainException("Email is already registered");
        }

        Instant now = Instant.now();
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username(command.username())
                .email(command.email().toLowerCase())
                .emailVerified(false)
                .passwordHash(passwordHasher.encode(command.password()))
                .givenName(command.givenName())
                .familyName(command.familyName())
                .locale(command.locale() != null ? command.locale() : "km")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .isDeleted(false)
                .createdAt(now)
                .createdBy("self-register")
                .updatedAt(now)
                .updatedBy("self-register")
                .build();

        User savedUser = userRepository.save(user);

        Role userRole = roleRepository.findByName("USER").orElse(null);
        if (userRole != null) {
            UserRole assignment = UserRole.builder()
                    .userId(savedUser.getId())
                    .roleId(userRole.getId())
                    .assignedAt(now)
                    .assignedBy("system")
                    .reason("Default role assigned upon self-registration")
                    .build();
            userRepository.saveUserRole(assignment);
        }

        List<RoleSummary> roles = userRole != null ?
                List.of(new RoleSummary(userRole.getUuid(), userRole.getName(), userRole.getDescription(), userRole.isActive())) :
                List.of();

        return new UserResult(
                savedUser.getUuid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.isEmailVerified(),
                savedUser.getFullName(),
                savedUser.getGivenName(),
                savedUser.getFamilyName(),
                savedUser.getLocale(),
                savedUser.isEnabled(),
                roles,
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}