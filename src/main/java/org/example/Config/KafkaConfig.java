package org.example.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class KafkaConfig {
    String configPath;

    public KafkaConfig(String configPath) {
        this.configPath = configPath;
    }

    public Properties getConfig() throws IOException, FileNotFoundException {
        FileInputStream propsInput = new FileInputStream(this.configPath);
        Properties prop = new Properties();
        prop.load(propsInput);
        return prop;
    }

}
