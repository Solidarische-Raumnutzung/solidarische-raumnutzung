package edu.kit.hci.soli.config.template;

import edu.kit.hci.soli.config.SoliConfiguration;
import gg.jte.TemplateEngine;
import lombok.NonNull;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JteViewResolver extends AbstractTemplateViewResolver {
    private final TemplateEngine templateEngine;
    private final SoliConfiguration soliConfiguration;

    public JteViewResolver(TemplateEngine templateEngine, SoliConfiguration soliConfiguration) {
        this.templateEngine = templateEngine;
        this.soliConfiguration = soliConfiguration;
        this.setViewClass(this.requiredViewClass());
        this.setSuffix(".jte");
        this.setViewClass(JteView.class);
        this.setContentType(MediaType.TEXT_HTML_VALUE);
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
        this.setExposeRequestAttributes(false);
    }

    @Override
    protected @NonNull AbstractUrlBasedView instantiateView() {
        return new JteView(templateEngine, soliConfiguration);
    }

    @Override
    protected @NonNull Class<?> requiredViewClass() {
        return JteView.class;
    }
}
