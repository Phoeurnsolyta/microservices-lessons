package kh.edu.istad.platform.iam.restapi.controller;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        UserResult result = registerUserUseCase.execute(userWebMapper.toCommand(request));
        return userWebMapper.toResponse(result);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        AuthUserResult user = loadUserForAuthenticationUseCase.execute(request.usernameOrEmail());

        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

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

        return new TokenResponse(token, "Bearer", 18000);
    }
}