package edu.kit.hci.soli.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CalendarEvent(String title, LocalDateTime start, LocalDateTime end, List<String> className) {
}
