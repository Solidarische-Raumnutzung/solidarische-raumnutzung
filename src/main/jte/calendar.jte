@import edu.kit.hci.soli.dto.LoginStateModel
@import edu.kit.hci.soli.domain.Room
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec

@param JteContext context
@param LoginStateModel login

@param Room room

@template.layout(page = new PageSpec("Index", "Soli"), context = context, login = login, content = @`
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
                timeZone: 'UTC',
                initialView: 'timeGridWeek',
                nowIndicator: true,
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'timeGridWeek,timeGridDay'
                },
                themeSystem: 'bootstrap5',
                selectable: true,
                select: function(info) {
                    window.location.href = "/${room.getId()}/bookings/new"
                        + "?start=" + encodeURIComponent(info.start.toISOString())
                        + "&end=" + encodeURIComponent(info.end.toISOString());
                },
                selectOverlap: function(event) {
                    return !event.classNames.includes('calendar-event-no');
                },
                selectAllow: function(selectInfo) {
                    // Keep these in sync with BookingsController!
                    return selectInfo.start > new Date(new Date().getTime() + 15 * 60 * 1000)
                        && selectInfo.end < new Date(selectInfo.start.getTime() + 4 * 60 * 60 * 1000)
                        && selectInfo.end < new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000);
                },
                slotDuration: '00:15:00',
                events: '/api/events'
            });

            calendar.render();

            setInterval(() => {
                calendar.refetchEvents();
            }, 1000 * 60 * 5);
        });
    </script>

    <style>
        :root {
            --fc-border-color: oklch(var(--bc)/0.5);
            --fc-now-indicator-color: oklch(var(--er));
        }

        .fc-timegrid-now-indicator-line {
            border-width: 2px !important;
        }

        .calendar-event-highest {
            background-color: oklch(var(--er));
            color: oklch(var(--erc));
        }

        .calendar-event-highest .fc-event-title::before {
            content: "p-${context.localize("bookings.priority.HIGHEST")}";
            justify-content: center;
            height: 1.25rem;
            font-size: .875rem;
            line-height: 1.25rem;
            padding-left: .563rem;
            padding-right: .563rem;
            border-radius: var(--rounded-badge);
            border-width: 1px;
            /*background-color: oklch(var(--p)/var(--tw-bg-opacity));*/
            color: oklch(var(--pc)/var(--tw-text-opacity));
        }

        .calendar-event-medium {
            background-color: oklch(var(--wa));
            color: oklch(var(--wac));
        }

        .calendar-event-medium .fc-event-title::before {
            content: "p-${context.localize("bookings.priority.MEDIUM")}";
            justify-content: center;
            height: 1.25rem;
            font-size: .875rem;
            line-height: 1.25rem;
            padding-left: .563rem;
            padding-right: .563rem;
            border-radius: var(--rounded-badge);
            border-width: 1px;
            /*background-color: oklch(var(--p)/var(--tw-bg-opacity));*/
            color: oklch(var(--pc)/var(--tw-text-opacity));
        }

        .calendar-event-lowest {
            background-color: oklch(var(--in));
            color: oklch(var(--inc));
        }

        .calendar-event-lowest .fc-event-title::before {
            content: "p-${context.localize("bookings.priority.LOWEST")}";
            justify-content: center;
            height: 1.25rem;
            font-size: .875rem;
            line-height: 1.25rem;
            padding-left: .563rem;
            padding-right: .563rem;
            border-radius: var(--rounded-badge);
            border-width: 1px;
            /*background-color: oklch(var(--p)/var(--tw-bg-opacity));*/
            color: oklch(var(--pc)/var(--tw-text-opacity));
        }

        .fc-event-main {
            color: inherit !important;
        }

        .calendar-event-own .fc-event-title::after {
            content: "${context.localize("bookings.own")}";
            justify-content: center;
            height: 1.25rem;
            font-size: .875rem;
            line-height: 1.25rem;
            padding-left: .563rem;
            padding-right: .563rem;
            border-radius: var(--rounded-badge);
            border-width: 1px;
            /*background-color: oklch(var(--p)/var(--tw-bg-opacity));*/
            color: oklch(var(--pc)/var(--tw-text-opacity));
        }

        .calendar-event-yes {
            /* TODO: add some form of icon? */
        }

        .calendar-event-no {
            /* TODO: add some form of icon? */
        }

        .calendar-event-on_request {
            /* TODO: add some form of icon? */
        }
    </style>

    <div id="calendar" class="w-full"></div>

    <noscript class="w-full">
        <h1 class="text-center">
            ${context.localize("calendar.no_js")}
        </h1>
    </noscript>
`)
