package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TokenConfig {
    private final GetUserUseCase getUserUseCase;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            if (principal == null) return;
            // 1. Customize Access Token: add roles and permissions
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Set<String> authorities = principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                Set<String> roles = authorities.stream()
                        .filter(auth -> auth.startsWith("ROLE_"))
                        .map(auth -> auth.substring(5))
                        .collect(Collectors.toSet());
                Set<String> permissions = authorities.stream()
                        .filter(auth -> !auth.startsWith("ROLE_"))
                        .collect(Collectors.toSet());
                context.getClaims().claims(claims -> {
                    claims.put("authorities", authorities);
                    claims.put("roles", roles);
                    claims.put("permissions", permissions);
                });
            }
            // 2. Customize ID Token: standard OIDC claims
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                try {
                    UUID userUuid = UUID.fromString(principal.getName());
                    UserResult user = getUserUseCase.execute(userUuid);
                    context.getClaims().claims(claims -> {
                        claims.put("sub", user.uuid().toString());
                        claims.put("preferred_username", user.username());
                        Set<String> authorizedScopes = context.getAuthorizedScopes();
                        if (authorizedScopes.contains(OidcScopes.PROFILE)) {
                            claims.put("name", user.name());
                            claims.put("given_name", user.givenName());
                            claims.put("family_name", user.familyName());
                            claims.put("locale", user.locale());
                        }
                        if (authorizedScopes.contains(OidcScopes.EMAIL)) {
                            claims.put("email", user.email());
                            claims.put("email_verified", user.emailVerified());
                        }
                    });
                } catch (IllegalArgumentException ignored) {
                    // Client credentials or non-user principal
                }
            }
        };
    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
//        return context -> {
//            Authentication authentication = context.getPrincipal();
//            log.info("Authentication: {}", authentication);
//            if(authentication == null){
//                return;
//            }
//            var principal = authentication.getPrincipal();
//            log.info("Principle : {}", principal);
//            System.out.println(principal);
//            if(principal instanceof UserDetails user){
//
//                if(OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())){
//                    context.getClaims()
//                            .claims(claim -> {
//                                claim.put("preferred_username", user.getUsername());
//                                claim.put("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
//                            });
//                }
//            }
//        };
//    }
}
