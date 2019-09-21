package com.emramirez.islandtrip.repository;

import com.emramirez.islandtrip.model.CalendarDate;
import com.emramirez.islandtrip.model.CalendarDateStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarDateRepository extends CrudRepository<CalendarDate, Long> {

    List<CalendarDate> findByDateBetweenAndStatus(LocalDate lowDate, LocalDate highDate, CalendarDateStatus status);
}
