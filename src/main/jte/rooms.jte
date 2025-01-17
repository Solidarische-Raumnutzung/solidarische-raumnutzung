@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param List<Room> rooms

@template.layout(page = new PageSpec(context.lookup("rooms"), "Soli"), layout = layout, context = context, content = @`
    <ul class="list bg-base-100 rounded-box shadow-md mx-auto">
        @for (Room room : rooms)
            <li class="list-row max-w-4xl">
                <div>${room.getName()}</div>
                <p class="list-col-wrap text-xs">
                    ${room.getDescription()}
                </p>
                <a href="/${room.getId()}" class="btn btn-primary">
                    <i class="fas fa-arrow-right"></i>
                </a>
            </li>
        @endfor
    </ul>
`)
