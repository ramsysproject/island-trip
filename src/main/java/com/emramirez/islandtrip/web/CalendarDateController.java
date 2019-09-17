package com.emramirez.islandtrip.web;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.repository.CalendarDateRepository;
import com.emramirez.islandtrip.service.CalendarDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calendar-dates")
@RequiredArgsConstructor
public class CalendarDateController {

    private final CalendarDateService calendarDateService;

    /**
     * Gets the available calendar dates. If no range is given, the default one is 30 days.
     *
     * @return a list of available ${@link CalendarDate}
     */
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalendarDate>> getAvailableDates(@RequestParam(defaultValue = "30") int range){
        List<CalendarDate> calendarDateList = calendarDateService.getAvailableCalendarDates(range);

        return ResponseEntity.ok(calendarDateList);
    }
}
