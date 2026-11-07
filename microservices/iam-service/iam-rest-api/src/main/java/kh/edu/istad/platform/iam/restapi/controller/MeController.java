package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.ChangePasswordCommand;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.ChangePasswordUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.UpdateUserProfileUseCase;
import kh.edu.istad.platform.iam.restapi.dto.request.ChangePasswordRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.IdentityResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final GetUserUseCase getUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UserWebMapper userWebMapper;

    @GetMapping
    public UserResponse getMe(@AuthenticationPrincipal Jwt jwt) {

        UUID userUuid = UUID.fromString(jwt.getSubject());
        return userWebMapper.toResponse(getUserUseCase.execute(userUuid));
    }

    @PatchMapping
    public UserResponse updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UserResult result = updateUserProfileUseCase
                .execute(userWebMapper.toCommand(userUuid, request));
        return userWebMapper.toResponse(result);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        changePasswordUseCase.execute(
                new ChangePasswordCommand(
                        userUuid,
                        request.currentPassword(),
                        request.newPassword(),
                        request.confirmPassword()
        ));
    }

    @GetMapping("/identities")
    public List<IdentityResponse> getIdentities(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        return listUserIdentitiesUseCase
                .execute(userUuid).stream()
                .map(userWebMapper::toIdentityResponse)
                .toList();
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkIdentity(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID identityUuid) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        unlinkIdentityUseCase.execute(userUuid, identityUuid);
    }
}
