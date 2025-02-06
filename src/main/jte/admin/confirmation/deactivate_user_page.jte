@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param User user

@template.layout(page = context.page("generic.warning"), context = context, layout = layout, content = @`
    <fieldset class="fieldset bg-base-200 w-80 mx-auto">
        @template.admin.confirmation.deactivate_user(context = context, csrf = csrf, id = "/admin/users", user = user)
    </fieldset>
`)
