@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec
@import java.util.List

@param JteContext context
@param LoginStateModel login
@param List<Booking> bookings

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, login = login, content = @`
    <div class="">

        <div class="overflow-x-auto">
            @if(bookings.isEmpty())
                <div class="card">
                    <div class="card-body">
                        <p class="text-center">Du hast noch keine Reservierungen.</p>
                    </div>
                </div>


            @else

                    <table class="table table-zebra">
                        <!-- head -->
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
                        <!-- body -->
                        @for(Booking booking : bookings)
                            <tr>

                                <td>${booking.getId()}</td>
                                <td>${booking.getDescription()}</td>
                                <td>${booking.getStartDate().toString()}</td>
                                <td>${booking.getEndDate().toString()}</td>
                                <td>${booking.getShareRoomType()}</td>
                                <td>${booking.getPriority()}</td>
                                <td>

                                    <a href="/bookings/delete/${booking.getId()}" class="btn btn-ghost">
                                        <?xml version="1.0" encoding="UTF-8"?><svg width="24px" height="24px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" color="#000000" stroke-width="1.5"><path d="M20 9L18.005 20.3463C17.8369 21.3026 17.0062 22 16.0353 22H7.96474C6.99379 22 6.1631 21.3026 5.99496 20.3463L4 9" fill="#000000"></path><path d="M20 9L18.005 20.3463C17.8369 21.3026 17.0062 22 16.0353 22H7.96474C6.99379 22 6.1631 21.3026 5.99496 20.3463L4 9H20Z" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path><path d="M21 6H15.375M3 6H8.625M8.625 6V4C8.625 2.89543 9.52043 2 10.625 2H13.375C14.4796 2 15.375 2.89543 15.375 4V6M8.625 6H15.375" stroke="#000000" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path></svg></a>
                                </td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
            @endif

            </div>
        </div>

`)
