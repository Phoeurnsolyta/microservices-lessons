package kh.edu.istad.platform.iam.web.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import gg.jte.springframework.boot.autoconfigure.JteViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class JteConfig {

    @Bean
    public TemplateEngine templateEngine() {
        Path path1 = Paths.get("iam-web", "src", "main", "jte");
        Path path2 = Paths.get("microservices", "iam-service", "iam-web", "src", "main", "jte");
        Path path3 = Paths.get("src", "main", "jte");

        Path jtePath = path1;
        if (Files.exists(path1)) {
            jtePath = path1;
        } else if (Files.exists(path2)) {
            jtePath = path2;
        } else if (Files.exists(path3)) {
            jtePath = path3;
        }

        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(jtePath);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    @Bean
    public JteViewResolver jteViewResolver(TemplateEngine templateEngine, JteProperties jteProperties) {
        return new JteViewResolver(templateEngine, jteProperties);
    }
}