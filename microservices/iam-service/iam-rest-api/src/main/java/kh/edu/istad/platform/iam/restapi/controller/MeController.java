package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.ChangePasswordCommand;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.ChangePasswordUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.UpdateUserProfileUseCase;
import kh.edu.istad.platform.iam.restapi.dto.request.ChangePasswordRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.IdentityResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.LoginHistoryResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserRoleSummaryResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserSessionResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import kh.edu.istad.platform.iam.restapi.service.RedisLoginHistoryService;
import kh.edu.istad.platform.iam.restapi.service.RedisMediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private final UserRepository userRepository;
    private final RedisMediaStorageService mediaStorageService;
    private final RedisLoginHistoryService loginHistoryService;

    private final Map<String, Boolean> revokedSessions = new ConcurrentHashMap<>();

    @GetMapping
    public Map<String, Object> getMe(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found: " + userUuid));

        UserResult result = getUserUseCase.execute(userUuid);
        List<UserRoleSummaryResponse> roleResponses = result.roles().stream()
                .map(userWebMapper::toRoleSummaryResponse)
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("uuid", user.getUuid());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("emailVerified", user.isEmailVerified());
        response.put("name", user.getFullName());
        response.put("givenName", user.getGivenName());
        response.put("familyName", user.getFamilyName());
        response.put("middleName", user.getMiddleName());
        response.put("nickname", user.getNickname());
        response.put("gender", user.getGender());
        response.put("birthdate", user.getBirthdate() != null ? user.getBirthdate().toString() : null);
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("pictureUrl", user.getPictureUrl());
        response.put("profileUrl", user.getProfileUrl());
        response.put("zoneinfo", user.getZoneinfo());
        response.put("locale", user.getLocale());
        response.put("enabled", user.isEnabled());
        response.put("roles", roleResponses);
        response.put("createdAt", user.getCreatedAt());
        response.put("updatedAt", user.getUpdatedAt());

        Map<String, Object> address = new LinkedHashMap<>();
        address.put("streetAddress", user.getAddressStreetAddress() != null ? user.getAddressStreetAddress() : "");
        address.put("locality", user.getAddressLocality() != null ? user.getAddressLocality() : "");
        address.put("region", user.getAddressRegion() != null ? user.getAddressRegion() : "");
        address.put("postalCode", user.getAddressPostalCode() != null ? user.getAddressPostalCode() : "");
        address.put("country", user.getAddressCountry() != null ? user.getAddressCountry() : "KH");
        response.put("address", address);

        return response;
    }

    @PatchMapping
    public Map<String, Object> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        updateUserProfileUseCase.execute(userWebMapper.toCommand(userUuid, request));
        return getMe(jwt);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG, WEBP) are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Avatar file size must be less than 5MB");
        }

        UUID userUuid = UUID.fromString(jwt.getSubject());
        mediaStorageService.saveAvatar(userUuid, file.getBytes(), contentType);

        String avatarUrl = "/api/media/avatars/" + userUuid + "?v=" + System.currentTimeMillis();

        userRepository.findByUuid(userUuid).ifPresent(user -> {
            User updated = User.builder()
                    .id(user.getId())
                    .uuid(user.getUuid())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .emailVerified(user.isEmailVerified())
                    .passwordHash(user.getPasswordHash())
                    .givenName(user.getGivenName())
                    .familyName(user.getFamilyName())
                    .middleName(user.getMiddleName())
                    .nickname(user.getNickname())
                    .gender(user.getGender())
                    .birthdate(user.getBirthdate())
                    .profileUrl(user.getProfileUrl())
                    .pictureUrl(avatarUrl)
                    .websiteUrl(user.getWebsiteUrl())
                    .zoneinfo(user.getZoneinfo())
                    .locale(user.getLocale())
                    .phoneNumber(user.getPhoneNumber())
                    .phoneNumberVerified(user.isPhoneNumberVerified())
                    .addressStreetAddress(user.getAddressStreetAddress())
                    .addressLocality(user.getAddressLocality())
                    .addressRegion(user.getAddressRegion())
                    .addressPostalCode(user.getAddressPostalCode())
                    .addressCountry(user.getAddressCountry())
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
                    .updatedBy(user.getUsername())
                    .version(user.getVersion())
                    .build();
            userRepository.save(updated);
        });

        return Map.of(
                "pictureUrl", avatarUrl,
                "message", "Avatar uploaded and stored in Redis successfully"
        );
    }

    @DeleteMapping("/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        mediaStorageService.deleteAvatar(userUuid);

        userRepository.findByUuid(userUuid).ifPresent(user -> {
            User updated = User.builder()
                    .id(user.getId())
                    .uuid(user.getUuid())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .emailVerified(user.isEmailVerified())
                    .passwordHash(user.getPasswordHash())
                    .givenName(user.getGivenName())
                    .familyName(user.getFamilyName())
                    .middleName(user.getMiddleName())
                    .nickname(user.getNickname())
                    .gender(user.getGender())
                    .birthdate(user.getBirthdate())
                    .profileUrl(user.getProfileUrl())
                    .pictureUrl(null)
                    .websiteUrl(user.getWebsiteUrl())
                    .zoneinfo(user.getZoneinfo())
                    .locale(user.getLocale())
                    .phoneNumber(user.getPhoneNumber())
                    .phoneNumberVerified(user.isPhoneNumberVerified())
                    .addressStreetAddress(user.getAddressStreetAddress())
                    .addressLocality(user.getAddressLocality())
                    .addressRegion(user.getAddressRegion())
                    .addressPostalCode(user.getAddressPostalCode())
                    .addressCountry(user.getAddressCountry())
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
                    .updatedBy(user.getUsername())
                    .version(user.getVersion())
                    .build();
            userRepository.save(updated);
        });
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
            @AuthenticationPrincipal Jwt jwt, @PathVariable("identityUuid") UUID identityUuid) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        unlinkIdentityUseCase.execute(userUuid, identityUuid);
    }

    @GetMapping("/sessions")
    public List<UserSessionResponse> getActiveSessions(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest request
    ) {
        String sessionId = "sess-" + Math.abs(jwt.getId() != null ? jwt.getId().hashCode() : jwt.getSubject().hashCode());

        if (Boolean.TRUE.equals(revokedSessions.get(sessionId))) {
            return List.of();
        }

        String userAgent = request.getHeader("User-Agent");
        String device = resolveDevice(userAgent);
        String browser = resolveBrowser(userAgent);
        String ipAddress = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            ipAddress = "127.0.0.1 (Localhost)";
        }

        Instant lastActive = jwt.getIssuedAt() != null ? jwt.getIssuedAt() : Instant.now();

        return List.of(new UserSessionResponse(
                sessionId,
                device,
                browser,
                ipAddress,
                "Phnom Penh, Cambodia",
                lastActive,
                true
        ));
    }

    @DeleteMapping("/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void terminateSession(@PathVariable("sessionId") String sessionId) {
        revokedSessions.put(sessionId, true);
    }

    @DeleteMapping("/sessions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void terminateAllOtherSessions() {
    }

    @GetMapping("/login-history")
    public List<LoginHistoryResponse> getLoginHistory(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        return loginHistoryService.getLoginHistory(userUuid);
    }

    @GetMapping("/activity")
    public List<Map<String, Object>> getAccountActivity(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UserResult user = getUserUseCase.execute(userUuid);

        return List.of(
                Map.of(
                        "id", "act-1",
                        "type", "USER_AUTHENTICATED",
                        "title", "Authenticated to Platform",
                        "description", "Successful login with signed RS256 token",
                        "timestamp", user.updatedAt() != null ? user.updatedAt().toString() : Instant.now().toString(),
                        "actor", user.username()
                ),
                Map.of(
                        "id", "act-2",
                        "type", "ACCOUNT_REGISTERED",
                        "title", "Account Provisioned",
                        "description", "Identity created in ISTAD IAM repository",
                        "timestamp", user.createdAt() != null ? user.createdAt().toString() : Instant.now().toString(),
                        "actor", "system"
                )
        );
    }

    private String resolveDevice(String userAgent) {
        if (userAgent == null) return "Desktop (Unknown)";
        if (userAgent.contains("Windows")) return "Desktop (Windows 11)";
        if (userAgent.contains("Macintosh")) return "MacBook (macOS)";
        if (userAgent.contains("Linux")) return "Desktop (Linux)";
        if (userAgent.contains("Android")) return "Android Device";
        if (userAgent.contains("iPhone")) return "iPhone";
        if (userAgent.contains("iPad")) return "iPad";
        return "Desktop";
    }

    private String resolveBrowser(String userAgent) {
        if (userAgent == null) return "Browser";
        if (userAgent.contains("Edg/")) return "Microsoft Edge";
        if (userAgent.contains("Chrome/")) return "Chrome";
        if (userAgent.contains("Firefox/")) return "Firefox";
        if (userAgent.contains("Safari/")) return "Safari";
        return "Web Browser";
    }
}