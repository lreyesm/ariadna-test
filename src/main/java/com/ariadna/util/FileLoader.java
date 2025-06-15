package com.ariadna.util;

import com.ariadna.model.Event;
import com.ariadna.model.Source;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;

public class FileLoader {

    /**
     * @brief Loads a list of {@link Source} objects from a CSV file.
     *
     * Reads a CSV file where each line contains a source ID and name,
     * separated by commas. Each line is parsed into a {@link Source} object.
     *
     * @param path The {@link Path} to the CSV file containing source data.
     * @return A {@link List} of {@link Source} objects parsed from the file.
     *
     * @throws IOException If the file cannot be read.
     */
    public static List<Source> loadSources(Path path) throws IOException {
        List<Source> sources = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Expected format: id,nombre
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    sources.add(new Source(id, name));
                }
            }
        }
        return sources;
    }

    /**
     * Loads events from multiple files in parallel using a fixed thread pool.
     *
     * @param filePaths An array of file paths from which to load events.
     * @return A list containing all events loaded from the specified files.
     * @throws InterruptedException If the current thread was interrupted while waiting.
     * @throws ExecutionException If a computation threw an exception during execution.
     *
     * This method submits a loading task for each file path to an executor service,
     * waits for all tasks to complete, and aggregates the results into a single list.
     */
    public static List<Event> loadEventsInParallel(String[] filePaths) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(filePaths.length);
        List<Future<List<Event>>> futures = new ArrayList<>();

        for (String filePath : filePaths) {
            futures.add(executor.submit(() -> loadEventsFromFile(filePath)));
        }
        List<Event> allEvents = new ArrayList<>();
        for (Future<List<Event>> future : futures) allEvents.addAll(future.get());

        executor.shutdown();
        return allEvents;
    }

    /**
     * @brief Loads a list of Event objects from a file.
     *
     * Reads a file specified by the given path, where each line is expected to be in the format:
     * id,fuente_id,timestamp,valor. Each line is parsed into an Event object and added to the resulting list.
     *
     * @param path The path to the file containing event data.
     * @return A list of Event objects parsed from the file. Returns an empty list if the file cannot be read.
     */
    private static List<Event> loadEventsFromFile(String path) {
        List<Event> events = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Event event = parseEvent(line);
                if (event != null) events.add(event);
            }
        } catch (IOException e) {
            System.err.println("Error reading events file: " + path);
            e.printStackTrace();
        }
        return events;
    }

    /**
     * @brief Parses a CSV line into an {@link Event} object.
     *
     * This method splits a CSV-formatted string by commas and extracts the event fields:
     * ID, source ID, timestamp, and value. It returns a new {@link Event} if parsing succeeds.
     *
     * @param line A CSV-formatted string in the format: "id,sourceId,timestamp,value".
     * @return An {@link Event} object parsed from the line, or {@code null} if the line is invalid or malformed.
     */
    private static Event parseEvent(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            int sourceId = Integer.parseInt(parts[1].trim());
            LocalDateTime timestamp = LocalDateTime.parse(parts[2].trim());
            double value = Double.parseDouble(parts[3].trim());
            return new Event(id, sourceId, timestamp, value);
        } 
        catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Error parsing event line: " + line);
            return null;
        }
    }
}
