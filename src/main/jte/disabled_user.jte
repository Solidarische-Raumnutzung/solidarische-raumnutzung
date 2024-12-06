@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import java.util.Date

@param JteContext context
@param LoginStateModel login

@param Date timestamp

@template.layout(page = new PageSpec("Error", "Soli"), context = context, login = login, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            ${context.localize("user.disable.message")}
        </div>
    </div>
`)