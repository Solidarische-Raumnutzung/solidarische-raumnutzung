package edu.kit.hci.soli.config.template;

import gg.jte.TemplateEngine;
import lombok.NonNull;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JteViewResolver extends AbstractTemplateViewResolver {
    private final TemplateEngine templateEngine;
    private final String hostname;

    public JteViewResolver(TemplateEngine templateEngine, String hostname) {
        this.templateEngine = templateEngine;
        this.hostname = hostname;
        this.setViewClass(this.requiredViewClass());
        this.setSuffix(".jte");
        this.setViewClass(JteView.class);
        this.setContentType(MediaType.TEXT_HTML_VALUE);
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
        this.setExposeRequestAttributes(false);
    }

    @Override
    protected @NonNull AbstractUrlBasedView instantiateView() {
        return new JteView(templateEngine, hostname);
    }

    @Override
    protected @NonNull Class<?> requiredViewClass() {
        return JteView.class;
    }
}
