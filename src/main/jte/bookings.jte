@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.time.format.DateTimeFormatter
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf
@param Room room
@param List<Booking> bookings

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, login = login, room = room, content = @`
    <div class="overflow-x-auto">
        @if(bookings.isEmpty())
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
                        <td>${booking.getId()} ${booking.getOutstandingRequests().isEmpty() ? context.empty() : context.localize("bookings.outstanding", booking.getOutstandingRequests().size())}</td>
                        <td>${booking.getDescription()}</td>
                        <td>${context.format(booking.getStartDate())}</td>
                        <td>${context.format(booking.getEndDate())}</td>
                        <td>${context.localize("bookings.share." + booking.getShareRoomType())}</td>
                        <td>${context.localize("bookings.priority." + booking.getPriority())}</td>
                        <td>
                            <form action="/${booking.getRoom().getId()}/bookings/${booking.getId()}/delete" method="post">
                                @if(csrf != null)
                                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                                @endif
                                <input type="hidden" name="_method" value="delete"/>
                                <button type="submit" class="btn btn-ghost">
                                    <i class="fas fa-trash-alt"></i>
                                    ${context.localize("bookings.delete")}
                                </button>
                            </form>
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`)
