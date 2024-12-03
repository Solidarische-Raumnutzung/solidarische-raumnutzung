@import edu.kit.hci.soli.domain.Priority
@import edu.kit.hci.soli.domain.ShareRoomType
@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.time.LocalDateTime

@param JteContext context
@param LoginStateModel login
@param Long room
@param LocalDateTime start
@param LocalDateTime end
@param LocalDateTime minimumTime
@param LocalDateTime maximumTime
@param CsrfToken csrf

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, login = login, content = @`
    <form class="card-body w-96 mx-auto" method="post" action="/${room}/bookings/new">
        @if(csrf != null)
            <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
        @endif
        <div class="form-control">
            <label class="label" for="start">
                <span class="label-text-alt">${context.localize("bookings.start")}</span>
            </label>
            <input id="start" name="start" type="datetime-local" class="input input-bordered"
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
            <input id="end" name="end" type="datetime-local" class="input input-bordered"
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
                               checked="${shareRoomType == ShareRoomType.NO}"
                        />
                    </label>
                </div>
            @endfor
        </div>
        <div class="form-control mt-6">
            <input type="submit" class="btn btn-primary" value="OK">
        </div>
    </form>
`)
