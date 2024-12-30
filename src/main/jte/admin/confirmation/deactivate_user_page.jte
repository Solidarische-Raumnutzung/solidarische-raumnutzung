@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param User user

@template.layout(page = new PageSpec(context.lookup("generic.warning"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            @template.admin.confirmation.deactivate_user(context = context, csrf = csrf, id = "/admin/users", user = user)
        </div>
    </div>
`)
