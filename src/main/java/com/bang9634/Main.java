package com.bang9634;

import com.bang9634.util.ConfigConstants;

public class Main {
    static public void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String keepLogin = Config.getConfig(ConfigConstants.KEEP_LOGIN);
            if (!ConfigConstants.TRUE.equals(keepLogin)) {
                Config.setConfig(ConfigConstants.SERVICE_KEY, "");
            }
        }));
        AppController.run();
    }
}