@import edu.kit.hci.soli.domain.LoginStateModel
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
            <h1 class="text-5xl font-bold">Something went wrong</h1>
            <p class="py-6">
                You are seeing this because an error occurred.
            </p>
            <details class="collapse collapse-plus bg-base-200">
                <summary class="collapse-title font-medium">Details</summary>
                <div class="collapse-content">
                    <p>${error == null ? message != null ? message : "Unknown Exception" : error}</p>
                </div>
            </details>
            <a class="btn btn-primary" href="/">Go Home</a>
        </div>
    </div>
`)