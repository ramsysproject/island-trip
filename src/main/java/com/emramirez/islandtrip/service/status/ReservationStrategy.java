package com.emramirez.islandtrip.service.status;

import com.emramirez.islandtrip.model.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReservationStrategy {

    private final Map<ReservationStatus, ReservationStatusHandler> strategies;

    @Autowired
    public ReservationStrategy(List<ReservationStatusHandler> reservationStatusHandlerList) {
        strategies = reservationStatusHandlerList.stream()
                .collect(Collectors.toMap(it -> it.supports(), Function.identity()));
    }

    public Optional<ReservationStatusHandler> getHandler(ReservationStatus status) {
        return Optional.ofNullable(strategies.get(status));
    }
}
