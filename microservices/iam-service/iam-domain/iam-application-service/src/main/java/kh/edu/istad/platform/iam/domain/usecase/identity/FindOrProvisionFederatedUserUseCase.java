package kh.edu.istad.platform.iam.domain.usecase.identity;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.FederatedIdentityCommand;
import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;

    public AuthUserResult execute(FederatedIdentityCommand command) {
        IdentityProvider provider = IdentityProvider.valueOf(
                command.provider().toUpperCase()
        );

        log.info(
                "Federated login attempt with provider: {}, sub: {}",
                provider,
                command.providerSubject()
        );

        Optional<Identity> existingIdentity = userRepository
                .findIdentityByProvider(
                        provider, command.providerSubject()
                );

        if (existingIdentity.isPresent()) {

            Identity identity = existingIdentity.get();
            identity.markLogin();

            userRepository.saveIdentity(identity);

            User user = userRepository.findById(
                    identity.getUserId()
            ).orElseThrow();

            return loadUserForAuthenticationUseCase.execute(user.getUsername());
        }

        User user = null;
        if (command.email() != null && command.emailVerified()) {
            user = userRepository.findByEmail(
                    command.email().toLowerCase())
                    .orElse(null);
        }

        if (user == null) {
            String generatedUsername = command.username()
                    != null ? command.username() : (
                            command.provider().toLowerCase() + "_" + UUID.randomUUID().toString().substring(0, 8));

            user = User.builder()
                    .uuid(UUID.randomUUID())
                    .username(generatedUsername)
                    .email(command.email() != null ? command.email().toLowerCase() : generatedUsername + "@federated.iam")
                    .emailVerified(command.emailVerified())
                    .passwordHash(null)
                    .givenName(command.displayName() != null ? command.displayName() : generatedUsername)
                    .familyName("")
                    .pictureUrl(command.pictureUrl())
                    .locale("en")
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .createdBy("social-provider")
                    .updatedBy("social-provider")
                    .build();

            user = userRepository.save(user);

            Role defaultRole = roleRepository.findByName("USER").orElse(null);

            if (defaultRole != null) {
                UserRole userRole = UserRole.builder()
                        .userId(user.getId())
                        .roleId(defaultRole.getId())
                        .assignedAt(Instant.now())
                        .assignedBy("system")
                        .reason("Assigned during federated user auto-provisioning")
                        .build();

                userRepository.saveUserRole(userRole);
            }
        }

        Identity newIdentity = Identity.builder()
                .uuid(UUID.randomUUID())
                .userId(user.getId())
                .provider(provider)
                .providerSubject(command.providerSubject())
                .email(command.email())
                .emailVerified(command.emailVerified())
                .displayName(command.displayName())
                .username(command.username())
                .pictureUrl(command.pictureUrl())
                .attributes(command.attributes())
                .linkedAt(Instant.now())
                .lastLoginAt(Instant.now())
                .createdBy("social-login")
                .updatedBy("social-login")
                .build();

        userRepository.saveIdentity(newIdentity);

        return loadUserForAuthenticationUseCase.execute(user.getUsername());
    }
}
