package kh.edu.istad.platform.iam.domain.usecase.identity;

import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UnlinkIdentityUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService domainService;

    public void execute(UUID userUuid, UUID identityUuid) {

        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        Identity identity = userRepository.findIdentityByUuid(identityUuid)
                .filter(i -> !i.isDeleted())
                .orElseThrow(() -> new IamDomainException("Identity not found"));

        List<Identity> activeIdentities = userRepository.findIdentitiesByUserId(user.getId());

        domainService.validateIdentityUnlink(user, activeIdentities, identity.getId());
        String actor = currentActorProvider.getCurrentActorSub();
        identity.unlink(actor);
        userRepository.saveIdentity(identity);
    }
}
