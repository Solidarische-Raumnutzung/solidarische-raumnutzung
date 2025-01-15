@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.time.format.DateTimeFormatter

@param JteContext context
@param CsrfToken csrf

@param Booking booking
@param boolean showRequestButton
@param boolean showDeleteButton

<h2 class="card-title">${context.localize("bookings.priority")}: ${context.localize("bookings.priority." + booking.getPriority())}</h2>
<p>${context.localize("bookings.start.value", context.format(booking.getStartDate()))}</p>
<p>${context.localize("bookings.end.value", context.format(booking.getEndDate()))}</p>
<div class="form-control">
    <label class="label">
        <span class="label-text">${context.localize("bookings.description")}</span>
    </label>
    <p class="textarea textarea-bordered">${booking.getDescription()}</p>
</div>
@if (showDeleteButton || showRequestButton)
    <div class="card-actions justify-end">
        <%--  TODO This compares users using objects.equals() is this still correct for the new approach?   --%>
        @if (showDeleteButton)
            @template.util.form(csrf = csrf, action = "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(), method = DELETE, content = @`
                <button type="submit" class="btn btn-ghost">
                    <i class="fas fa-trash-alt"></i>
                    ${context.localize("bookings.delete")}
                </button>
            `)
        @endif
        @if (showRequestButton)
            <a class="btn btn-primary" href="/${booking.getRoom().getId()}/bookings/new?start=${booking.getStartDate().format(DateTimeFormatter.ISO_DATE_TIME)}&end=${booking.getEndDate().format(DateTimeFormatter.ISO_DATE_TIME)}&cooperative=true">${context.localize("bookings.request")}</a>
        @endif
    </div>
@endif
