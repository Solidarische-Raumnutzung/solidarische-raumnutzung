@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param CsrfToken csrf
@param String id

@param Room room

<legend class="fieldset-legend">${context.localize("generic.warning")}</legend>
<p>${context.localize("rooms.delete.message")}</p>
<div class="modal-action">
    @template.util.modal.close(message = context.localize("generic.cancel"), id = id)
    @template.util.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/rooms/" + room.getId(), method = DELETE, content = @`
        <input class="btn btn-error" type="submit" value="${context.localize("rooms.delete")}">
    `)
</div>
