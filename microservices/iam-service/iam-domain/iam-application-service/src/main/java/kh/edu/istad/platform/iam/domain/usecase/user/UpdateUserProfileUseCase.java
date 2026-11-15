package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.AddressCommand;
import kh.edu.istad.platform.iam.domain.dto.UpdateUserProfileCommand;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
                .givenName(command.givenName() != null && !command.givenName().isBlank() ? command.givenName() : user.getGivenName())
                .familyName(command.familyName() != null && !command.familyName().isBlank() ? command.familyName() : user.getFamilyName())
                .middleName(command.middleName() != null ? command.middleName() : user.getMiddleName())
                .nickname(command.nickname() != null ? command.nickname() : user.getNickname())
                .gender(command.gender() != null ? command.gender() : user.getGender())
                .birthdate(command.birthdate() != null ? command.birthdate() : user.getBirthdate())
                .phoneNumber(command.phoneNumber() != null && !command.phoneNumber().isBlank() ? command.phoneNumber() : user.getPhoneNumber())
                .profileUrl(user.getProfileUrl())
                .pictureUrl(user.getPictureUrl())
                .websiteUrl(user.getWebsiteUrl())
                .zoneinfo(command.zoneinfo() != null ? command.zoneinfo() : user.getZoneinfo())
                .locale(command.locale() != null ? command.locale() : user.getLocale())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .lastLoginAt(user.getLastLoginAt())
                .passwordChangedAt(user.getPasswordChangedAt())
                .isDeleted(user.isDeleted())
                .deletedAt(user.getDeletedAt())
                .deletedBy(user.getDeletedBy())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .updatedAt(Instant.now())
                .updatedBy(actor)
                .version(user.getVersion());

        if (command.address() != null) {
            AddressCommand addr = command.address();
            updated.addressStreetAddress(addr.streetAddress() != null && !addr.streetAddress().isBlank() ? addr.streetAddress() : user.getAddressStreetAddress())
                    .addressLocality(addr.locality() != null && !addr.locality().isBlank() ? addr.locality() : user.getAddressLocality())
                    .addressRegion(addr.region() != null && !addr.region().isBlank() ? addr.region() : user.getAddressRegion())
                    .addressPostalCode(addr.postalCode() != null && !addr.postalCode().isBlank() ? addr.postalCode() : user.getAddressPostalCode())
                    .addressCountry(addr.country() != null && !addr.country().isBlank() ? addr.country() : user.getAddressCountry());
        } else {
            updated.addressStreetAddress(user.getAddressStreetAddress())
                    .addressLocality(user.getAddressLocality())
                    .addressRegion(user.getAddressRegion())
                    .addressPostalCode(user.getAddressPostalCode())
                    .addressCountry(user.getAddressCountry());
        }

        userRepository.save(updated.build());

        return getUserUseCase.execute(user.getUuid());
    }
}