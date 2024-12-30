@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param CsrfToken csrf
@param String id

<h2 class="card-title">${context.localize("generic.warning")}</h2>
<p>${context.localize("administration.user.guests.disable.description")}</p>
<div class="modal-action">
    @template.util.modal.close(message = context.localize("generic.cancel"), id = id)
    @template.util.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/disable-guests", method = PUT, content = @`
        <input class="btn btn-error" type="submit" value="${context.localize("administration.user.guests.disable")}">
    `)
</div>
