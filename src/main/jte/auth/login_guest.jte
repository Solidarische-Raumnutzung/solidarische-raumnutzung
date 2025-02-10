@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param String guestMarker

@template.layout(page = context.page("login.login"), context = context, layout = layout, content = @`
    <fieldset class="fieldset w-80 mx-auto bg-base-200 border border-base-300 p-4 rounded-box">
        <legend class="fieldset-legend">${context.localize("login.login")}</legend>
        <p>${context.localize("login.guest.description")}</p>
        <br>
        @template.util.form(csrf = csrf, action = "/login", method = POST, content = @`
            <input id="password" name="password" type="hidden" value="${guestMarker}"/>
            <input id="remember-me" name="remember-me" type="hidden" value="true">

            <label class="input" for="username">
                <i class="fas fa-envelope"></i>
                <input id="username" name="username" type="email" placeholder="${context.localize("login.email")}" class="validator" required/>
            </label>

            <input class="input btn btn-secondary mt-4" type="submit" value="${context.localize("login.login")}">
        `)
    </fieldset>
`)
