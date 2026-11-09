package kh.edu.istad.platform.iam.web.controller;

import kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserAuthoritiesUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.GetUserUseCase;
import kh.edu.istad.platform.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final GetUserUseCase getUserUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
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
        model.addAttribute("msg", i18n);
        return "index";
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
