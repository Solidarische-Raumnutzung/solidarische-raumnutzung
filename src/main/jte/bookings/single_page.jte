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
    <div class="card bg-neutral text-neutral-content w-96 mx-auto">
        <div class="card-body items-center text-center">
            <%-- TODO we should demarcate staged bookings (IE bookings that have open outstanding requests) --%>
            @template.bookings.single(context = context, booking = booking, csrf = csrf, showRequestButton = true, showDeleteButton = showDeleteButton)
        </div>
    </div>
`)