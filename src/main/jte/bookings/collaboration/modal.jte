@import edu.kit.hci.soli.domain.*
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Booking booking

@template.layout(page = new PageSpec(context.lookup("bookings.collaboration"), "Soli"), context = context, layout = layout, content = @`
    <fieldset class="fieldset bg-base-200 w-80 mx-auto">
        <legend class="fieldset-legend">${context.localize("bookings.collaboration")}</legend>
        <p>${context.localize("bookings.collaboration.message")}</p>
        @template.bookings.single(context = context, csrf = csrf, booking = booking, showRequestButton = false, showDeleteButton = false)
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
    </fieldset>
`)
