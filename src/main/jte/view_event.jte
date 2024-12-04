@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.domain.ShareRoomType
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.Objects

@param JteContext context
@param LoginStateModel login
@param Booking booking
@param CsrfToken csrf

@template.layout(page = new PageSpec("Terminansicht", "Soli"), context = context, login = login, content = @`
<div class="card-body w-96 mx-auto">
    <div class="form-control">
        <label class="label">
            <span class="label-text">${context.localize("bookings.priority")}</span>
        </label>
        <p class="input input-bordered">${context.localize("bookings.priority." + booking.getPriority())}</p>
    </div>
    <div class="form-control">
        <label class="label">
            <span class="label-text">${context.localize("bookings.description")}</span>
        </label>
        <p class="textarea textarea-bordered">${booking.getDescription()}</p>
    </div>
    <%--  TODO This compares users using objects.equals() is this still correct for the new approach?   --%>
    @if(login.kind().equals(LoginStateModel.Kind.ADMIN) || (login.user() != null
        && Objects.equals(login.user(), booking.getUser())))
        <form action="/bookings/delete/${booking.getId()}" method="post">
            @if(csrf != null)
                <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
            @endif
            <input type="hidden" name="_method" value="delete" />
            <button type="submit" class="btn btn-ghost fas fa-trash-alt">${context.localize("bookings.delete")}</button>
        </form>
    @endif
    @if(Objects.equals(ShareRoomType.ON_REQUEST, booking.getShareRoomType()) && (login.user() != null
        && !Objects.equals(login.user(), booking.getUser())))
        <%-- TODO implement this endpoint --%>
        <form action="/bookings/request/${booking.getId()}" method="post">
            @if(csrf != null)
                <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
            @endif
            <button type="submit" class="btn btn-primary">${context.localize("bookings.request")}</button>
        </form>
    @endif
</div>
`)