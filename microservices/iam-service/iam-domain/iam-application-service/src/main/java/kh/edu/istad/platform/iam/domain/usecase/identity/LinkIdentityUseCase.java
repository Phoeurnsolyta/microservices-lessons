package kh.edu.istad.platform.iam.domain.usecase.identity;

import kh.edu.istad.platform.iam.domain.dto.FederatedIdentityCommand;
import kh.edu.istad.platform.iam.domain.dto.IdentityResult;
import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LinkIdentityUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public IdentityResult execute(UUID userUuid, FederatedIdentityCommand command) {

        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        IdentityProvider provider = IdentityProvider
                .valueOf(command.provider().toUpperCase());

        if (
                userRepository.existsByUserIdAndProvider(user.getId(), provider)
        ) {
            throw new IamDomainException(
                    "Provider " + provider + " is already linked to this account");
        }

        String actor = currentActorProvider.getCurrentActorSub();

        Identity identity = Identity.builder()
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
                .createdBy(actor)
                .updatedBy(actor)
                .build();

        Identity saved = userRepository.saveIdentity(identity);

        return new IdentityResult(
                saved.getUuid(),
                saved.getProvider().name(),
                saved.getEmail(),
                saved.getDisplayName(),
                saved.getLinkedAt()
        );
    }
}
