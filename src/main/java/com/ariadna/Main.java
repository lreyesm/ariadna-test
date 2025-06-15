package com.ariadna;

import com.ariadna.model.Event;
import com.ariadna.model.Source;
import com.ariadna.repository.DataStore;
import com.ariadna.service.EventService;
import com.ariadna.util.FileLoader;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting data loading...");

        String sourceFilePath = "resources/sources.csv";
        String[] eventFiles = {
                "resources/events_1.csv",
                "resources/events_2.csv",
                "resources/events_3.csv",
                "resources/events_4.csv",
                "resources/events_5.csv",
                "resources/events_6.csv"
        };

        try {
            // Load sources
            List<Source> sources = FileLoader.loadSources(Paths.get(sourceFilePath));
            DataStore.setSources(sources);

            // Load events in parallel
            List<Event> events = FileLoader.loadEventsInParallel(eventFiles);
            DataStore.setEvents(events);

            // Link events with their sources
            DataStore.linkEventsWithSources();

            System.out.println("Data loaded successfully.");
            System.out.println("Total sources: " + DataStore.getSources().size());
            System.out.println("Total events: " + DataStore.getEvents().size());

            // Example queries
            EventService service = new EventService();

            String sourceName = "Sensor C";
            System.out.println("\nEvents containing '" + sourceName + "':");
            List<Event> sensorEvents = service.findBySourceName(sourceName);
            // sensorEvents.forEach(System.out::println);
            System.out.println("\nTotal events containing '" + sourceName + "': " + sensorEvents.size());

            System.out.println("\nEvents between 2025-01-01 and 2025-01-31:");
            service.findByDateRange(
                    LocalDateTime.parse("2025-01-01T00:00:00"),
                    LocalDateTime.parse("2025-01-01T08:59:59")
            ).forEach(System.out::println);

            System.out.println("\nEvents with value between 70.0 and 72.0:");
            service.findByValueRange(70.0, 72.0).forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during loading or processing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
