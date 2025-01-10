@import edu.kit.hci.soli.dto.LayoutParams
@import edu.kit.hci.soli.config.template.JteContext
@import edu.kit.hci.soli.config.template.PageSpec
@import org.springframework.security.web.csrf.CsrfToken
@import java.time.DayOfWeek
@import java.time.Month
@import java.util.Map

@param JteContext context
@param LayoutParams layout
@param CsrfToken csrf

@param Map<DayOfWeek, Long> bookingsPerWeekdayAllTime
@param Map<DayOfWeek, Long> bookingsPerWeekdayRecent
@param Map<Integer, Long> bookingsPerHourAllTime
@param Map<Integer, Long> bookingsPerHourRecent
@param Map<Month, Long> bookingsPerMonthAllTime
@param Map<Month, Long> bookingsPerMonthRecent

@template.layout(page = new PageSpec(context.lookup("administration.statistics"), "Soli"), context = context, layout = layout, content = @`
    <div role="tablist" class="tabs tabs-boxed w-full">
        <input type="radio" name="frame_tabs" role="tab" class="tab" aria-label="${context.localize("administration.statistics.recent")}" checked>
        <div role="tabpanel" class="tab-content bg-base-100 border-base-300 rounded-box p-6 prose text-center">
            <h1>${context.localize("administration.statistics.weekday")}</h1>
            @if(bookingsPerWeekdayRecent.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerWeekdayRecent.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerWeekdayRecent.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${context.format(e.getKey())}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif

            <div class="divider"></div>

            <h1>${context.localize("administration.statistics.hour")}</h1>
            @if(bookingsPerHourRecent.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerHourRecent.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerHourRecent.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${e.getKey()}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif

            <div class="divider"></div>

            <h1>${context.localize("administration.statistics.month")}</h1>
            @if(bookingsPerMonthRecent.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerMonthRecent.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerMonthRecent.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${context.format(e.getKey())}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif
        </div>
        <input type="radio" name="frame_tabs" role="tab" class="tab" aria-label="${context.localize("administration.statistics.all_time")}">
        <div role="tabpanel" class="tab-content bg-base-100 border-base-300 rounded-box p-6 prose text-center">
            <h1>${context.localize("administration.statistics.weekday")}</h1>
            @if(bookingsPerWeekdayAllTime.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerWeekdayAllTime.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerWeekdayAllTime.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${context.format(e.getKey())}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif

            <div class="divider"></div>

            <h1>${context.localize("administration.statistics.hour")}</h1>
            @if(bookingsPerHourAllTime.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerHourAllTime.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerHourAllTime.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${e.getKey()}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif

            <div class="divider"></div>

            <h1>${context.localize("administration.statistics.month")}</h1>
            @if(bookingsPerMonthAllTime.size() <= 1)
                <p>${context.localize("administration.statistics.no_data")}</p>
            @else
                <ul class="not-prose chart">
                    !{var max = bookingsPerMonthAllTime.values().stream().mapToDouble(s -> s).max().orElse(1);}
                    @for (var e : bookingsPerMonthAllTime.entrySet())
                        <li>
                            <span
                                    style="height: ${e.getValue() * 100 / max}%"
                                    x-label="${context.format(e.getKey())}"
                                    x-value="${e.getValue()}"
                                    title="${context.localize("administration.statistics.n_bookings", e.getValue())}"
                            ></span>
                        </li>
                    @endfor
                </ul>
                <br>
            @endif
        </div>
    </div>
`)
