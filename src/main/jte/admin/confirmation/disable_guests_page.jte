@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@template.layout(page = new PageSpec(context.lookup("administration.user.guests.disable"), "Soli"), context = context, layout = layout, content = @`
    <div class="card bg-base-200 w-96 mx-auto">
        <div class="card-body">
            @template.admin.confirmation.disable_guests(context = context, csrf = csrf, id = "/admin/users")
        </div>
    </div>
`)
