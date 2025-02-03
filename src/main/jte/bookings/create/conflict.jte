@import edu.kit.hci.soli.domain.Booking
@import edu.kit.hci.soli.dto.BookingAttemptResult
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Booking attemptedBooking
@param BookingAttemptResult.PossibleCooperation bookingResult

@template.layout(page = new PageSpec(context.lookup("bookings.conflict.resolution"), "Soli"), context = context, layout = layout, content = @`
    <div class="hero-content text-center">
        <div class="max-w-md">
            <h1 class="text-2xl font-bold mb-2">${context.localize("bookings.conflict.resolution")}</h1>

            <ul class="list bg-base-100 rounded-box shadow-md">
                <li class="mb-4">${context.localize("bookings.conflict.recommend")}</li>

                @if (!bookingResult.contact().isEmpty())
                    <h2 class="text-left">${context.localize("bookings.conflict.contact")}</h2>
                    @for (Booking booking : bookingResult.contact())
                        <li class="list-row">
                            <div class="text-xs uppercase font-semibold opacity-60">
                                ${context.format(booking.getStartDate())} - ${context.format(booking.getEndDate().toLocalTime())}
                            </div>
                            <p class="list-col-wrap list-col-grow">
                                ${booking.getDescription().isBlank() ? context.lookup("bookings.description.none") : booking.getDescription()}
                            </p>
                        </li>
                    @endfor
                @endif

                @if (!bookingResult.override().isEmpty())
                    <h2 class="text-left">${context.localize("bookings.conflict.delete")}</h2>
                    @for (Booking booking : bookingResult.override())
                        <li class="list-row">
                            <div class="text-xs uppercase font-semibold opacity-60">
                                ${context.format(booking.getStartDate())} - ${context.format(booking.getEndDate().toLocalTime())}
                            </div>
                            <p class="list-col-wrap list-col-grow">
                                ${booking.getDescription().isBlank() ? context.lookup("bookings.description.none") : booking.getDescription()}
                            </p>
                        </li>
                    @endfor
                @endif

                @if (!bookingResult.cooperate().isEmpty())
                    <h2 class="text-left">${context.localize("bookings.conflict.cooperate")}</h2>
                    @for (Booking booking : bookingResult.cooperate())
                        <li class="list-row">
                            <div class="text-xs uppercase font-semibold opacity-60">
                                ${context.format(booking.getStartDate())} - ${context.format(booking.getEndDate().toLocalTime())}
                            </div>
                            <p class="list-col-wrap list-col-grow">
                                ${booking.getDescription().isBlank() ? context.lookup("bookings.description.none") : booking.getDescription()}
                            </p>
                        </li>
                    @endfor
                @endif
            </ul>
            @template.util.form(csrf = csrf, action = "/" + attemptedBooking.getRoom().getId() + "/bookings/new/conflict", method = POST, content = @`
                <button class="btn btn-primary" type="submit">${context.localize("bookings.conflict.solve")}</button>
            `)
        </div>
    </div>
`)
