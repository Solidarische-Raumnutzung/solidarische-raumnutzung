@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Booking booking
@param boolean showRequestButton
@param boolean showDeleteButton

@template.layout(page = new PageSpec("Terminansicht", "Soli"), context = context, layout = layout, content = @`
    <div class="card bg-base-200 border border-base-300 text-neutral-content w-96 mx-auto">
        <a href="/${booking.getRoom().getId()}/bookings/${booking.getId()}/booking.ics" class="absolute top-2 right-2 btn btn-primary">
            Export
            <i class="fa-solid fa-file-export"></i>
        </a>
        <div class="card-body items-center text-center">
            <%-- TODO we should demarcate staged bookings (IE bookings that have open outstanding requests) --%>
            @template.bookings.single(context = context, booking = booking, csrf = csrf, showRequestButton = true, showDeleteButton = showDeleteButton)
        </div>
    </div>
`)