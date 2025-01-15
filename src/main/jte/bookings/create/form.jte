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

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, layout = layout, content = @`
    @template.util.form(csrf = csrf, cssClass = "card-body w-96 mx-auto", action = "/" + layout.getRoom().getId() + "/bookings/new", method = POST, content = @`
        <div class="form-control">
            <label class="label" for="start">
                <span class="label-text-alt">${context.localize("bookings.start")}</span>
            </label>
            <input id="start" name="start" type="datetime-local" class="input validator"
                   value="${start.toString()}"
                   min="${minimumTime.toString()}"
                   max="${maximumTime.toString()}"
                   step="${60 * 15}"
                   required
            />
        </div>
        <div class="form-control">
            <label class="label" for="end">
                <span class="label-text-alt">${context.localize("bookings.end")}</span>
            </label>
            <input id="end" name="end" type="datetime-local" class="input validator"
                   value="${end.toString()}"
                   min="${minimumTime.toString()}"
                   max="${maximumTime.toString()}"
                   step="${60 * 15}"
                   required
            />
        </div>
        <br>
        <div class="form-control">
            <!--                <label class="label" for="description">-->
            <!--                    <span class="label-text">${context.localize("bookings.optional")}</span>-->
            <!--                </label>-->
            <textarea id="description" name="description" class="textarea textarea-bordered"
                      placeholder="${context.localize("bookings.description")}"
            ></textarea>
            <label class="label">
                <span class="label-text-alt">${context.localize("bookings.optional")}</span>
            </label>
        </div>
        <label class="form-control tooltip text-left flex" data-tip="${context.localize("bookings.priority.tooltip")}">
            <div class="label">
                <span class="label-text">${context.localize("bookings.priority")}</span>
            </div>
            <select name="priority" class="select select-bordered" required>
                @for(Priority priority : Priority.values())
                    <option
                            value="${priority}"
                            selected="${priority == Priority.LOWEST}"
                    >${context.localize("bookings.priority." + priority)}</option>
                @endfor
            </select>
        </label>
        <div class="tooltip text-left" data-tip="${context.localize("bookings.share.tooltip")}">
            <span class="label-text">${context.localize("bookings.share")}</span>
            @for(ShareRoomType shareRoomType : ShareRoomType.values())
                <div class="form-control">
                    <label class="label cursor-pointer">
                        <span class="label-text">${context.localize("bookings.share." + shareRoomType)}</span>
                        <input type="radio" name="cooperative" class="radio"
                               value="${shareRoomType}"
                               checked="${shareRoomType == cooperative}"
                        />
                    </label>
                </div>
            @endfor
        </div>
        <div class="divider"></div>
        <p class="text-gray-500 text-sm">${context.localize("bookings.who_can_see_answer")}</p>

        <div class="form-control mt-6">
            <input type="submit" class="btn btn-primary" value="OK">
        </div>
    `)
`)
