@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param User user

@template.layout(page = new PageSpec(context.lookup("generic.warning"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("generic.warning")}</h2>
            <p>${context.localize("disable.user.disable.are_you_sure")}</p>
            <div class="modal-action">
                <a class="btn" href="/admin/users">${context.localize("generic.cancel")}</a>
                @template.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/" + user.getId() + "/deactivate", method = PUT, content = @`
                    <input class="btn btn-warning" type="submit" value="${context.localize("administration.user.deactivate")}">
                `)
            </div>
        </div>
    </div>
`)
