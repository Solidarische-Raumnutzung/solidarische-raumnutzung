@import edu.kit.hci.soli.domain.LoginStateModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec

@param JteContext context
@param LoginStateModel login
@template.layout(page = new PageSpec("Index", "Soli"), context = context, login = login, content = @`
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const calendar = new FullCalendar.Calendar(document.getElementById('calendar'), {
                timeZone: 'UTC',
                initialView: 'timeGridWeek',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'timeGridWeek,timeGridDay'
                },
                themeSystem: 'bootstrap5',
                selectable: true,
                select: function(info) {
                    console.log(info);
                },
                selectOverlap: function(event) {
                    return event.display === 'background';
                },
                selectAllow: function(selectInfo) {
                    return selectInfo.start < new Date(new Date().getTime() + 15 * 60 * 1000)
                        && selectInfo.end < new Date(selectInfo.start.getTime() + 4 * 60 * 60 * 1000);
                },
                nowIndicator: true,
                slotDuration: '00:15:00',
                events: 'https://fullcalendar.io/api/demo-feeds/events.json'
            });

            calendar.render();

            setInterval(() => {
                calendar.refetchEvents();
            }, 1000 * 60 * 5);
        });
    </script>

    <div id="calendar" class="w-full"></div>

    <noscript class="w-full">
        <h1 class="text-center">
            The calendar is only supported with JavaScript.<br>
            Use the sidebar to book instead.
        </h1>
    </noscript>

`)
