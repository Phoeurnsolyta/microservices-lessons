package kh.edu.istad.platform.iam.web.controller;

import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ConsentController {

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final I18n i18n;

    @GetMapping("/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state,
            @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode
    ) {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new IllegalArgumentException("Invalid client id: " + clientId);
        }

        OAuth2AuthorizationConsent currentConsent =
                this.authorizationConsentService.findById(
                        registeredClient.getId(),
                        principal.getName()
                );

        Set<String> authorizedScopes = currentConsent != null ?
                currentConsent.getScopes() : Collections.emptySet();

        Set<String> scopesToApprove = new LinkedHashSet<>();
        Set<String> previouslyApprovedScopes = new LinkedHashSet<>();

        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", registeredClient.getClientName());
        model.addAttribute("state", state);
        model.addAttribute("scopesToApprove", scopesToApprove);
        model.addAttribute("previouslyApprovedScopes", previouslyApprovedScopes);
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userCode", userCode);
        model.addAttribute("msg", i18n);

        return "consent";
    }
}
