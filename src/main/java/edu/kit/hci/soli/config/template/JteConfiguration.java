package edu.kit.hci.soli.config.template;

import edu.kit.hci.soli.SoliApplication;
import edu.kit.hci.soli.config.SoliConfiguration;
import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class JteConfiguration {
    @Bean
    public ViewResolver jteViewResolve(TemplateEngine templateEngine, SoliConfiguration soliConfiguration) {
        return new JteViewResolver(templateEngine, soliConfiguration.getHostname());
    }

    @Bean
    public TemplateEngine templateEngine(SoliConfiguration soliConfiguration) {
        if (soliConfiguration.isDevelopmentMode()) {
            CodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
            TemplateEngine templateEngine = TemplateEngine.create(
                    codeResolver,
                    Paths.get("jte-classes"),
                    ContentType.Html,
                    SoliApplication.class.getClassLoader()
            );
            templateEngine.setBinaryStaticContent(true);
            return templateEngine;
        } else {
            return TemplateEngine.createPrecompiled(ContentType.Html);
        }
    }
}
