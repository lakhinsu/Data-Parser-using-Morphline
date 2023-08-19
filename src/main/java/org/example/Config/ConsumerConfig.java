package org.example.Config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ConsumerConfig {
    String configFile;
    JSONParser parser = new JSONParser();
    ArrayList<ConfigProperty> configProperties;

    public ConsumerConfig(String configFile) throws IOException, ParseException {
        this.configFile = configFile;
        this.configProperties = new ArrayList<ConfigProperty>();
        Object obj = parser.parse(new FileReader(configFile));
        JSONObject jsonObject =  (JSONObject) obj;
        JSONArray configs = (JSONArray) jsonObject.get("config");
        Iterator<JSONObject> iterator = configs.iterator();
        while (iterator.hasNext()) {
            JSONObject config = iterator.next();
            String morphlinePath = (String) config.get("morphline");
            String morphlineId = (String) config.get("id");
            ArrayList<String> topicsArray = new ArrayList<>();
            JSONArray topicsJson = (JSONArray) config.get("topics");
            Iterator<String> topicsIterator = topicsJson.iterator();
            while (topicsIterator.hasNext()) {
                topicsArray.add(topicsIterator.next());
            }
            ConfigProperty configProperty = new ConfigProperty(morphlinePath, morphlineId, topicsArray);
            this.configProperties.add(configProperty);
        }
    }

    public ArrayList<ConfigProperty> getConfigProperties() {
        return this.configProperties;
    }
}

