@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param Booking booking
@param Room room
@param boolean showRequestButton
@param boolean showDeleteButton

@template.layout(page = new PageSpec("Terminansicht", "Soli"), context = context, login = login, room = booking.getRoom(), content = @`
    <div class="card bg-neutral text-neutral-content w-96 mx-auto">
        <div class="card-body items-center text-center">
            <%-- TODO we should demarcate staged bookings (IE bookings that have open outstanding requests) --%>
            @template.bookings.single(context = context, login = login, booking = booking, csrf = csrf, showRequestButton = true, showDeleteButton = showDeleteButton)
        </div>
    </div>
`)