package com.bang9634;

import com.bang9634.gui.*;
import com.bang9634.util.*;

import java.io.IOException;
import java.util.Properties;


public class Main {
    static public void main(String[] args) {
        /** 
         * Config 파일이 존재하지 않거나, 파일안에 ServiceKey가 없다면 ServiceKeyInputGUI를 출력한다. <p>
         * Config 파일이 이미 존재하고, ServiceKey가 있다면 ServiceKeyInputGUI를 건너뛰고 WeatherDisplayGUi를 바로 출력한다 <p>
         * 
         */
        if (!Config.isConfigFileExists() || getServiceKeyFromConfig().isEmpty()) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                ServiceKeyInputGUI gui = new ServiceKeyInputGUI();
                gui.setVisible(true);
            });
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> {
                FcstData fcstData = fetchWeatherData(getServiceKeyFromConfig());
                WeatherDisplayGUI gui = new WeatherDisplayGUI();
                gui.setWeatherData(fcstData);
                gui.setVisible(true);
            });
        }
    }

    /** 
     * 날씨출력 메서드.
     * 
     * @todo    Main에 있으면 안됨. 스파게티 코드 됨. 수정해서 WeatherDisplayGUI로 옮기기. 
     */
    public static void showWeatherGUI() {
        try {
            FcstData fcstData = fetchWeatherData(getServiceKeyFromConfig());
            WeatherDisplayGUI gui = new WeatherDisplayGUI();
            gui.setVisible(true);
            gui.setWeatherData(fcstData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * Config 파일로부터 serviceKey를 불러들여와 String타입으로 반환한다.
     * 
     * @return  serviceKey를 String타입으로 반환한다.
     */
    private static String getServiceKeyFromConfig() {
        try {
            Properties config = Config.loadConfig();
            return config.getProperty("SERVICE_KEY", "");
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * API 요청에 필요한 좌표 및 시각과 시간을 설정해 기상 예보 데이터 요청 및 응답 파싱, 파싱된 데이터를 반환한다.
     * 
     * @return  성공적으로 API 요청을 수행 및 응답을 반환한다. 예외 발생시 null을 반환한다.
     */
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