@import edu.kit.hci.soli.domain.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.time.LocalDateTime

@param JteContext context
@param LoginStateModel login
@param Long room
@param LocalDateTime start
@param LocalDateTime end
@param CsrfToken csrf

@template.layout(page = new PageSpec("Meine Reservierungen", "Soli"), context = context, login = login, content = @`
    <form class="card-body" method="post" action="/${room}/bookings/new">
        @if(csrf != null)
            <input type="hidden" name="${csrf.getParameterName()}" value="${csrf.getToken()}">
        @endif
        <div class="form-control">
            <label class="label" for="start">
                <span class="label-text-alt">Start</span>
            </label>
            <input id="start" name="start" type="datetime-local" class="input input-bordered" value="${start.toString()}" />
        </div>
        <div class="form-control">
            <label class="label" for="end">
                <span class="label-text-alt">Ende</span>
            </label>
            <input id="end" name="end" type="datetime-local" class="input input-bordered" value="${end.toString()}" />
        </div>
        <br>
        <div class="form-control">
            <!--                <label class="label" for="description">-->
            <!--                    <span class="label-text">Optional</span>-->
            <!--                </label>-->
            <textarea id="description" name="description" class="textarea textarea-bordered" placeholder="Beschreibung"></textarea>
            <label class="label">
                <span class="label-text-alt">Optional</span>
            </label>
        </div>
        <div class="form-control tooltip text-left" data-tip="Erlaube es anderen, dich per E-Mail zu kontaktieren">
            <label class="label cursor-pointer" for="emailVisible">
                <span class="label-text">E-Mail für andere sichtbar</span>
                <input id="emailVisible" name="emailVisible" type="checkbox" checked class="toggle" />
            </label>
        </div>
        <div class="form-control tooltip text-left flex" data-tip="Wie kritisch die Buchung ist. z.B. rot für kritische medizinische Zwecke">
            <label class="label cursor-pointer rating gap-1">
                <span class="label-text">Priorität</span>
                <input type="radio" name="priority" class="mask mask-circle bg-green-400" checked="checked" value="LOWEST" />
                <input type="radio" name="priority" class="mask mask-circle bg-yellow-400" value="MEDIUM" />
                <input type="radio" name="priority" class="mask mask-circle bg-red-400" value="HIGHEST" />
            </label>
        </div>
        <div class="form-control tooltip text-left" data-tip="Erlaube es anderen, den Raum gleichzeitig zu nutzen">
            <label class="label cursor-pointer" for="cooperative">
                <span class="label-text">Parallele Reservierungen</span>
                <input name="cooperative" id="cooperative" type="checkbox" checked class="toggle" />
            </label>
        </div>
        <div class="form-control mt-6">
            <input type="submit" class="btn btn-primary" value="OK">
        </div>
    </form>
`)
