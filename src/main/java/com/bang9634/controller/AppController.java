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
 * AppController는 애플리케이션의 주요 흐름을 제어하는 클래스.
 * <p>
 * 프로그램의 초기 화면을 결정하고, 서비스 키의 유효성을 검사하여
 * 적절한 화면(ServiceKeyInput 또는 WeatherDisplay)으로 이동한다.
 * 또한 WeatherService 및 관련 의존성을 초기화하며,
 * 화면 전환 및 예외 처리 등 전체 애플리케이션의 흐름을 관리한다.
 * </p>
 *
 * <ul>
 *   <li>{@link #run()} - 애플리케이션 실행의 진입점이다.</li>
 *   <li>{@link #goToInitialScreen()} - 서비스 키 유효성에 따라 초기 화면을 결정한다.</li>
 *   <li>{@link #goToServiceKeyInput()} - 서비스 키 입력 화면을 표시한다.</li>
 *   <li>{@link #goToWeatherDisplay()} - 날씨 정보 표시 화면을 초기화하고 표시한다.</li>
 *   <li>{@link #initializeServices(String)} - WeatherService 및 Provider를 초기화한다.</li>
 *   <li>{@link #isServiceKeyValid(String)} - 서비스 키의 유효성을 검사한다.</li>
 * </ul>
 *
 * <b>사용 예시:</b>
 * <pre>
 *     AppController.run();
 * </pre>
 *
 * @author  bangdeokjae
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
     * serviceKey의 유효성을 판단해 ServiceKeyInput 혹은 WeatherDisplay로 이동한다.
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
     * ServiceKeyInput 화면을 출력한다. <p>
     * ServiceKeyValidator를 생성하고, 인증 성공 시 다음 동작을 정의한다.
     * 인증 성공 시 WeatherDisplay 화면으로 이동한다.
     * @param   serviceKeyValidator
     *         서비스 키의 유효성을 검증하는 인터페이스를 매개변수로 받는다.
    */
    private static void goToServiceKeyInput() {
        ServiceKeyValidator serviceKeyValidator = (serviceKey) -> {
            return isServiceKeyValid(serviceKey);
        };

        /** 인증 성공 시 WeatherDisplay 화면으로 이동한다. */
        Runnable successCallBack = () -> {
            goToInitialScreen();
        };

        /** ServiceKeyInput을 화면에 출력할 것을 지시한다. */
        javax.swing.SwingUtilities.invokeLater(() -> {
            navigationManager.showServiceKeyInput(serviceKeyValidator, successCallBack);
        });
    }

    /** 
     * WeatherDisplay 화면을 출력한다. 
     * <p>
     * WeatherService를 초기화하고, WeatherDisplayPresenter를 생성하여
     * WeatherDisplayGUI에 연결한다.
     * WeatherDisplayPresenter는 WeatherService를 통해 날씨 정보를 가져오고,
     * WeatherDisplayGUI에 표시한다.
     */
    private static void goToWeatherDisplay() {
        SwingUtilities.invokeLater(() -> {
            try {
                /** 서비스 키를 가져와 WeatherService를 초기화한다. */
                WeatherDisplayPresenter weatherDisplayPresenter = new WeatherDisplayPresenter(weatherService);
                WeatherDisplayGUI view = new WeatherDisplayGUI(() -> goToInitialScreen());

                /** WeatherDisplayPresenter와 WeatherDisplayGUI를 연결한다. */
                weatherDisplayPresenter.setView(view);
                view.setPresenter(weatherDisplayPresenter);

                navigationManager.showFrame(view);

                /** 초기 데이터를 로드한다. */
                weatherDisplayPresenter.loadInitialData();
            } catch (Exception e) {
                // 예외 발생 시, 오류 메시지를 표시하고 ServiceKeyInput으로 돌아간다.
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "날씨 정보를 표시하는 중 오류 발생", "오류", JOptionPane.ERROR_MESSAGE);
                goToServiceKeyInput();
            }
        });
    }

    /**
     * 서비스를 초기화한다. 
     * <p>
     * 서비스 키를 매개변수로 받아 WeatherProvider를 생성하고,
     * WeatherService를 초기화한다.
     * 
     * @param   serviceKey
     *          서비스 키를 매개변수로 받아 WeatherProvider를 생성하고,
     *          WeatherService를 초기화한다.
     */
    public static void initializeServices(String serviceKey) {
        WeatherProvider weatherProvider = new PublicDataPortalProvider(serviceKey);
        weatherService = new WeatherService(weatherProvider, weatherDataParser);
    }

    /**
     * 서비스 키의 유효성을 검증한다.
     * <p>
     * 서비스 키가 유효한지 확인하기 위해 WeatherService를 생성하고,
     * getWeather 메소드를 호출한다.
     * 만약 예외가 발생하지 않으면 유효한 서비스 키로 간주한다.
     * 
     * @param   serviceKey
     *         서비스 키를 매개변수로 받아 유효성을 검증한다.
     * @return  boolean
     *          서비스 키가 유효하면 true, 그렇지 않으면 false를 반환한다
     */
    private static boolean isServiceKeyValid(String serviceKey) {
        WeatherProvider tempProvider = new PublicDataPortalProvider(serviceKey);
        WeatherService tempService = new WeatherService(tempProvider, weatherDataParser);
        return tempService.isServiceKeyValid();
    }
}