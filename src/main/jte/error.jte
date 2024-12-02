@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec
@import java.util.Date

@param JteContext context
@param LoginStateModel login

@param Date timestamp
@param Integer status
@param String message
@param String error

@template.layout(page = new PageSpec("Error", "Soli"), context = context, login = login, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-5xl font-bold">${context.localize("error.unknown.title")}</h1>
            <p class="py-6">${context.localize("error.unknown.message")}</p>
            <details class="collapse collapse-plus bg-base-200">
                <summary class="collapse-title font-medium">${context.localize("error.unknown.details")}</summary>
                <div class="collapse-content">
                    <p>${error == null ? message != null ? message : context.lookup("error.unknown.fallback") : error}</p>
                </div>
            </details>
            <a class="btn btn-primary" href="/">${context.localize("index.go")}</a>
        </div>
    </div>
`)