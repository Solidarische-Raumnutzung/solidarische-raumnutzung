@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param List<User> users
@param boolean guestsEnabled

@template.layout(page = new PageSpec(context.lookup("administration.user"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="overflow-x-auto">
        @if (guestsEnabled)
            <a class="btn btn-error" href="/admin/users/disable-guests">${context.localize("administration.user.guests.disable")}</a>
        @else
            @template.form(csrf = csrf, cssClass = "card-actions justify-end", action = "/admin/users/enable-guests", method = PUT, content = @`
                <input class="btn btn-info" type="submit" value="${context.localize("administration.user.guests.enable")}">
            `)
        @endif
        @if (users.isEmpty())
            <div class="card">
                <div class="card-body">
                    <p class="text-center">${context.localize("disabled.user.no_users_in_db")}</p>
                </div>
            </div>
        @else
            <table class="table table-zebra">
                <thead>
                <tr>
                    <th></th>
                    <th>${context.localize("administration.user.username")}</th>
                    <th>${context.localize("administration.user.email")}</th>
                    <th>${context.localize("administration.user.user_id")}</th>
                    <th>${context.localize("administration.user.is_disabled")}</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                @for (User user : users)
                    <tr>
                        <td>${user.getId()}</td>
                        <td>${user.getUsername()}</td>
                        <td>${user.getEmail()}</td>
                        <td>${user.getUserId()}</td>
                        <td>${user.isDisabled()}</td>
                        <td>
                            @if (user.isDisabled())
                                @template.form(csrf = csrf, action = "/admin/users/" + user.getId() + "/reactivate", method = PUT, content = @`
                                    <button type="submit" class="btn btn-ghost">${context.localize("administration.user.reactivate")}</button>
                                `)
                            @else
                                <%-- TODO: ideally, the code for the modal and non-modal versions should be unified and the same approach extended to disabling guest accounts --%>
                                <noscript>
                                    <a href="/admin/users/${user.getId()}/deactivate" id="initial" class="btn btn-ghost">${context.localize("administration.user.deactivate")}</a>
                                </noscript>

                                <a onclick="sure_modal.showModal()" id="initial" class="btn btn-ghost needs-js">${context.localize("administration.user.deactivate")}</a>
                                <dialog id="sure_modal" class="modal">
                                    <div class="modal-box">
                                        <h3 class="text-lg font-bold">${context.localize("generic.warning")}</h3>
                                        <p class="py-4">${context.localize("disable.user.disable.are_you_sure")}</p>
                                        <div class="modal-action">
                                            <form method="dialog">
                                                <a onclick="sure_modal.close()" class="btn">${context.localize("generic.close")}</a>
                                            </form>
                                            @template.form(csrf = csrf, action = "/admin/users/" + user.getId() + "/deactivate", method = PUT, content = @`
                                                <button type="submit" class="btn btn-warning">${context.localize("administration.user.deactivate")}</button>
                                            `)
                                        </div>
                                    </div>
                                </dialog>
                            @endif
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`)
