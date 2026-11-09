package kh.edu.istad.platform.iam.web.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("msg")
@RequiredArgsConstructor
public class I18n {

    private final MessageSource messageSource;

    public String get(String code, Object... args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, code, currentLocale);
    }

    public String getCurrentLang() {
        return LocaleContextHolder.getLocale().getLanguage();
    }
}
