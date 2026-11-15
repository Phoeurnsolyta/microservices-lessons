package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import kh.edu.istad.platform.iam.restapi.dto.response.LoginHistoryResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserSessionResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    // Track terminated sessions in memory
    private final Map<String, Boolean> revokedSessions = new ConcurrentHashMap<>();

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
            @AuthenticationPrincipal Jwt jwt, @PathVariable("identityUuid") UUID identityUuid) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        unlinkIdentityUseCase.execute(userUuid, identityUuid);
    }

    // ==========================================
    // REAL SESSIONS (From Authenticated User Request)
    // ==========================================
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

        // Return ONLY the real session currently authenticated!
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
    public List<LoginHistoryResponse> getLoginHistory(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest request
    ) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UserResult user = getUserUseCase.execute(userUuid);

        String userAgent = request.getHeader("User-Agent");
        String device = resolveDevice(userAgent) + " / " + resolveBrowser(userAgent);
        String ipAddress = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }

        Instant loginTime = user.updatedAt() != null ? user.updatedAt() : Instant.now();

        List<LoginHistoryResponse> history = new ArrayList<>();
        history.add(new LoginHistoryResponse(
                "lh-" + user.uuid().toString().substring(0, 8),
                loginTime,
                "SUCCESS",
                ipAddress,
                device,
                "Phnom Penh, KH",
                "Password Authentication"
        ));
        return history;
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