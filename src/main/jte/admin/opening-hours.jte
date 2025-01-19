@import edu.kit.hci.soli.domain.User
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.data.domain.Page
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.time.DayOfWeek
@import java.util.UUID

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
                    <td><input type="time" class="input input-bordered w-full" name="${day.name()}_start"></td>
                    <td><input type="time" class="input input-bordered w-full" name="${day.name()}_end"></td>
                    <input type="hidden" name="dayOfWeek" value="${day.name()}">
                </tr>
            @endfor
            </tbody>
        </table>
        <form action="/admin/opening-hours/${layout.getRoom().getId()}/save" method="get">
            <div class="flex justify-end mt-4">
                <button class="btn btn-primary" type="submit">Save</button>
            </div>
        </form>
    </div>
`)