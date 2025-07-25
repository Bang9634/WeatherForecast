package com.bang9634.controller;


import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.bang9634.config.Config;
import com.bang9634.gui.NavigationManager;
import com.bang9634.gui.WeatherDisplayGUI;
import com.bang9634.provider.WeatherProvider;
import com.bang9634.provider.impl.PublicDataPortalProvider;
import com.bang9634.provider.parser.WeatherDataParser;
import com.bang9634.service.ServiceKeyValidator;
import com.bang9634.service.WeatherService;
import com.bang9634.util.constants.ConfigConstants;

/**
 * 전체 애플리케이션의 흐름을 담당하는 컨트롤러 클래스 <p>
 * 
 * 앱의 실행 흐름을 제어한다. 중앙관리 구조 + MVC 형태로 동작한다.
 */
public class AppController {
    private static final NavigationManager navigationManager = new NavigationManager();
    private static final WeatherDataParser weatherDataParser = new WeatherDataParser();
    private static WeatherService weatherService;

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
        if (!isServiceKeyValid(serviceKey)) {
            goToServiceKeyInput();
        } else {
            initializeServices(serviceKey);
            goToWeatherDisplay();
        }
    }

    /** 
     * ServiceKeyInput을 띄운다. 
     * 인증 성공 시 다시 초기 화면 판단 로직을 다시 호출한다.
    */
    private static void goToServiceKeyInput() {
        ServiceKeyValidator serviceKeyValidator = (serviceKey) -> {
            return isServiceKeyValid(serviceKey);
        };

        Runnable successCallBack = () -> {
            goToInitialScreen();
        };

        javax.swing.SwingUtilities.invokeLater(() -> {
            navigationManager.showServiceKeyInput(serviceKeyValidator, successCallBack);
        });
    }

    /** 
     * goToWeatherDisplay를 띄운다.
     * fetchWeatherData를 호출해 정보를 기상 예보 데이터를 가져와 WeatherDisplay에게 전달하여
     * 기상 예보 정보를 출력한다.
     * 서비스 키 초기화 버튼을 누르면 초기 화면 판단 로직을 다시 호출한다.
     */
    private static void goToWeatherDisplay() {
        SwingUtilities.invokeLater(() -> {
            try {
                WeatherDisplayPresenter weatherDisplayPresenter = new WeatherDisplayPresenter(weatherService);
                WeatherDisplayGUI view = new WeatherDisplayGUI(() -> goToInitialScreen());

                weatherDisplayPresenter.setView(view);
                view.setPresenter(weatherDisplayPresenter);

                navigationManager.showFrame(view);

                weatherDisplayPresenter.loadInitialData();


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "날씨 정보를 표시하는 중 오류 발생", "오류", JOptionPane.ERROR_MESSAGE);
                goToServiceKeyInput();
            }
        });
    }

    /**
     * API 요청에 필요한 좌표 및 시각과 시간을 설정해 기상 예보 데이터 요청 및 응답 파싱, 파싱된 데이터를 반환한다.
     * 
     * TODO: 동기 처리로 인해 호출 시간이 길어지면 GUI가 멈춤. 개선의 여지 보임 #2 이슈도 아마 이때문인듯
     * 
     * @return  성공적으로 API 요청을 수행 및 응답을 반환한다. 예외 발생시 null을 반환한다.
     */
    public static void initializeServices(String serviceKey) {
        WeatherProvider weatherProvider = new PublicDataPortalProvider(serviceKey);
        weatherService = new WeatherService(weatherProvider, weatherDataParser);
    }

    private static boolean isServiceKeyValid(String serviceKey) {
        WeatherProvider tempProvider = new PublicDataPortalProvider(serviceKey);
        WeatherService tempService = new WeatherService(tempProvider, weatherDataParser);
        return tempService.isServiceKeyValid();
    }
}
