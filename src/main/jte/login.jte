@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param String error
@param String message
@param boolean guestEnabled

@template.layout(page = new PageSpec("Anmeldung", "Soli"), context = context, login = login, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">Anmelden</h2>
            @if(error != null)
                <div role="alert" class="alert alert-error">
                    <i class="fas fa-xmark"></i>
                    <span>${error}</span>
                </div>
            @endif
            @if(message != null)
                <div role="alert" class="alert alert-info">
                    <i class="fas fa-circle-info"></i>
                    <span>${message}</span>
                </div>
            @endif
            <div class="form-control mt-6">
                <a class="btn btn-primary" href="/oauth2/authorization/kit">per KIT</a>
            </div>
            @if(guestEnabled)
                <div class="form-control mt-6">
                    <a class="btn btn-secondary" href="/login/guest">Anonym</a>
                </div>
            @endif
            <br>
            <details class="collapse collapse-plus bg-base-200">
                <summary class="collapse-title font-medium">Administratoren</summary>
                <form name="f" action="/login" method="POST" class="collapse-content">
                    @if(csrf != null)
                        <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                    @endif
                    <input id="username" name="username" type="hidden" value="admin"/>
                    <div class="form-control mt-6">
                        <label for="password" class="label">
                            <span class="label-text">Passwort</span>
                        </label>
                        <input id="password" name="password" type="password" placeholder="Passwort" class="input input-bordered" required/>
                    </div>
                    <div class="form-control mt-6">
                        <input class="btn btn-secondary" type="submit" value="Anmelden">
                    </div>
                </form>
            </details>
        </div>
    </div>
`)
