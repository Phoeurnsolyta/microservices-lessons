package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.UpdateUserProfileCommand;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final GetUserUseCase getUserUseCase;
    private final CurrentActorProvider currentActorProvider;

    public UserResult execute(UpdateUserProfileCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found: " + command.userUuid()));

        String actor = currentActorProvider.getCurrentActorSub();

        User.Builder updated = User.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .username(user.getUsername())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .passwordHash(user.getPasswordHash())
                .givenName(command.givenName() != null ? command.givenName() : user.getGivenName())
                .familyName(command.familyName() != null ? command.familyName() : user.getFamilyName())
                .middleName(command.middleName() != null ? command.middleName() : user.getMiddleName())
                .nickname(command.nickname() != null ? command.nickname() : user.getNickname())
                .gender(command.gender() != null ? command.gender() : user.getGender())
                .birthdate(command.birthdate() != null ? command.birthdate() : user.getBirthdate())
                .phoneNumber(command.phoneNumber() != null ? command.phoneNumber() : user.getPhoneNumber())
                .zoneinfo(command.zoneinfo() != null ? command.zoneinfo() : user.getZoneinfo())
                .locale(command.locale() != null ? command.locale() : user.getLocale())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(actor);

        if (command.address() != null) {
            updated.addressStreetAddress(command.address().streetAddress())
                    .addressLocality(command.address().locality())
                    .addressRegion(command.address().region())
                    .addressPostalCode(command.address().postalCode())
                    .addressCountry(command.address().country());
        }

        userRepository.save(updated.build());

        return getUserUseCase.execute(user.getUuid());
    }
}
