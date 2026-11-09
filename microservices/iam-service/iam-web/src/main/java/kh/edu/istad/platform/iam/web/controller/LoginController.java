package kh.edu.istad.platform.iam.web.controller;

import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final I18n i18n;

    @Value("${telegram.bot.username:}")
    private String telegramBotUsername;

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout,
            @RequestParam(name = "registered", required = false) String registered,
            Model model
    ) {
        model.addAttribute("hasError", error != null);
        model.addAttribute("isLogout", logout != null);
        model.addAttribute("isRegistered", registered != null);
        model.addAttribute("telegramBotUsername", telegramBotUsername);
        model.addAttribute("msg", i18n);
        return "login";
    }
}
