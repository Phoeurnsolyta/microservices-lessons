package kh.edu.istad.platform.iam.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.platform.iam.web.i18n.I18n;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "kh.edu.istad.platform.iam.web.controller")
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final I18n i18n;

    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @ModelAttribute("msg")
    public I18n i18n() {
        return i18n;
    }
}
