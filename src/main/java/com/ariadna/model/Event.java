package com.ariadna.model;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private int sourceId;
    private LocalDateTime timestamp;
    private double value;
    private Source source; // to have the embedded object

    public Event(int id, int sourceId, LocalDateTime timestamp, double value) {
        this.id = id;
        this.sourceId = sourceId;
        this.timestamp = timestamp;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Event{id=" + id + ", timestamp=" + timestamp + ", value=" + value + ", source=" + source + "}";
    }
}
