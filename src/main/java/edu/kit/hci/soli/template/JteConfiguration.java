package edu.kit.hci.soli.template;

import edu.kit.hci.soli.SoliApplication;
import gg.jte.*;
import gg.jte.resolve.DirectoryCodeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class JteConfiguration {
    @Bean
    public ViewResolver jteViewResolve(TemplateEngine templateEngine) {
        return new JteViewResolver(templateEngine);
    }

    @Bean
    public TemplateEngine templateEngine() {
        String profile = System.getenv("SPRING_ENV");
        if ("prod".equals(profile)) {
            return TemplateEngine.createPrecompiled(ContentType.Html);
        } else {
            CodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
            TemplateEngine templateEngine = TemplateEngine.create(
                    codeResolver,
                    Paths.get("jte-classes"),
                    ContentType.Html,
                    SoliApplication.class.getClassLoader()
            );
            templateEngine.setBinaryStaticContent(true);
            return templateEngine;
        }
    }
}
