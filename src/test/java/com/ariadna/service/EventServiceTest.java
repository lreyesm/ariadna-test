package com.ariadna.service;

import com.ariadna.model.Event;
import com.ariadna.model.Source;
import com.ariadna.repository.DataStore;
import com.ariadna.util.FileLoader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {

    private EventService service;

    /**
     * @brief Prepares the in-memory test data before each unit test.
     *
     * This method is executed before each test method thanks to the @BeforeEach annotation.
     * It creates mock `Source` and `Event` objects and injects them into the `DataStore`.
     * The data is used to test the functionality of the `EventService` in isolation.
     *
     * @details
     * - Creates two mock sources: "Sensor A" and "Sensor B".
     * - Creates three events with different timestamps and values.
     * - Associates each event with its corresponding source.
     * - Sets the sources and events in the `DataStore` for use in tests.
     * - Initializes an instance of `EventService`.
     */
    @BeforeEach
    public void setup() {
        // Create mock sources
        Source sensorA = new Source(1, "Sensor A");
        Source sensorB = new Source(2, "Sensor B");

        // Create mock events
        Event e1 = new Event(1001, 1, LocalDateTime.parse("2025-01-10T10:00:00"), 45.0);
        Event e2 = new Event(1002, 2, LocalDateTime.parse("2025-01-15T12:00:00"), 60.5);
        Event e3 = new Event(1003, 1, LocalDateTime.parse("2025-02-01T08:00:00"), 75.2);

        e1.setSource(sensorA);
        e2.setSource(sensorB);
        e3.setSource(sensorA);

        // Inject into memory store
        DataStore.setSources(Arrays.asList(sensorA, sensorB));
        DataStore.setEvents(Arrays.asList(e1, e2, e3));

        this.service = new EventService();
    }

    /**
     * @brief Tests the {@code findBySourceName} method of the EventService.
     *
     * This test verifies that the service correctly returns the expected number of Event objects
     * when searching by different source names:
     * - When searching for "sensor", it expects 3 results.
     * - When searching for "A", it expects 2 results.
     * - When searching for a nonexistent source name, it expects 0 results.
     */
    @Test
    public void testFindBySourceName() {
        List<Event> results = service.findBySourceName("sensor");
        assertEquals(3, results.size());

        results = service.findBySourceName("A");
        assertEquals(2, results.size());

        results = service.findBySourceName("Nonexistent");
        assertEquals(0, results.size());
    }

    /**
     * @brief Tests the {@code findByDateRange} method of the service.
     *
     * This test verifies that the service correctly retrieves events that fall within
     * the specified date range from January 1, 2025 to January 31, 2025.
     * It asserts that the number of events returned matches the expected count.
     */
    @Test
    public void testFindByDateRange() {
        List<Event> results = service.findByDateRange(
            LocalDateTime.parse("2025-01-01T00:00:00"),
            LocalDateTime.parse("2025-01-31T23:59:59")
        );
        assertEquals(2, results.size());
    }

    /**
     * @brief Tests the {@code findByValueRange} method of the service.
     *
     * This test verifies that the service correctly returns a list of events whose values
     * fall within the specified range. It checks that the correct number of events are returned
     * for given value ranges.
     *
     * - For the range 50.0 to 80.0, it expects 2 events to be found.
     * - For the range 10.0 to 40.0, it expects no events to be found.
     */
    @Test
    public void testFindByValueRange() {
        List<Event> results = service.findByValueRange(50.0, 80.0);
        assertEquals(2, results.size());

        results = service.findByValueRange(10.0, 40.0);
        assertEquals(0, results.size());
    }

    /**
     * @brief Integration test for loading sources and events, linking them, and searching by source name.
     *
     * This test performs the following steps:
     * - Loads a list of sources from a CSV file and sets them in the DataStore.
     * - Loads events from multiple CSV files in parallel and sets them in the DataStore.
     * - Links each event with its corresponding source.
     * - Searches for events by a specific source name ("Sensor") using the EventService.
     * - Asserts that the search result is not empty and that the first event's source name contains "Sensor".
     *
     * @throws Exception if any file loading or processing fails.
     */
    @Test
    public void testIntegrationLoadingAndSearchingEvents() throws Exception {
        List<Source> sources = FileLoader.loadSources(Paths.get("resources/sources.csv"));
        DataStore.setSources(sources);

        String[] sourceFilePath = new String[]{ "resources/events_1.csv", "resources/events_2.csv" };
        List<Event> events = FileLoader.loadEventsInParallel(sourceFilePath);
        DataStore.setEvents(events);
        DataStore.linkEventsWithSources();

        EventService service = new EventService();
        List<Event> result = service.findBySourceName("Sensor");

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getSource().getName().contains("Sensor"));
    }
}
