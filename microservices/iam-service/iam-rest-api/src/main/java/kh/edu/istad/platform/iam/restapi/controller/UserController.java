package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.*;
import kh.edu.istad.platform.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.*;
import kh.edu.istad.platform.iam.restapi.dto.request.*;
import kh.edu.istad.platform.iam.restapi.dto.response.IdentityResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserAuthoritiesResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final EnableUserUseCase enableUserUseCase;
    private final DisableUserUseCase disableUserUseCase;
    private final LockUserUseCase lockUserUseCase;
    private final UnlockUserUseCase unlockUserUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final AssignRolesToUserUseCase assignRolesToUserUseCase;
    private final RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    private final GrantPermissionsToUserUseCase grantPermissionsToUserUseCase;
    private final RevokePermissionFromUserUseCase revokePermissionFromUserUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UserWebMapper userWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user:create')")
    public UserResponse createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResult result = createUserUseCase.execute(userWebMapper.toCommand(request));
        return userWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public PageResponse<UserSummary> listUsers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "enabled", required = false) Boolean enabled,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt,desc") String sort
    ) {
        PageResult<UserSummary> pageResult = listUsersUseCase
                .execute(new UserFilterQuery(
                        search, enabled, page, size, sort)
                );
        return userWebMapper.toPageResponse(pageResult);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserResponse getUser(@PathVariable("uuid") UUID uuid) {
        return userWebMapper.toResponse(getUserUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:update')")
    public UserResponse updateUserProfile(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UserResult result = updateUserProfileUseCase.execute(
                userWebMapper.toCommand(uuid, request)
        );
        return userWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:delete')")
    public void deleteUser(@PathVariable("uuid") UUID uuid) {
        deleteUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void enableUser(@PathVariable("uuid") UUID uuid) {
        enableUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void disableUser(@PathVariable("uuid") UUID uuid) {
        disableUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void lockUser(@PathVariable("uuid") UUID uuid) {
        lockUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlockUser(@PathVariable("uuid") UUID uuid) {
        unlockUserUseCase.execute(uuid);
    }

    @PutMapping("/{uuid}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void resetPassword(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody ResetUserPasswordRequest request) {
        resetUserPasswordUseCase.execute(
                new ResetUserPasswordCommand(uuid, request.newPassword())
        );
    }

    @GetMapping("/{uuid}/authorities")
    @PreAuthorize("hasAuthority('user:read')")
    public UserAuthoritiesResponse getUserAuthorities(@PathVariable("uuid") UUID uuid) {
        return userWebMapper.toAuthoritiesResponse(getUserAuthoritiesUseCase.execute(uuid));
    }

    @PostMapping("/{uuid}/roles")
    @PreAuthorize("hasAuthority('user:assign')")
    public List<RoleAssignmentResult> assignRoles(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody AssignRolesToUserRequest request
    ) {
        return assignRolesToUserUseCase.execute(
                new AssignRolesToUserCommand(
                        uuid,
                        request.roleUuids(),
                        request.expiresAt(),
                        request.reason())
        );
    }

    @DeleteMapping("/{uuid}/roles/{roleUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokeRole(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("roleUuid") UUID roleUuid,
            @RequestParam(name = "reason", required = false, defaultValue = "Revoked by administrator") String reason
    ) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid, reason);
    }

    @PostMapping("/{uuid}/permissions")
    @PreAuthorize("hasAuthority('user:assign')")
    public List<PermissionGrantResult> grantPermissions(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody GrantPermissionsToUserRequest request
    ) {
        return grantPermissionsToUserUseCase.execute(
                new GrantPermissionsToUserCommand(
                        uuid,
                        request.permissionUuids(),
                        request.effect(),
                        request.expiresAt(),
                        request.reason()
                ));
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokePermission(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("permissionUuid") UUID permissionUuid,
            @RequestParam(name = "reason", required = false, defaultValue = "Revoked by administrator") String reason
    ) {
        revokePermissionFromUserUseCase.execute(uuid, permissionUuid, reason);
    }

    @GetMapping("/{uuid}/identities")
    @PreAuthorize("hasAuthority('user:read')")
    public List<IdentityResponse> listUserIdentities(@PathVariable("uuid") UUID uuid) {
        return listUserIdentitiesUseCase.execute(uuid).stream()
                .map(userWebMapper::toIdentityResponse)
                .toList();
    }

    @DeleteMapping("/{uuid}/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlinkUserIdentity(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("identityUuid") UUID identityUuid) {
        unlinkIdentityUseCase.execute(uuid, identityUuid);
    }
}