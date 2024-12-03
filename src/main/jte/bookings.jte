@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.StagedBooking
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf
@param List<Booking> bookings
@param List<StagedBooking> stagedBookings

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, login = login, content = @`
    <div class="overflow-x-auto">
        @if(bookings.isEmpty() && stagedBookings.isEmpty())
            <div class="card">
                <div class="card-body">
                    <p class="text-center">Du hast noch keine Reservierungen.</p>
                </div>
            </div>
        @else
            <table class="table table-zebra">
                <thead>
                <tr>
                    <th></th>
                    <th>${context.localize("bookings.description")}</th>
                    <th>${context.localize("bookings.start")}</th>
                    <th>${context.localize("bookings.end")}</th>
                    <th>${context.localize("bookings.share")}</th>
                    <th>${context.localize("bookings.priority")}</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                @for(Booking booking : bookings)
                    <tr>
                        <td>${booking.getId()}</td>
                        <td>${booking.getDescription()}</td>
                        <td>${booking.getStartDate().toString()}</td>
                        <td>${booking.getEndDate().toString()}</td>
                        <td>${context.localize("bookings.share." + booking.getShareRoomType())}</td>
                        <td>${context.localize("bookings.priority." + booking.getPriority())}</td>
                        <td></td>
                    </tr>
                @endfor
                @if(!bookings.isEmpty() && !stagedBookings.isEmpty())
                    <tr>
                        <td colspan="7"></td>
                    </tr>
                @endif
                @for(StagedBooking booking : stagedBookings)
                    <tr>
                        <td>${booking.getId()}</td>
                        <td>${booking.getDescription()}</td>
                        <td>${booking.getStartDate().toString()}</td>
                        <td>${booking.getEndDate().toString()}</td>
                        <td>${context.localize("bookings.share." + booking.getShareRoomType())}</td>
                        <td>${context.localize("bookings.priority." + booking.getPriority())}</td>
                        <td>
                            <form action="/bookings/delete/${booking.getId()}" method="post">
                                @if(csrf != null)
                                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                                @endif
                                <input type="hidden" name="_method" value="delete" />
                                <button type="submit" class="btn btn-ghost fas fa-trash-alt">${context.localize("bookings.delete")}</button>
                            </form>
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`)
