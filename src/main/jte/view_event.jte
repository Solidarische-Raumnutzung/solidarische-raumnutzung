@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.time.format.DateTimeFormatter
@import java.util.Objects

@param JteContext context
@param LoginStateModel login
@param Booking booking
@param CsrfToken csrf
@param Room room
@param boolean showRequestButton

@template.layout(page = new PageSpec("Terminansicht", "Soli"), context = context, login = login, room = booking.getRoom(), content = @`
    <div class="card bg-neutral text-neutral-content w-96 mx-auto">
        <div class="card-body items-center text-center">
            <%-- TODO we should demarcate staged bookings (IE bookings that have open outstanding requests) --%>
            <h2 class="card-title">${context.localize("bookings.priority")}: ${context.localize("bookings.priority." + booking.getPriority())}</h2>
            <p>${context.localize("bookings.start")}: ${booking.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</p>
            <p>${context.localize("bookings.end")}: ${booking.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</p>
            <div class="form-control">
                <label class="label">
                    <span class="label-text">${context.localize("bookings.description")}</span>
                </label>
                <p class="textarea textarea-bordered">${booking.getDescription()}</p>
            </div>
            <div class="card-actions justify-end">
                <%--  TODO This compares users using objects.equals() is this still correct for the new approach?   --%>
                @if(login.kind().equals(LoginStateModel.Kind.ADMIN) || (login.user() != null
                    && Objects.equals(login.user(), booking.getUser())))
                    <form action="/${booking.getRoom().getId()}/bookings/${booking.getId()}" method="post">
                        @if(csrf != null)
                            <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                        @endif
                        <input type="hidden" name="_method" value="delete"/>
                        <button type="submit" class="btn btn-ghost">
                            <i class="fas fa-trash-alt"></i>
                            ${context.localize("bookings.delete")}
                        </button>
                    </form>
                @endif
                @if(showRequestButton)
                    <a class="btn btn-primary" href="/${booking.getRoom().getId()}/bookings/new?start=${booking.getStartDate().format(DateTimeFormatter.ISO_DATE_TIME)}&end=${booking.getEndDate().format(DateTimeFormatter.ISO_DATE_TIME)}&cooperative=true">${context.localize("bookings.request")}</a>
                @endif
            </div>
        </div>
    </div>
`)