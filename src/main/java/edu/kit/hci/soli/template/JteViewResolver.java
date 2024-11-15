package edu.kit.hci.soli.template;

import gg.jte.TemplateEngine;
import lombok.NonNull;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JteViewResolver extends AbstractTemplateViewResolver {
    private final TemplateEngine templateEngine;

    public JteViewResolver(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.setViewClass(this.requiredViewClass());
        this.setSuffix(".jte");
        this.setViewClass(JteView.class);
        this.setContentType(MediaType.TEXT_HTML_VALUE);
        this.setOrder(Ordered.HIGHEST_PRECEDENCE);
        this.setExposeRequestAttributes(false);
    }

    @Override @NonNull
    protected AbstractUrlBasedView instantiateView() {
        return new JteView(templateEngine);
    }

    @Override @NonNull
    protected Class<?> requiredViewClass() {
        return JteView.class;
    }
}
