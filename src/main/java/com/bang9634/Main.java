package com.bang9634;

import com.bang9634.gui.*;
import com.bang9634.util.*;

import java.io.IOException;
import java.util.Properties;

/**
 * @todo    지금 인증키랑 날짜 시각, 좌표 사용자 입력으로 조정할 수 있도록 입출력 클래스 구현 <p>
 *          가능하면 CLI말고 GUI로 구현할 수 있도록.. (swwing? awt? )
 *          
 */

public class Main {
    static public void main(String[] args) {
        if (!Config.isConfigFileExists() || getServiceKeyFromConfig().isEmpty()) {
            javax.swing.SwingUtilities.invokeLater(ServiceKeyInputGUI::new);
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> {
                FcstData fcstData = fetchWeatherData(getServiceKeyFromConfig());
                WeatherDisplayGUI gui = new WeatherDisplayGUI();
                gui.setVisible(true);
                gui.displayWeather(fcstData);
            });
        }
    }

    public static void showWeatherGUI() {
        try {
            FcstData fcstData = fetchWeatherData(getServiceKeyFromConfig());
            WeatherDisplayGUI gui = new WeatherDisplayGUI();
            gui.setVisible(true);
            gui.displayWeather(fcstData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * 
     */
    private static String getServiceKeyFromConfig() {
        try {
            Properties config = Config.loadConfig();
            return config.getProperty("SERVICE_KEY", "");
        } catch (IOException e) {
            return "";
        }
    }

    private static FcstData fetchWeatherData(String serviceKey) {
        String nx = "60";
        String ny = "127";
        WeatherApiClient client = new WeatherApiClient(serviceKey);
        try {
            String json = client.getWeather(WeatherConstants.LABEL_BASE_DATE, WeatherConstants.LABEL_BASE_TIME, nx, ny);
            return FcstDataReader.getVilageFcstData(FcstDataReader.parseVilageFcstJsonData(json));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}