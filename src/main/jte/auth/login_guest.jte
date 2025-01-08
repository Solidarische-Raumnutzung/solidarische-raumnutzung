@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param String guestMarker

@template.layout(page = new PageSpec(context.lookup("login.login"), "Soli"), context = context, layout = layout, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("login.login")}</h2>
            <p>${context.localize("login.guest.description")}</p>
            @template.util.form(csrf = csrf, action = "/login", method = POST, content = @`
                <input id="password" name="password" type="hidden" value="${guestMarker}"/>
                <input id="remember-me" name="remember-me" type="hidden" value="true">
                <div class="form-control mt-6">
                    <label for="username" class="label">
                        <span class="label-text">${context.localize("login.email")}</span>
                    </label>
                    <input id="username" name="username" type="email" placeholder="${context.localize("login.email")}" class="input input-bordered" required/>
                </div>
                <div class="form-control mt-6">
                    <input class="btn btn-secondary" type="submit" value="${context.localize("login.login")}">
                </div>
            `)
        </div>
    </div>
`)
