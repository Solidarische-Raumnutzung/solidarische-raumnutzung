@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param CsrfToken csrf
@param String id

<legend class="fieldset-legend">${context.localize("generic.warning")}</legend>
<p>${context.localize("administration.user.guests.disable.description")}</p>
<div class="modal-action">
    @template.util.modal.close(message = context.localize("generic.cancel"), id = id)
    @template.util.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/disable-guests", method = PUT, content = @`
        <input class="btn btn-error" type="submit" value="${context.localize("administration.user.guests.disable")}">
    `)
</div>
