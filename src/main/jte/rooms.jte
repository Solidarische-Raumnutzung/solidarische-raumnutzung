@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param List<Room> rooms
@param boolean edit = false

@template.layout(page = context.page("rooms"), layout = layout, context = context, content = @`
    <ul class="list bg-base-100 rounded-box shadow-md mx-auto min-w-80">
        @for (Room room : rooms)
            <li class="list-row max-w-4xl">
                <div class="list-col-grow">
                    <div aria-label="${context.localize("rooms.name")}" title="${context.localize("rooms.name")}">${room.getName()}</div>
                    <div class="text-xs uppercase font-semibold opacity-60" aria-label="${context.localize("rooms.location")}" title="${context.localize("rooms.location")}">
                        <i class="fa-solid fa-location-dot"></i> &shy;
                        ${room.getLocation()}</div>
                </div>
                <p class="list-col-wrap text-xs" aria-label="${context.localize("rooms.description")}" title="${context.localize("rooms.description")}">${room.getDescription()}</p>
                @if (edit)
                    <a href="/admin/rooms/${room.getId()}" class="btn btn-square btn-ghost" aria-label="${context.localize("bookings")}" title="${context.localize("rooms.edit")}">
                        <i class="fas fa-pen"></i>
                    </a>
                    !{var id = "modal_delete_" + room.getId();}
                    @template.util.modal.open(message = @`
                        <i class="fas fa-trash-alt" aria-label="${context.localize("rooms.delete")}" title="${context.localize("rooms.delete")}"></i>
                    `, style = "btn btn-error", id = id, alternate = "/admin/rooms/" + room.getId() + "/delete", content = @`
                        @template.admin.confirmation.delete_room(context = context, csrf = csrf, id = id, room = room)
                    `)
                @else
                    <a href="/${room.getId()}" class="btn btn-primary" aria-label="${context.localize("rooms.select")}" title="${context.localize("rooms.select")}">
                        <i class="fas fa-arrow-right"></i>
                    </a>
                @endif
            </li>
        @endfor

        @if (edit)
            <a href="/admin/rooms/new" aria-label="${context.localize("rooms.new")}" title="${context.localize("rooms.new")}">
                <li class="btn btn-primary w-full">
                    <i class="fas fa-plus"></i>
                </li>
            </a>
        @endif
    </ul>
`)
