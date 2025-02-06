@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Room room

@template.layout(page = context.page("rooms.delete"), context = context, layout = layout, content = @`
    <fieldset class="fieldset bg-base-200 w-80 mx-auto">
        @template.admin.confirmation.delete_room(context = context, csrf = csrf, id = "/admin/rooms", room = room)
    </fieldset>
`)
