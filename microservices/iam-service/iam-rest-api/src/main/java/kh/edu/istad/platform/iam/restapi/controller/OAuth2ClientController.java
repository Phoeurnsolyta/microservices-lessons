package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.restapi.dto.request.CreateOAuth2ClientRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.OAuth2ClientResponse;
import kh.edu.istad.platform.iam.restapi.service.RedisLoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class OAuth2ClientController {

    private final DataSource dataSource;
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisLoginHistoryService loginHistoryService;

    private String resolveCurrentActor() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().isBlank()) {
                return auth.getName();
            }
        } catch (Exception ignored) {
        }
        return "admin";
    }

    private List<String> parseCsv(String val) {
        if (val == null || val.isBlank()) return List.of();
        return Arrays.stream(val.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<OAuth2ClientResponse> getClients() {
        List<OAuth2ClientResponse> result = new ArrayList<>();
        String sql = "SELECT id, client_id, client_name, client_secret, authorization_grant_types, redirect_uris, scopes " +
                "FROM oauth2_registered_client ORDER BY client_id_issued_at DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String clientId = rs.getString("client_id");
                String clientName = rs.getString("client_name");
                String secret = rs.getString("client_secret");
                String grantTypesRaw = rs.getString("authorization_grant_types");
                String redirectUrisRaw = rs.getString("redirect_uris");
                String scopesRaw = rs.getString("scopes");

                result.add(new OAuth2ClientResponse(
                        id,
                        clientId,
                        (clientName != null && !clientName.isBlank()) ? clientName : clientId,
                        secret != null ? secret : "••••••••",
                        parseCsv(grantTypesRaw),
                        parseCsv(scopesRaw),
                        parseCsv(redirectUrisRaw)
                ));
            }
        } catch (Exception e) {
            log.error("Failed to query oauth2_registered_client from database", e);
        }
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public OAuth2ClientResponse registerClient(@Valid @RequestBody CreateOAuth2ClientRequest request) {
        String id = UUID.randomUUID().toString();
        String safeName = (request.clientName() != null && !request.clientName().isBlank())
                ? request.clientName().trim()
                : request.clientId().trim();

        RegisteredClient.Builder builder = RegisteredClient.withId(id)
                .clientId(request.clientId().trim())
                .clientName(safeName)
                .clientIdIssuedAt(Instant.now())
                .clientSecret(passwordEncoder.encode(request.clientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);

        if (request.grantTypes() != null && !request.grantTypes().isEmpty()) {
            for (String gt : request.grantTypes()) {
                builder.authorizationGrantType(new AuthorizationGrantType(gt.trim()));
            }
        } else {
            builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
            builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
        }

        if (request.scopes() != null && !request.scopes().isEmpty()) {
            for (String scope : request.scopes()) {
                builder.scope(scope.trim());
            }
        } else {
            builder.scope(OidcScopes.OPENID);
            builder.scope(OidcScopes.PROFILE);
            builder.scope(OidcScopes.EMAIL);
        }

        if (request.redirectUris() != null) {
            for (String uri : request.redirectUris()) {
                if (!uri.isBlank()) {
                    builder.redirectUri(uri.trim());
                }
            }
        }

        builder.clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .build());

        builder.tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(15))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .reuseRefreshTokens(true)
                .build());

        RegisteredClient registeredClient = builder.build();
        registeredClientRepository.save(registeredClient);

        // Record real dynamic audit log into Redis
        loginHistoryService.recordAuditEvent(
                "CLIENT_REGISTERED",
                resolveCurrentActor(),
                "Client: " + request.clientId(),
                "Registered new OAuth2 client @" + request.clientId(),
                "127.0.0.1",
                "SUCCESS"
        );

        return new OAuth2ClientResponse(
                id,
                request.clientId(),
                safeName,
                request.clientSecret(),
                request.grantTypes() != null ? request.grantTypes() : List.of("authorization_code", "refresh_token"),
                request.scopes() != null ? request.scopes() : List.of("openid", "profile", "email"),
                request.redirectUris() != null ? request.redirectUris() : List.of()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteClient(@PathVariable("id") String id) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM oauth2_authorization WHERE registered_client_id = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM oauth2_registered_client WHERE id = ? OR client_id = ?")) {
                ps.setString(1, id);
                ps.setString(2, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            log.error("Failed to delete client {}", id, e);
        }

        loginHistoryService.recordAuditEvent(
                "CLIENT_DELETED",
                resolveCurrentActor(),
                "Client: " + id,
                "Deleted OAuth2 client registration " + id,
                "127.0.0.1",
                "SUCCESS"
        );
    }
}