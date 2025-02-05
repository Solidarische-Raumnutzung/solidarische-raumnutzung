@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@template.layout(page = context.page("administration.user.guests.disable"), context = context, layout = layout, content = @`
    <fieldset class="fieldset bg-base-200 w-96 mx-auto">
        @template.admin.confirmation.disable_guests(context = context, csrf = csrf, id = "/admin/users")
    </fieldset>
`)
