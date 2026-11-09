package kh.edu.istad.platform.iam.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.FederatedIdentityCommand;
import kh.edu.istad.platform.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TelegramAuthController {
    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    @Value("${telegram.bot.token:}")
    private String botToken;
    @Value("${telegram.bot.username:}")
    private String botUsername;

    /**
     * Fallback for direct /oauth2/authorization/telegram clicks
     */
    @GetMapping("/oauth2/authorization/telegram")
    public String redirectTelegramAuth() {
        if (botUsername != null && !botUsername.isBlank()) {
            return "redirect:https://t.me/" + botUsername;
        }
        return "redirect:/login";
    }

    /**
     * Handles the callback from Telegram Login Widget
     */
    @GetMapping("/login/telegram/callback")
    public String telegramCallback(
            @RequestParam Map<String, String> allParams,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("Received Telegram login callback: {}", allParams);
        if (!verifyTelegramAuth(allParams, botToken)) {
            log.warn("Telegram authentication verification failed");
            return "redirect:/login?error=telegram_failed";
        }
        String id = allParams.get("id");
        String firstName = allParams.get("first_name");
        String lastName = allParams.get("last_name");
        String username = allParams.get("username");
        String photoUrl = allParams.get("photo_url");
        String displayName = firstName != null
                ? (lastName != null ? firstName + " " + lastName : firstName)
                : (username != null ? username : "Telegram User");
        String effectiveUsername = username != null ? username : "tg_" + id;
        String email = effectiveUsername + "@telegram.org";
        Map<String, Object> attributes = allParams.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        FederatedIdentityCommand command = new FederatedIdentityCommand(
                "TELEGRAM",
                id,
                email,
                true,
                displayName,
                effectiveUsername,
                photoUrl,
                attributes
        );
        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);
        List<SimpleGrantedAuthority> authorities = authUser.authorities() != null
                ? authUser.authorities().stream().map(SimpleGrantedAuthority::new).toList()
                : List.of();
        UserDetails userDetails = User.builder()
                .username(authUser.username())
                .password("")
                .authorities(authorities)
                .build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        log.info("Telegram authentication successful for user: {}", authUser.username());
        return "redirect:/";
    }

    private boolean verifyTelegramAuth(Map<String, String> params, String token) {
        if (token == null || token.isBlank()) {
            log.error("Telegram bot token is not configured");
            return false;
        }
        String receivedHash = params.get("hash");
        if (receivedHash == null || receivedHash.isBlank()) {
            return false;
        }
        // Build data_check_string from alphabetically sorted parameters excluding hash
        Map<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.remove("hash");
        String dataCheckString = sortedParams.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
        try {
            // secret_key = SHA256(botToken)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            // HMAC-SHA256(dataCheckString, secret_key)
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hmac = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));
            String calculatedHash = HexFormat.of().formatHex(hmac);
            return calculatedHash.equalsIgnoreCase(receivedHash);
        } catch (Exception e) {
            log.error("Error during Telegram HMAC verification: {}", e.getMessage(), e);
            return false;
        }
    }
}
