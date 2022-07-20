package com.demo.cache.utils;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType eventType;
    Map<String, String> exts = new HashMap<>();

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
