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
                                <td>${context.localize("bookings.share." + booking.getShareRoomType())}</td>
                                <td>${context.localize("bookings.priority." + booking.getPriority())}</td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
            @endif

            </div>
        </div>

`)
