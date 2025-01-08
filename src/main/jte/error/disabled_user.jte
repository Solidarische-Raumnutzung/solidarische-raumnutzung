@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import java.util.Date

@param JteContext context
@param LayoutParams layout
@param String message
@param String error
@param Date timestamp

@template.layout(page = new PageSpec("Error", "Soli"), context = context, layout = layout, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-5xl font-bold">${context.localize("error.unknown.title")}</h1>
            <p class="py-6">${context.localize("error.unknown.message")}</p>
            <details class="collapse collapse-plus bg-base-200">
                <summary class="collapse-title font-medium">${context.localize("error.unknown.details")}</summary>
                <div class="collapse-content">
                    ${context.localize("user.deactivated.message")}
                </div>
            </details>
            <a class="btn btn-primary" href="/">${context.localize("index.go")}</a>
        </div>
    </div>
`)