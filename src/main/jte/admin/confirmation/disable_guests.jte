@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@template.layout(page = new PageSpec(context.lookup("administration.user.guests.disable"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="card bg-base-200 w-96 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("generic.warning")}</h2>
            <p>${context.localize("administration.user.guests.disable.description")}</p>
            <div class="modal-action">
                <a class="btn" href="/admin/users">${context.localize("generic.cancel")}</a>
                @template.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/disable-guests", method = PUT, content = @`
                    <input class="btn btn-error" type="submit" value="${context.localize("administration.user.guests.disable")}">
                `)
            </div>
        </div>
    </div>
`)
