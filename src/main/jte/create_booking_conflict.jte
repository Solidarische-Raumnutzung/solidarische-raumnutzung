@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.BookingAttemptResult
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken

@param JteContext context
@param LoginStateModel login
@param CsrfToken csrf

@param Booking attemptedBooking
@param BookingAttemptResult.PossibleCooperation bookingResult

@template.layout(page = new PageSpec("Conflict", "Soli"), context = context, login = login, room = attemptedBooking.getRoom(), content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-5xl font-bold">Konfliktlösung</h1>
            <p class="py-6">
                Ein lösbarer Konflikt wurde festgestellt.<br>
                Vorgeschlagene Lösung:
            </p>
            <ul>
                <!-- TODO: Diese list-items werden nicht korrekt dargestellt -->
                @for(Booking booking : bookingResult.contact())
                    <li>Kontaktiere: ${booking.getDescription()} (${booking.getStartDate().toString()} - ${booking.getEndDate().toString()})</li>
                @endfor
                @for(Booking booking : bookingResult.override())
                    <li>Lösche: ${booking.getDescription()} (${booking.getStartDate().toString()} - ${booking.getEndDate().toString()})</li>
                @endfor
                @for(Booking booking : bookingResult.cooperate())
                    <li>Nutze gleichzeitig mit: ${booking.getDescription()} (${booking.getStartDate().toString()} - ${booking.getEndDate().toString()})</li>
                @endfor
            </ul>
            <form method="post" action="/${attemptedBooking.getRoom().getId()}/bookings/new/conflict">
                @if(csrf != null)
                    <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
                @endif

                <button class="btn btn-primary" type="submit">Lösen</button>
            </form>
        </div>
    </div>
`)