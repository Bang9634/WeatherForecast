package com.bang9634;

import java.io.*;
import java.util.*;

public class Config {
    public static Properties loadConfig() throws IOException {
        String home = System.getProperty("user.home");
        File configFile = new File(home, ".weather_config");
        Properties props = new Properties();
        try (FileReader reader = new FileReader(configFile)) {
            props.load(reader);
        }
        return props;
    }
}
