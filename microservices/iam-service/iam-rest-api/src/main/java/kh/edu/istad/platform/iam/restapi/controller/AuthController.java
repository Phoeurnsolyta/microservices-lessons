package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserAuthoritiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.platform.iam.restapi.dto.request.LoginRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.RegisterUserRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.TokenResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import kh.edu.istad.platform.iam.restapi.service.RedisLoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UserWebMapper userWebMapper;
    private final RedisLoginHistoryService loginHistoryService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        UserResult result = registerUserUseCase.execute(userWebMapper.toCommand(request));
        loginHistoryService.recordAuditEvent(
                "USER_CREATED",
                result.username(),
                "User: " + result.username(),
                "New user registered self-service",
                "127.0.0.1",
                "SUCCESS"
        );
        return userWebMapper.toResponse(result);
    }

    @PostMapping("/login")
    public TokenResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        AuthUserResult user = loadUserForAuthenticationUseCase.execute(request.usernameOrEmail());

        String ipAddress = httpRequest.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        String userAgent = httpRequest.getHeader("User-Agent");
        String device = resolveDevice(userAgent);

        // 1. Reject deactivated / disabled accounts
        if (!user.enabled()) {
            loginHistoryService.recordLogin(user.uuid(), "FAILED", ipAddress, device, "Phnom Penh, KH", "Account deactivated or disabled");
            loginHistoryService.recordAuditEvent(
                    "LOGIN_FAILURE",
                    user.username(),
                    "Auth Service",
                    "Rejected login: Account is deactivated or disabled",
                    ipAddress,
                    "FAILURE"
            );
            throw new BadCredentialsException("Your account is deactivated or disabled. Please contact an administrator to restore access.");
        }

        // 2. Reject locked accounts
        if (!user.accountNonLocked()) {
            loginHistoryService.recordLogin(user.uuid(), "FAILED", ipAddress, device, "Phnom Penh, KH", "Account locked");
            loginHistoryService.recordAuditEvent(
                    "LOGIN_FAILURE",
                    user.username(),
                    "Auth Service",
                    "Rejected login: Account is locked",
                    ipAddress,
                    "FAILURE"
            );
            throw new BadCredentialsException("Your account is locked. Please contact an administrator.");
        }

        // 3. Verify password credentials
        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            loginHistoryService.recordLogin(user.uuid(), "FAILED", ipAddress, device, "Phnom Penh, KH", "Invalid password hash");
            loginHistoryService.recordAuditEvent(
                    "LOGIN_FAILURE",
                    user.username(),
                    "Auth Service",
                    "Invalid password attempt from " + device,
                    ipAddress,
                    "FAILURE"
            );
            throw new BadCredentialsException("Invalid username or password");
        }

        // Record SUCCESS login attempt in Redis (personal history + live audit log)
        loginHistoryService.recordLogin(user.uuid(), "SUCCESS", ipAddress, device, "Phnom Penh, KH", "Password Authentication");
        loginHistoryService.recordAuditEvent(
                "USER_LOGIN_SUCCESS",
                user.username(),
                "User: " + user.username(),
                "Password authentication via " + device,
                ipAddress,
                "SUCCESS"
        );

        UserAuthoritiesResult authorities = getUserAuthoritiesUseCase.execute(user.uuid());

        Instant now = Instant.now();
        List<String> roleStrings = authorities.roles().stream()
                .map(kh.edu.istad.platform.iam.domain.dto.RoleAuthorityResult::name)
                .toList();

        List<String> permStrings = new ArrayList<>(authorities.effectivePermissions());

        List<String> authStrings = new ArrayList<>();
        for (String r : roleStrings) {
            authStrings.add("ROLE_" + r);
        }
        authStrings.addAll(permStrings);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.HOURS))
                .subject(user.uuid().toString())
                .claim("preferred_username", user.username())
                .claim("roles", roleStrings)
                .claim("permissions", permStrings)
                .claim("authorities", authStrings)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(() -> "RS256").build(), claims)).getTokenValue();

        // Establish Spring Security Session
        List<SimpleGrantedAuthority> grantedAuthorities = authStrings.stream().map(SimpleGrantedAuthority::new).toList();
        UserDetails userDetails = User.builder()
                .username(user.username())
                .password("")
                .authorities(grantedAuthorities)
                .build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                grantedAuthorities
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        SavedRequest savedRequest = requestCache.getRequest(httpRequest, httpResponse);
        String redirectUrl = savedRequest != null ? savedRequest.getRedirectUrl() : null;
        if (savedRequest != null) {
            requestCache.removeRequest(httpRequest, httpResponse);
        }

        return new TokenResponse(token, "Bearer", 18000, redirectUrl);
    }

    @GetMapping("/session")
    public ResponseEntity<TokenResponse> session(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        SecurityContext context = session != null
                ? (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
                : null;

        if (context == null || context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()
                || "anonymousUser".equalsIgnoreCase(context.getAuthentication().getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = context.getAuthentication().getName();
        AuthUserResult user = loadUserForAuthenticationUseCase.execute(username);

        String ipAddress = httpRequest.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        String userAgent = httpRequest.getHeader("User-Agent");
        String device = resolveDevice(userAgent);

        loginHistoryService.recordLogin(user.uuid(), "SUCCESS", ipAddress, device, "Phnom Penh, KH", "OAuth2 SSO Session");
        loginHistoryService.recordAuditEvent(
                "USER_LOGIN_SUCCESS",
                user.username(),
                "User: " + user.username(),
                "OAuth2 SSO Session established via " + device,
                ipAddress,
                "SUCCESS"
        );

        UserAuthoritiesResult authorities = getUserAuthoritiesUseCase.execute(user.uuid());

        Instant now = Instant.now();
        List<String> roleStrings = authorities.roles().stream()
                .map(kh.edu.istad.platform.iam.domain.dto.RoleAuthorityResult::name)
                .toList();

        List<String> permStrings = new ArrayList<>(authorities.effectivePermissions());

        List<String> authStrings = new ArrayList<>();
        for (String r : roleStrings) {
            authStrings.add("ROLE_" + r);
        }
        authStrings.addAll(permStrings);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.HOURS))
                .subject(user.uuid().toString())
                .claim("preferred_username", user.username())
                .claim("roles", roleStrings)
                .claim("permissions", permStrings)
                .claim("authorities", authStrings)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(() -> "RS256").build(), claims)).getTokenValue();

        return ResponseEntity.ok(new TokenResponse(token, "Bearer", 18000, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String actor = "anonymous";
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                actor = auth.getName();
            }
        } catch (Exception ignored) {
        }

        String ipAddress = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }

        if (!"anonymous".equals(actor) && !"anonymousUser".equalsIgnoreCase(actor)) {
            loginHistoryService.recordAuditEvent(
                    "USER_LOGIN_SUCCESS",
                    actor,
                    "User: " + actor,
                    "User logged out successfully",
                    ipAddress,
                    "SUCCESS"
            );
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    private String resolveDevice(String userAgent) {
        if (userAgent == null) return "Desktop (Unknown)";
        if (userAgent.contains("Windows")) return "Desktop (Windows 11) / Chrome";
        if (userAgent.contains("Macintosh")) return "MacBook (macOS) / Safari";
        if (userAgent.contains("Linux")) return "Desktop (Linux)";
        if (userAgent.contains("Android")) return "Android Device";
        if (userAgent.contains("iPhone")) return "iPhone / Safari";
        return "Web Browser";
    }
}