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
                <tr>
                    <td>${day}</td>
                    <td><input type="time" class="input input-bordered w-full" name="${day}_start"></td>
                    <td><input type="time" class="input input-bordered w-full" name="${day}_end"></td>
                    <input type="hidden" name="dayOfWeek" value="${day}">
                </tr>
            @endfor
            </tbody>
        </table>
        @template.util.form(csrf = csrf, action = "/admin/opening-hours/${layout.getRoom().getId()}/save", method = PUT, content = @`
            <button type="submit" class="btn btn-primary">${context.localize("administration.opening_hours.save")}</button>
        `)
    </div>
`)