package kh.edu.istad.platform.iam.web.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.*;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.*;
import kh.edu.istad.platform.iam.web.dto.ChangePasswordForm;
import kh.edu.istad.platform.iam.web.dto.ConnectedAppDto;
import kh.edu.istad.platform.iam.web.dto.ProfileEditForm;
import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final GetUserUseCase getUserUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final I18n i18n;


    @GetMapping("/")
    public String index(Authentication authentication, Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        UUID userUuid = resolveUserUuid(authentication.getName());

        if (userUuid != null) {
            UserResult user = getUserUseCase.execute(userUuid);
            UserAuthoritiesResult authorities = getUserAuthoritiesUseCase.execute(userUuid);
            model.addAttribute("user", user);
            model.addAttribute("authorities", authorities);
        }
        model.addAttribute("activeTab", "overview");
        model.addAttribute("msg", i18n);

        return "index";
    }

    @GetMapping("/profile/edit")
    public String showEditProfile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        UUID userUuid = resolveUserUuid(authentication.getName());
        UserResult user = getUserUseCase.execute(userUuid);

        ProfileEditForm form = new ProfileEditForm();
        form.setGivenName(user.givenName());
        form.setFamilyName(user.familyName());
        form.setLocale(user.locale());

        model.addAttribute("form", form);
        model.addAttribute("user", user);
        model.addAttribute("activeTab", "editProfile");
        model.addAttribute("msg", i18n);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @ModelAttribute("form") ProfileEditForm form,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        UUID userUuid = resolveUserUuid(authentication.getName());

        try {
            LocalDate birthdate = null;
            if (form.getBirthdate() != null && !form.getBirthdate().isBlank()) {
                birthdate = LocalDate.parse(form.getBirthdate());
            }

            AddressCommand address = null;
            if (form.getStreetAddress() != null || form.getLocality() != null || form.getCountry() != null) {
                address = new AddressCommand(
                        form.getStreetAddress(),
                        form.getLocality(),
                        form.getRegion(),
                        form.getPostalCode(),
                        form.getCountry()
                );
            }

            UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                    userUuid,
                    form.getGivenName(),
                    form.getFamilyName(),
                    form.getMiddleName(),
                    form.getNickname(),
                    form.getGender(),
                    birthdate,
                    form.getPhoneNumber(),
                    form.getZoneinfo(),
                    form.getLocale(),
                    address
            );

            updateUserProfileUseCase.execute(command);
            redirectAttributes.addFlashAttribute(
                    "successMessage", i18n.get("profileEdit.success")
            );

            return "redirect:/profile/edit";
        } catch (IamDomainException ex) {
            UserResult user = getUserUseCase.execute(userUuid);
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("activeTab", "editProfile");
            model.addAttribute("msg", i18n);

            return "profile-edit";
        }
    }

    @GetMapping("/profile/password")
    public String showChangePassword(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        model.addAttribute("form", new ChangePasswordForm());
        model.addAttribute("activeTab", "changePassword");
        model.addAttribute("msg", i18n);
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @Valid @ModelAttribute("form") ChangePasswordForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword", "password.mismatch",
                    "New passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "changePassword");
            model.addAttribute("msg", i18n);
            return "change-password";
        }

        UUID userUuid = resolveUserUuid(authentication.getName());
        try {
            ChangePasswordCommand command = new ChangePasswordCommand(
                    userUuid,
                    form.getCurrentPassword(),
                    form.getNewPassword(),
                    form.getConfirmPassword()
            );
            changePasswordUseCase.execute(command);
            redirectAttributes.addFlashAttribute(
                    "successMessage", i18n.get("changePassword.success"));
            return "redirect:/profile/password";
        } catch (IamDomainException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("activeTab", "changePassword");
            model.addAttribute("msg", i18n);
            return "change-password";
        }
    }

    @GetMapping("/profile/accounts")
    public String listLinkedAccounts(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        UUID userUuid = resolveUserUuid(authentication.getName());
        List<IdentityResult> identities = listUserIdentitiesUseCase.execute(userUuid);

        model.addAttribute("identities", identities);
        model.addAttribute("activeTab", "linkedAccounts");
        model.addAttribute("msg", i18n);
        return "linked-accounts";
    }

    @PostMapping("/profile/accounts/{identityUuid}/unlink")
    public String unlinkAccount(
            @PathVariable("identityUuid") UUID identityUuid,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        UUID userUuid = resolveUserUuid(authentication.getName());
        try {
            unlinkIdentityUseCase.execute(userUuid, identityUuid);
            redirectAttributes
                    .addFlashAttribute("successMessage",
                            i18n.get("linkedAccounts.unlinkedSuccess")
                    );
        } catch (IamDomainException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/profile/accounts";
    }

    @GetMapping("/profile/apps")
    public String listConnectedApps(Authentication authentication, Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String principalName = authentication.getName();

        List<ConnectedAppDto> apps = new ArrayList<>();
        // Check for registered clients in authorization server
        List<String> clientIds = List.of("oidc-client", "istad-client");
        for (String cId : clientIds) {
            try {
                RegisteredClient client = registeredClientRepository.findByClientId(cId);
                if (client != null) {
                    OAuth2AuthorizationConsent consent = authorizationConsentService
                            .findById(client.getId(), principalName);

                    if (consent != null) {
                        Set<String> scopes = consent.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());
                        apps.add(new ConnectedAppDto(
                                client.getId(),
                                client.getClientId(),
                                client.getClientName(),
                                scopes)
                        );
                    }
                }
            } catch (Exception e) {
                log.warn("Could not check consent for client {}: {}", cId, e.getMessage());
            }
        }

        model.addAttribute("apps", apps);
        model.addAttribute("activeTab", "connectedApps");
        model.addAttribute("msg", i18n);

        return "connected-apps";
    }

    @PostMapping("/profile/apps/{clientId}/revoke")
    public String revokeAppConsent(
            @PathVariable("clientId") String clientId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String principalName = authentication.getName();
        try {
            RegisteredClient client = registeredClientRepository.findByClientId(clientId);
            if (client != null) {
                OAuth2AuthorizationConsent consent = authorizationConsentService.
                        findById(client.getId(), principalName);

                if (consent != null) {
                    authorizationConsentService.remove(consent);
                    redirectAttributes
                            .addFlashAttribute("successMessage",
                                    i18n.get("connectedApps.revokedSuccess")
                            );

                    return "redirect:/profile/apps";
                }
            }
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Application not found or already revoked");
        } catch (Exception e) {
            log.error("Failed to revoke consent for client {}: {}", clientId, e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Failed to revoke access: " + e.getMessage());
        }
        return "redirect:/profile/apps";
    }

    private UUID resolveUserUuid(String identifier) {
        try {
            return UUID.fromString(identifier);
        } catch (IllegalArgumentException e) {
            try {
                return loadUserForAuthenticationUseCase.execute(identifier).uuid();
            } catch (Exception ex) {
                return null;
            }
        }
    }
}