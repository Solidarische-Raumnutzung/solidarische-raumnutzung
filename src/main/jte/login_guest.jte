@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param String guestMarker

@template.layout(page = new PageSpec("Anmeldung", "Soli"), context = context, login = login, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">Anmelden</h2>
            <form name="f" action="/login" method="POST">
                @if(csrf != null)
                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                @endif
                <input id="password" name="password" type="hidden" value="${guestMarker}"/>
                <div class="form-control mt-6">
                    <label for="username" class="label">
                        <span class="label-text">E-Mail</span>
                    </label>
                    <input id="username" name="username" type="email" placeholder="E-Mail" class="input input-bordered" required/>
                </div>
                <div class="form-control mt-6">
                    <input class="btn btn-secondary" type="submit" value="Anmelden">
                </div>
            </form>
        </div>
    </div>
`)
