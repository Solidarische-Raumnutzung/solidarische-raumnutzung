package edu.kit.hci.soli;

import gg.jte.*;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.springframework.boot.autoconfigure.JteViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.nio.file.Path;

@Configuration
public class JteConfiguration {

    @Bean
    public ViewResolver jteViewResolve(TemplateEngine templateEngine) {
        return new JteViewResolver(templateEngine, ".jte");
    }

    @Bean
    public TemplateEngine templateEngine() {
        String profile = System.getenv("SPRING_ENV");
        if ("prod".equals(profile)) {
            return TemplateEngine.createPrecompiled(ContentType.Html);
        } else {
            CodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
            TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
            templateEngine.setBinaryStaticContent(true);
            return templateEngine;
        }
    }
}
