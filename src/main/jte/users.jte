@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf
@param List<User> users

@template.layout(page = new PageSpec("Benutzer Verwaltung", "Soli"), context = context, login = login, content = @`
    <div class="overflow-x-auto">
        @if(users.isEmpty())
            <div class="card">
                <div class="card-body">
                    <p class="text-center">Es befinden sich keine Nutzer in der Datenbank</p>
                </div>
            </div>
        @else
            <table class="table table-zebra">
                <thead>
                <tr>
                    <th></th>
                    <th>${context.localize("user.administration.username")}</th>
                    <th>${context.localize("user.administration.email")}</th>
                    <th>${context.localize("user.administration.user_id")}</th>
                    <th>${context.localize("user.administration.is_disabled")}</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                @for(User user : users)
                    <tr>
                        <td>${user.getId()}</td>
                        <td>${user.getUsername()}</td>
                        <td>${user.getEmail()}</td>
                        <td>${user.getUserId()}</td>
                        <td>${user.isDisabled()}</td>
                        <td>
                            <form action="/admin/users/deactivate/${user.getId()}" method="post">
                                @if(csrf != null)
                                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                                @endif
                                <input type="hidden" name="_method" value="put" />
                                @if(user.isDisabled())
                                    <button type="submit" class="btn btn-ghost">${context.localize("user.administration.allow")}</button>
                                @else
                                    <button type="submit" class="btn btn-ghost">${context.localize("user.administration.disable")}</button>
                                @endif
                            </form>
                        </td>

                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`)
