package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

/**
 * Controller class for showing statistics.
 */
@Controller("/admin/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    /**
     * Constructs a new StatisticsController instance.
     *
     * @param statisticsService the statistics service to be used by this controller
     */
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Handles GET requests to the /admin/statistics endpoint.
     * Adds various booking statistics to the model.
     *
     * @param model the model to which attributes are added
     * @return the name of the view to be rendered
     */
    @GetMapping("/admin/statistics")
    public String showStatistics(Model model) {
        Duration recentFrame = Duration.ofDays(7 * 3);
        model.addAttribute("bookingsPerWeekdayAllTime", statisticsService.countBookingsPerWeekdayAllTime());
        model.addAttribute("bookingsPerWeekdayRecent", statisticsService.countBookingsPerWeekdayRecent(recentFrame));
        model.addAttribute("bookingsPerHourAllTime", statisticsService.countBookingsPerHourAllTime());
        model.addAttribute("bookingsPerHourRecent", statisticsService.countBookingsPerHourRecent(recentFrame));
        model.addAttribute("bookingsPerMonthAllTime", statisticsService.countBookingsPerMonthAllTime());
        model.addAttribute("bookingsPerMonthRecent", statisticsService.countBookingsPerMonthRecent(recentFrame));
        return "admin/statistics";
    }
}
