@import edu.kit.hci.soli.domain.*
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf
@param Room room

@param Booking booking

@template.layout(page = new PageSpec(context.lookup("bookings.collaboration"), "Soli"), context = context, login = login, room = null, content = @`
    <div class="card bg-base-200 w-80 mx-auto">
        <div class="card-body">
            <h2 class="card-title">${context.localize("bookings.collaboration")}</h2>
            <p>${context.localize("bookings.collaboration.message")}</p>
            @template.bookings.single(context = context, login = login, csrf = csrf, booking = booking, showRequestButton = false, showDeleteButton = false)
            <div class="modal-action">
                <div class="card-actions justify-end">
                    @template.util.form(csrf = csrf, action = "/" + room.getId() + "/bookings/" + booking.getId() + "/collaboration", method = DELETE, content = @`
                        <input class="btn btn-error" type="submit" value="${context.localize("bookings.collaboration.deny")}">
                    `)
                    @template.util.form(csrf = csrf, action = "/" + room.getId() + "/bookings/" + booking.getId() + "/collaboration", method = PUT, content = @`
                        <input class="btn btn-success" type="submit" value="${context.localize("bookings.collaboration.accept")}">
                    `)
                </div>
            </div>
        </div>
    </div>
`)
