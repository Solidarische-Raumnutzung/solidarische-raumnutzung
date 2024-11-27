@import edu.kit.hci.soli.domain.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec

@param JteContext context
@param LoginStateModel login
@template.layout(page = new PageSpec("Anmeldung", "Soli"), context = context, login = login, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">Anmelden</h2>
            <div class="form-control mt-6">
                <a class="btn btn-primary" href="/oauth2/authorization/kit">per KIT</a>
            </div>
            <div class="form-control mt-6">
                <button class="btn btn-secondary">Anonym</button>
            </div>
            <br>
            <details class="collapse collapse-plus bg-base-200">
                <summary class="collapse-title font-medium">Administratoren</summary>
                <form name="f" action="/login" method="POST" class="collapse-content">
                    <div class="form-control mt-6">
                        <label for="email" class="label">
                            <span class="label-text">Benutzername</span>
                        </label>
                        <input id="text" type="text" placeholder="Benutzername" class="input input-bordered" required/>
                    </div>
                    <div class="form-control mt-6">
                        <label for="password" class="label">
                            <span class="label-text">Passwort</span>
                        </label>
                        <input id="password" type="password" placeholder="Passwort" class="input input-bordered" required/>
                    </div>
                    <div class="form-control mt-6">
                        <button class="btn btn-secondary" type="submit">Anmelden</button>
                    </div>
                </form>
            </details>
        </div>
    </div>
`)
