package edu.kit.hci.soli.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CalendarEvent(String url, String title, LocalDateTime start, LocalDateTime end, List<String> classNames) {
}
