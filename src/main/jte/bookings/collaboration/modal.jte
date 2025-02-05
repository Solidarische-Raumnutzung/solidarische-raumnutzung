@import edu.kit.hci.soli.domain.*
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Booking booking

@template.layout(page = context.page("bookings.collaboration"), context = context, layout = layout, content = @`
    <div class="card bg-base-200 border border-base-300 w-96 mx-auto">
        <div class="card-body items-center text-center">
            <p class="fieldset-legend">${context.localize("bookings.collaboration")}</p>
            <p>${context.localize("bookings.collaboration.message")}</p>

            @template.bookings.single(context = context, csrf = csrf, booking = booking, allowEditing = false, showRequestButton = false)

            <div class="modal-action">
                <div class="card-actions justify-end">
                    @template.util.form(csrf = csrf, action = "/" + layout.getRoom().getId() + "/bookings/" + booking.getId() + "/collaboration", method = DELETE, content = @`
                        <input class="btn btn-error" type="submit" value="${context.localize("bookings.collaboration.deny")}">
                    `)
                    @template.util.form(csrf = csrf, action = "/" + layout.getRoom().getId() + "/bookings/" + booking.getId() + "/collaboration", method = PUT, content = @`
                        <input class="btn btn-success" type="submit" value="${context.localize("bookings.collaboration.accept")}">
                    `)
                </div>
            </div>
        </div>
    </div>
`)
