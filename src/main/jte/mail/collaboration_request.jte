@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.Booking

@param JteContext context
@param String css

@param Booking booking

@template.mail.layout(title= context.lookup("mail.collaboration_request.heading"), content = @`
    <p>
        ${context.localize("mail.collaboration_request.message", booking.getUser().getUsername(), booking.getRoom().getId(), booking.getStartDate().toString())} <br><br>
        ${context.localize("mail.ending")}<br><br>
    </p>
    <div>
        <button class="btn btn-accent">
            <a class="pretty-link" href= "${context.getHostname()}${booking.getRoom().getId()}/bookings/${booking.getId()}/collaboration">
                <p>${context.localize("mail.collaboration_request.more_details")}</p>
            </a>
        </button>
    </div>
`, context = context, css = css)
