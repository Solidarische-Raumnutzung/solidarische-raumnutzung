@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.BookingDeleteReason

@param JteContext context
@param String css

@param Booking booking
@param BookingDeleteReason reason

@template.mail.layout(title = context.lookup("mail.booking_deleted.heading"), context = context, css = css, content = @`
    <p>
        ${context.localize("mail.booking_deleted.message", booking.getUser().getUsername(), booking.getRoom().getId().toString(), booking.getStartDate().toString())}
    </p>
    <p>
        ${context.localize("bookings.delete_reason." + reason)}
    </p>
    <p>
        ${context.localize("mail.booking_deleted.last_phrase")}<br>
        ${context.localize("mail.ending")}
    </p>
    <p>
        <a class="btn btn-accent" href="${context.getHostname()}${booking.getRoom().getId()}">
            ${context.localize("mail.make_new_booking")}
        </a>
    </p>
`)
