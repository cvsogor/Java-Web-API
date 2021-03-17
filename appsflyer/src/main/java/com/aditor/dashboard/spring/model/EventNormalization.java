package com.aditor.dashboard.spring.model;

import java.util.List;

/**
 * Created by DGordiichuk on 11.01.2016.
 */
public class EventNormalization {

    public static class EventNormalizationEntry {
        public String normalizedEventName;
        public List<String> replaceableNames;

        public EventNormalizationEntry(String normalizedEventName, List<String> replaceableNames) {
            this.normalizedEventName = normalizedEventName;
            this.replaceableNames = replaceableNames;
        }

        public EventNormalizationEntry() {
        }
    }

    public List<EventNormalizationEntry>  evetentForNormalization;

    public EventNormalization(List<EventNormalizationEntry> evetentForNormalization) {
        this.evetentForNormalization = evetentForNormalization;
    }

    public EventNormalization() {
    }
}

