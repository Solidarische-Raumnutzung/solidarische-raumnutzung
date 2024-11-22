@import edu.kit.hci.soli.DemoModel
@import edu.kit.hci.soli.template.JteContext
@import edu.kit.hci.soli.template.PageSpec

@param DemoModel model
@param JteContext context

@template.layout(page = new PageSpec("Index", "Soli"), context = context, content = @`
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
        });
    </script>

    <div id="calendar" class="w-full"></div>

`)
