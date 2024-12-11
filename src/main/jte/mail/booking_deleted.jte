@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.BookingDeleteReason

@param JteContext context

@param Booking booking
@param BookingDeleteReason reason

<p>
    Dear ${booking.getUser().getUsername()},
</p>
<p>
    unfortunately, your booking for the room ${booking.getRoom().getId()} starting at ${booking.getStartDate().toString()} has been deleted.
</p>
<p>
    Reason: ${context.localize("bookings.delete_reason." + reason)}
</p>