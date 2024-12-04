package edu.kit.hci.soli.dto;

import edu.kit.hci.soli.domain.Booking;
import edu.kit.hci.soli.service.BookingsService;

import java.util.List;

public sealed interface BookingAttemptResult {
    record Success(Booking booking) implements BookingAttemptResult {
    }

    record Staged(Booking booking) implements BookingAttemptResult {
    }

    record Failure(List<Booking> conflict) implements BookingAttemptResult {
    }

    sealed interface PossibleCooperation extends BookingAttemptResult {
        List<Booking> override();

        List<Booking> cooperate();

        List<Booking> contact();

        record Immediate(List<Booking> override, List<Booking> cooperate) implements PossibleCooperation {
            @Override
            public List<Booking> contact() {
                return List.of();
            }
        }

        record Deferred(List<Booking> override, List<Booking> contact,
                        List<Booking> cooperate) implements PossibleCooperation {
        }
    }
}
