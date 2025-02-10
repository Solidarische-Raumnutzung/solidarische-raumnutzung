@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.domain.ShareRoomType
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.time.format.DateTimeFormatter

@param JteContext context
@param CsrfToken csrf

@param Booking booking
@param boolean allowEditing
@param boolean showRequestButton

<h2 class="card-title">${context.localize("bookings.priority")}: ${context.localize("bookings.priority." + booking.getPriority())}</h2>
<a href="/${booking.getRoom().getId()}/bookings/${booking.getId()}/booking.ics" class="absolute top-2 right-2 btn btn-ghost" aria-label="${context.localize("bookings.export")}" title="${context.localize("bookings.export")}">
    <i class="fa-solid fa-download"></i>
</a>

<div class="absolute top-5 left-5">
    !{var bookingShareType = booking.getShareRoomType();}
    @if(bookingShareType == ShareRoomType.YES)
        <label class="label" aria-label="${context.localize("bookings.collaboration.share_room")}"
               title="${context.localize("bookings.collaboration.share_room.tooltip")}">
            <i class="fa-solid fa-users"></i>
        </label>
    @elseif(bookingShareType == ShareRoomType.NO)
        <label class="label" aria-label="${context.localize("bookings.collaboration.no_share_room")}"
               title="${context.localize("bookings.collaboration.no_share_room.tooltip")}">
            <i class="fa-solid fa-users-slash"></i>
        </label>
    @else
        <label class="label" aria-label="${context.localize("bookings.collaboration.on_request_share_room")}"
               title="${context.localize("bookings.collaboration.on_request_share_room.tooltip")}">
            <i class="fa-solid fa-question"></i>
            <i class="fa-solid fa-users"></i>
        </label>
    @endif
</div>

<p>${context.localize("bookings.start.value", context.format(booking.getStartDate()))}</p>
<p>${context.localize("bookings.end.value", context.format(booking.getEndDate()))}</p>

@if (!booking.getDescription().isBlank())
    <label class="label">${context.localize("bookings.description")}</label>
    <p class="relative textarea textarea-bordered soli-view-content break-words">
        @if (allowEditing)
            <label for="soli-edit-toggle" class="absolute top-0 right-0 btn btn-ghost" aria-label="${context.localize("bookings.description.edit")}" title="${context.localize("bookings.description.edit")}">
                <i class="fa-solid fa-pen"></i>
            </label>
            <input type="checkbox" id="soli-edit-toggle" class="hidden" />
        @endif
        ${booking.getDescription()}
    </p>
@elseif (allowEditing)
    <br>
    <label class="btn btn-ghost text-gray-500 text-sm soli-view-content" for="soli-edit-toggle" aria-label="${context.localize("bookings.description.new")}" title="${context.localize("bookings.description.new")}">
        ${context.localize("bookings.description.new")}
        <i class="fa-solid fa-pen"></i>
    </label>
    <input type="checkbox" id="soli-edit-toggle" class="hidden" />
@endif
@if (allowEditing)
    @template.util.form(csrf = csrf, cssClass = "soli-edit-content relative w-full", action = "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(), method = PATCH, content = @`
        <label for="edit-description-submit" class="absolute top-1 right-1 btn btn-ghost z-50" aria-label="${context.localize("bookings.description.edit")}" title="${context.localize("bookings.description.edit")}">
            <i class="fa-solid fa-check"></i>
        </label>
        <textarea id="edit-description-content" name="description" class="w-full input validator textarea textarea-bordered"
                  maxlength="1024">${booking.getDescription()}</textarea>
        <input type="submit" id="edit-description-submit" class="hidden" />
    `)
@endif

@if (allowEditing || showRequestButton)
    <div class="card-actions justify-end">
        @if (allowEditing)
            @template.util.form(csrf = csrf, action = "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(), method = DELETE, content = @`
                <button type="submit" class="btn btn-ghost" aria-label="${context.localize("bookings.tooltip.delete")}" title="${context.localize("bookings.tooltip.delete")}">
                    <i class="fas fa-trash-alt"></i>
                    ${context.localize("bookings.delete")}
                </button>
            `)
        @endif
        @if (showRequestButton)
            <a class="btn btn-primary" href="/${booking.getRoom().getId()}/bookings/new?start=${booking.getStartDate().format(DateTimeFormatter.ISO_DATE_TIME)}&end=${booking.getEndDate().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)}&cooperative=true" aria-label="${context.localize("bookings.tooltip.request")}" title="${context.localize("bookings.tooltip.request")}">${context.localize("bookings.request")}</a>
        @endif
    </div>
@endif
