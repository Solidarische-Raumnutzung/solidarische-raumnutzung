@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.BookingDeleteReason

@param JteContext context
@param String css

@param Booking booking
@param BookingDeleteReason reason

@template.mail.layout(title= context.lookup("mail.booking_deleted.heading"), content = @`
    !{var deleteReason = switch (reason) {
                    case SELF -> context.localize("bookings.delete_reason.SELF");
                    case ADMIN -> context.localize("bookings.delete_reason.ADMIN");
                    case CONFLICT -> context.localize("bookings.delete_reason.CONFLICT");
                };}
    <p>
        ${context.localize("mail.booking_deleted.message", booking.getUser().getUsername(), booking.getRoom().getId().toString(), booking.getStartDate().toString())} <br>
        ${deleteReason}<br>
        ${context.localize("mail.booking_deleted.ending")}
    </p>
    <div style="text-align:center">
        <button class="btn">
            <a href= "${context.getHostname()}${booking.getRoom().getId()}">
                <p>${context.localize("mail.make_new_booking")}</p>
            </a>
        </button>
    </div>
`, context = context, css = css)
