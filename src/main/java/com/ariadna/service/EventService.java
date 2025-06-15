package com.ariadna.service;

import com.ariadna.model.Event;
import com.ariadna.repository.DataStore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {

    /**
     * Retrieves a list of {@link Event} objects whose source name contains the specified substring,
     * ignoring case sensitivity.
     *
     * @param namePart the substring to search for within the source name of each event
     * @return a list of events whose source name contains the specified substring; 
     *         returns an empty list if no matches are found
     */
    public List<Event> findBySourceName(String namePart) {
        return DataStore.getEvents().stream()
                .filter(event -> event.getSource() != null &&
                        event.getSource().getName().toLowerCase().contains(namePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of {@link Event} objects whose timestamps fall within the specified date and time range.
     *
     * @param start the start of the date and time range (inclusive)
     * @param end the end of the date and time range (inclusive)
     * @return a list of events occurring between {@code start} and {@code end}, inclusive
     */
    public List<Event> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return DataStore.getEvents().stream()
                .filter(event -> {
                    LocalDateTime ts = event.getTimestamp();
                    return (ts.isEqual(start) || ts.isAfter(start)) &&
                            (ts.isEqual(end) || ts.isBefore(end));
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of {@link Event} objects whose value falls within the specified range.
     *
     * @param min the minimum value (inclusive) of the range
     * @param max the maximum value (inclusive) of the range
     * @return a list of events with values between {@code min} and {@code max}, inclusive
     */
    public List<Event> findByValueRange(double min, double max) {
        return DataStore.getEvents().stream()
                .filter(event -> event.getValue() >= min && event.getValue() <= max)
                .collect(Collectors.toList());
    }
}
