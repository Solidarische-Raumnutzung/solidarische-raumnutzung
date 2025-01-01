@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.data.domain.Page
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf
@param Room room
@param Page<Booking> bookings

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
                @for(Booking booking : bookings.getContent())
                    <tr>
                        <td>${booking.getId()} ${booking.getOpenRequests().isEmpty() ? context.empty() : context.localize("bookings.outstanding", booking.getOpenRequests().size())}</td>
                        <td>${booking.getDescription()}</td>
                        <td>${booking.getStartDate().toString()}</td>
                        <td>${booking.getEndDate().toString()}</td>
                        <td>${context.localize("bookings.share." + booking.getShareRoomType())}</td>
                        <td>${context.localize("bookings.priority." + booking.getPriority())}</td>
                        <td>
                            @template.util.form(csrf = csrf, action = "/" + booking.getRoom().getId() + "/bookings/" + booking.getId(), method = DELETE, content = @`
                                <button type="submit" class="btn btn-ghost">
                                    <i class="fas fa-trash-alt"></i>
                                    ${context.localize("bookings.delete")}
                                </button>
                            `)
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
            <div>
                <div class="join grid grid-cols-3 mt-4 ">
                    @if(bookings.hasPrevious())
                        <a class="join-item btn btn-outline"
                                    href="/${room.getId()}/bookings?page=${bookings.getNumber() - 1}&size=${bookings.getSize()}">${context.localize("pagination.previous")}</a>
                        @else <span class="join-item btn btn-outline btn-disabled">${context.localize("pagination.previous")}</span>
                    @endif
                    <span class="join-item btn btn-outline">${context.localize("pagination.page")} ${bookings.getNumber() + 1} ${context.localize("pagination.of")} ${bookings.getTotalPages()}</span>
                    @if(bookings.hasNext())
                        <a class="join-item btn btn-outline"
                           href="/${room.getId()}/bookings?page=${bookings.getNumber() + 1}&size=${bookings.getSize()}">${context.localize("pagination.next")}</a>
                        @else <span class="join-item btn btn-outline btn-disabled">${context.localize("pagination.next")}</span>
                    @endif
                </div>

            </div>
        @endif
    </div>
`)
