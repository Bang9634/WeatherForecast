package com.bang9634;

import com.bang9634.gui.NavigationManager;
import com.bang9634.util.*;

/**
 * 전체 애플리케이션의 흐름을 담당하는 컨트롤러 <p>
 * 
 * 앱의 실행 흐름을 제어한다.
 */
public class AppController {
    private static final NavigationManager navigationManager = new NavigationManager();
    public static void run() {
        /** 
         * Config 파일이 존재하지 않거나, 파일안에 ServiceKey가 없다면 ServiceKeyInputGUI를 출력한다. <p>
         * Config 파일이 이미 존재하고, ServiceKey가 있다면 ServiceKeyInputGUI를 건너뛰고 WeatherDisplayGUi를 바로 출력한다 <p>
         * 
         * TODO: Config 파일이 존재하고 serviceKey가 작성되있지만 유효하지 않은 serviceKey일 경우, 프로그램 실행시 GUI가 나오지않고 비정상 종료됨. 조건문에서 유효성 검사를 해야될 것으로 보임.
         */
        if (!Config.isConfigFileExists() || Config.getConfig(ConfigConstants.SERVICE_KEY).isEmpty()) {
            /** Config 파일이 존재하지 않거나, 파일안에 ServiceKey가 존재하지 않는 경우 */
            javax.swing.SwingUtilities.invokeLater(() -> {
                /** serviceKey 입력 후 유효한 경우 Config 파일에 입력한 serviceKey를 저장한다. */
                navigationManager.showServiceKeyInput(()-> {
                    /** 
                     * 인증 성공 시 Config 파일로부터 serviceKey를 불러와 날씨 정보 초기화를 하고,
                     * 기상 예보 GUI를 출력한다.
                     */
                    FcstData fcstData = fetchWeatherData(Config.getConfig(ConfigConstants.SERVICE_KEY));
                    navigationManager.showWeatherDisplay(fcstData);
                });
            });
        } else {
            /** Config 파일이 존재하고 serviceKey가 존재하면 바로 기상 정보 GUI를 출력한다. */
            javax.swing.SwingUtilities.invokeLater(() -> {
                FcstData fcstData = fetchWeatherData(Config.getConfig(ConfigConstants.SERVICE_KEY));
                navigationManager.showWeatherDisplay(fcstData);
            });
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
