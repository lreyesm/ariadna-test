package com.ariadna.repository;

import com.ariadna.model.Event;
import com.ariadna.model.Source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    private static List<Source> sources;
    private static List<Event> events;

    public static void setSources(List<Source> sourceList) {
        sources = sourceList;
    }

    public static void setEvents(List<Event> eventList) {
        events = eventList;
    }

    public static List<Source> getSources() {
        return sources;
    }

    public static List<Event> getEvents() {
        return events;
    }

    /**
     * @brief Links each Event in the events list with its corresponding Source from the sources list.
     *
     * This method iterates through all available events and assigns the appropriate Source object
     * to each Event based on the source ID. It first constructs a map of Source objects keyed by their IDs
     * for efficient lookup. If either the sources or events list is null, the method returns immediately.
     *
     * @note Assumes that the sources and events collections are accessible and populated.
     */
    public static void linkEventsWithSources() {
        if (sources == null || events == null) return;

        Map<Integer, Source> sourceMap = new HashMap<>();
        for (Source source : sources) sourceMap.put(source.getId(), source);

        for (Event event : events) {
            Source src = sourceMap.get(event.getSourceId());
            if (src != null) event.setSource(src);
        }
    }

    /**
     * Retrieves an {@link Event} object from the list of events by its unique identifier.
     *
     * @param eventId the unique identifier of the event to retrieve
     * @return the {@link Event} with the specified ID, or {@code null} if no such event exists or the events list is uninitialized
     */
    public static Event getEventById(int eventId) {
        if (events == null) return null;

        for (Event event : events) {
            if (event.getId() == eventId) return event;
        }
        return null;
    }

    /**
     * Retrieves a {@link Source} object from the list of sources by its unique identifier.
     *
     * @param sourceId the unique identifier of the source to retrieve
     * @return the {@link Source} with the specified ID, or {@code null} if not found or if the sources list is {@code null}
     */
    public static Source getSourceById(int sourceId) {
        if (sources == null) return null;

        for (Source source : sources) {
            if (source.getId() == sourceId) return source;
        }
        return null;
    } 

    /**
     * @brief Clears the data store by resetting all stored sources and events.
     *
     * This method sets the internal sources and events to {@code null},
     * effectively removing all stored data from the data store.
     * Use this method to reset the repository to its initial state.
     */
    public static void clear() {
        sources = null;
        events = null;
    }
}
