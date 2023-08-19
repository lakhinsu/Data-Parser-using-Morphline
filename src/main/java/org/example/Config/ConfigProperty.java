package org.example.Config;

import java.util.ArrayList;

public class ConfigProperty {
    String morphlinePath;
    String morphlineId;
    ArrayList<String> topics;

    public ConfigProperty(String morphlinePath, String morphlineId, ArrayList<String> topics) {
        this.morphlineId = morphlineId;
        this.morphlinePath = morphlinePath;
        this.topics = topics;
    }

    public String getMorphlinePath() {
        return this.morphlinePath;
    }

    public String getMorphlineId() {
        return this.morphlineId;
    }

    public ArrayList<String> getTopics() {
        return this.topics;
    }
}
