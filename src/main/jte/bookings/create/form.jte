@import edu.kit.hci.soli.domain.Priority
@import edu.kit.hci.soli.domain.ShareRoomType
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import static org.springframework.http.HttpMethod.*
@import java.time.LocalDateTime

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param LocalDateTime start
@param LocalDateTime end
@param ShareRoomType cooperative

@param LocalDateTime minimumTime
@param LocalDateTime maximumTime

@template.layout(page = new PageSpec(context.lookup("bookings.new"), "Soli"), context = context, layout = layout, content = @`
    <fieldset class="fieldset w-80 mx-auto bg-base-200 border border-base-300 p-4 rounded-box">
        <legend class="fieldset-legend">${context.localize("bookings.new")}</legend>

        @template.util.form(csrf = csrf, action = "/" + layout.getRoom().getId() + "/bookings/new", method = POST, content = @`
            <label class="fieldset-legend" for="start">${context.localize("bookings.start")} </label>
            <input id="start" name="start" type="datetime-local" class="input validator"
                   value="${start.toString()}"
                   min="${minimumTime.toString()}"
                   max="${maximumTime.toString()}"
                   step="${60 * 15}"
                   required
            />

            <label class="fieldset-legend" for="end">${context.localize("bookings.end")} </label>
            <input id="end" name="end" type="time" class="input validator"
                   value="${end.toLocalTime().toString()}"
                   min="${minimumTime.toString()}"
                   max="${maximumTime.toString()}"
                   step="${60 * 15}"
                   required
            />

            <br><br>

            <textarea id="description" name="description" class="input textarea textarea-bordered"
                      placeholder="${context.localize("bookings.description")}"
            ></textarea>
            <label class="label">
                <span class="label-text-alt">${context.localize("bookings.optional")}</span>
            </label>

            <br><br>

            <div class="tooltip block" data-tip="${context.localize("bookings.priority.tooltip")}">
                <label class="fieldset-label" for="priority">${context.localize("bookings.priority")}</label>
                <select name="priority" class="select select-bordered" required>
                    @for(Priority priority : Priority.values())
                        <option
                                value="${priority}"
                                selected="${priority == Priority.LOWEST}"
                        >${context.localize("bookings.priority." + priority)}</option>
                    @endfor
                </select>
            </div>

            <br>

            <div class="tooltip block" data-tip="${context.localize("bookings.share.tooltip")}">
                <label class="fieldset-label" for="cooperative">${context.localize("bookings.share")}</label>
                @for(ShareRoomType shareRoomType : ShareRoomType.values())
                    <label class="fieldset-label">
                        <input type="radio" name="cooperative" class="radio"
                               value="${shareRoomType}"
                               checked="${shareRoomType == cooperative}"
                        />
                        ${context.localize("bookings.share." + shareRoomType)}
                    </label>
                @endfor
            </div>

            <div class="divider"></div>
            <p class="text-gray-500 text-sm">${context.localize("bookings.who_can_see_answer")}</p>
            <br>

            <input type="submit" class="input btn btn-primary" value="OK">
        `)
    </fieldset>
`)
