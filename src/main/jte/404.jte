@import edu.kit.hci.soli.domain.LoginStateModel
@import edu.kit.hci.soli.template.JteContext

@param JteContext context
@param LoginStateModel login

@template.layout(page = new edu.kit.hci.soli.template.PageSpec("Not Found", "Soli"), context = context, login = login, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-5xl font-bold">404 - Page Not Found</h1>
            <p class="py-6">
                Oops! The page you are looking for does not exist.
            </p>
            <a class="btn btn-primary" href="/">Go Home</a>
        </div>
    </div>
`))