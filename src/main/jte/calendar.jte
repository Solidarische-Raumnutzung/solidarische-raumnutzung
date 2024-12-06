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

        .calendar-event-medium {
            background-color: oklch(var(--wa));
            color: oklch(var(--wac));
        }

        .calendar-event-lowest {
            background-color: oklch(var(--in));
            color: oklch(var(--inc));
        }

        .fc-event-main {
            color: inherit !important;
        }

        .calendar-event-own {
            border-color: oklch(var(--a));
            border-width: 3px;
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
            The calendar is only supported with JavaScript.<br>
            Use the sidebar to book instead.
        </h1>
    </noscript>

`)
