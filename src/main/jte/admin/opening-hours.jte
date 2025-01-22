@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.time.DayOfWeek

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@template.layout(page = new PageSpec(context.lookup("administration.opening_hours"), "Soli"), context = context, layout = layout, content = @`
    <div class="overflow-x-auto">
        @template.util.form(csrf = csrf, action = "/admin/opening-hours/" + layout.getRoom().getId().toString() + "/save", method = PUT, content = @`
            <table class="table table-zebra w-full">
                <thead>
                <tr>
                    <th>Day</th>
                    <th>Start Time</th>
                    <th>Closing Time</th>
                </tr>
                </thead>
                <tbody>
                @for (DayOfWeek day : DayOfWeek.values())
                    @if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY)
                    <tr>
                        <td>${context.format(day)}</td>
                        <td><input type="time" class="input input-bordered w-full" name="${day.toString().toLowerCase()}Start"></td>
                        <td><input type="time" class="input input-bordered w-full" name="${day.toString().toLowerCase()}End"></td>
                        <input type="hidden" name="dayOfWeek" value="${day}">
                    </tr>
                    @endif
                @endfor
                </tbody>
            </table>
            <button type="submit" class="btn btn-primary">${context.localize("administration.opening_hours.save")}</button>
        `)
    </div>
`)