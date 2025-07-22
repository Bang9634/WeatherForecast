package com.bang9634;

import com.bang9634.gui.NavigationManager;
import com.bang9634.model.FcstData;
import com.bang9634.util.*;

/**
 * 전체 애플리케이션의 흐름을 담당하는 컨트롤러 클래스 <p>
 * 
 * 앱의 실행 흐름을 제어한다. 중앙관리 구조 + MVC 형태로 동작한다.
 */
public class AppController {
    private static final NavigationManager navigationManager = new NavigationManager();

    /** 프로그램 흐름을 시작한다. */
    public static void run() {
        goToInitialScreen();
    }

    /** 
     * 초기 화면을 띄운다 <p>
     * Config 파일의 유무와 serviceKey의 유효성을 판단해 ServiceKeyInput 혹은 WeatherDisplay로 이동한다.
     */
    private static void goToInitialScreen() {
        String serviceKey = Config.getConfig(ConfigConstants.SERVICE_KEY);
        if (!Config.isConfigFileExists() || !new WeatherApiClient(serviceKey).isValiedServiceKey()) {
            goToServiceKeyInput();
        } else {
            goToWeatherDisplay();
        }
    }

    /** 
     * ServiceKeyInput을 띄운다. 
     * 인증 성공 시 다시 초기 화면 판단 로직을 다시 호출한다.
    */
    private static void goToServiceKeyInput() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            navigationManager.showServiceKeyInput(() -> {
                goToInitialScreen();
            });
        });
    }

    /** 
     * goToWeatherDisplay를 띄운다.
     * fetchWeatherData를 호출해 정보를 기상 예보 데이터를 가져와 WeatherDisplay에게 전달하여
     * 기상 예보 정보를 출력한다.
     * 서비스 키 초기화 버튼을 누르면 초기 화면 판단 로직을 다시 호출한다.
     */
    private static void goToWeatherDisplay() {
        String serviceKey = Config.getConfig(ConfigConstants.SERVICE_KEY);
        javax.swing.SwingUtilities.invokeLater(() -> {
            FcstData fcstData = fetchWeatherData(serviceKey, "60", "127");
            navigationManager.showWeatherDisplay(fcstData, () -> {
                goToInitialScreen();
            });
        });
    }

    /**
     * API 요청에 필요한 좌표 및 시각과 시간을 설정해 기상 예보 데이터 요청 및 응답 파싱, 파싱된 데이터를 반환한다.
     * 
     * @return  성공적으로 API 요청을 수행 및 응답을 반환한다. 예외 발생시 null을 반환한다.
     */
    public static FcstData fetchWeatherData(String serviceKey, String nx, String ny) {
        WeatherApiClient client = new WeatherApiClient(serviceKey);
        try {
            String json = client.getWeather(
                WeatherConstants.LABEL_BASE_DATE, 
                WeatherConstants.LABEL_BASE_TIME, 
                nx, 
                ny
                );
            return FcstDataReader.getVilageFcstData(FcstDataReader.parseVilageFcstJsonData(json));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
