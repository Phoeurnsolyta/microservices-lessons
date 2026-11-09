package kh.edu.istad.platform.iam.web.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.RegisterUserCommand;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.platform.iam.web.dto.RegisterForm;
import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;
    private final I18n i18n;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        model.addAttribute("msg", i18n);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("form") RegisterForm form,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("msg", i18n);

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch", "Passwords do not match"
            );
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            RegisterUserCommand command = new RegisterUserCommand(
                    form.getUsername(),
                    form.getEmail(),
                    form.getPassword(),
                    form.getConfirmPassword(),
                    form.getGivenName(),
                    form.getFamilyName(),
                    form.getLocale() != null ? form.getLocale() : "en"
            );
            registerUserUseCase.execute(command);

            return "redirect:/login?registered=true";
        } catch (IamDomainException ex) {
            model.addAttribute("errorMessage", ex.getMessage());

            return "register";
        }
    }
}
