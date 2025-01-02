@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.Booking

@param JteContext context

@param Booking booking

<p>
    Dear ${booking.getUser().getUsername()},
</p>
<p>${context.localize("bookings.collaboration.message")}</p>
<a href="${context.getHostname()}/${booking.getRoom().getId()}/bookings/${booking.getId()}/collaboration">
    <p>${context.localize("bookings.collaboration.more_details")}</p>
</a>
