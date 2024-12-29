@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@template.layout(page = new PageSpec(context.lookup("login.login"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("administration.user.guests.disable")}</h2>
            <p>${context.localize("administration.user.guests.disable.description")}</p>
            <form name="f" action="/admin/users/disable-guests" method="POST" class="card-actions justify-end">
                @if(csrf != null)
                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                @endif
                <input type="hidden" name="_method" value="put"/>
                <input class="btn btn-error" type="submit" value="${context.localize("administration.user.guests.disable")}">
            </form>
        </div>
    </div>
`)
