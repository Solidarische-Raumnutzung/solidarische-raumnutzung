@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param String guestMarker

@template.layout(page = new PageSpec(context.lookup("login.login"), "Soli"), context = context, login = login,  room = null, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("login.login")}</h2>
            <form name="f" action="/login" method="POST">
                @if(csrf != null)
                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                @endif
                <input id="password" name="password" type="hidden" value="${guestMarker}"/>
                <div class="form-control mt-6">
                    <label for="username" class="label">
                        <span class="label-text">${context.localize("login.email")}</span>
                    </label>
                    <input id="username" name="username" type="email" placeholder="${context.localize("login.email")}" class="input input-bordered" required/>
                </div>
                <div class="form-control mt-6">
                    <input class="btn btn-secondary" type="submit" value="${context.localize("login.login")}">
                </div>
            </form>
        </div>
    </div>
`)
