@import edu.kit.hci.soli.config.template.PageSpec
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.KnownError
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext

@param JteContext context
@param LoginStateModel login
@param KnownError error

@param Room room = null

@template.layout(page = new PageSpec("Not Found", "Soli"), context = context, login = login, room = room, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-5xl font-bold">${context.localize(error.title)}</h1>
            <p class="py-6">${context.localize(error.message)}</p>
            <a class="btn btn-primary" href="/">${context.localize("index.go")}</a>
        </div>
    </div>
`))