@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param CsrfToken csrf
@param String id

@param User user

<legend class="fieldset-legend">${context.localize("generic.warning")}</legend>
<p>${context.localize("administration.user.deactivate.message")}</p>
<div class="modal-action">
    @template.util.modal.close(message = context.localize("generic.cancel"), id = id)
    @template.util.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/" + user.getId() + "/deactivate", method = PUT, content = @`
        <input class="btn btn-warning" type="submit" value="${context.localize("administration.user.deactivate")}" aria-label="${context.localize("administration.user.deactivate")}">
    `)
</div>
