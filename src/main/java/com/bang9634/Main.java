package com.bang9634;

import com.bang9634.config.Config;
import com.bang9634.controller.AppController;
import com.bang9634.util.constants.ConfigConstants;

/**
 * TODO: 테스트 코드 만들기
 */

/**
 * Main 클래스는 애플리케이션의 진입점으로, 프로그램 실행 시 가장 먼저 호출되는 클래스이다.
 * <p>
 * 이 클래스는 프로그램의 초기 설정을 로드하고, 종료 시 실행할 작업을 등록한다.
 * 또한, AppController를 통해 애플리케이션의 주요 흐름을 시작한다.
 * </p>
 * <p>
 * <b>종료 시 작업:</b>
 * <ul>
 *   <li>프로그램 종료 시 Config에 저장된 서비스 키를 초기화한다.
 *       이는 사용자가 로그인 유지 설정을 하지 않은 경우에만 적용된다.</li>
 * </ul>
 * </p>
 * <p>
 * <b>예외 처리:</b>
 * <ul>
 *   <li>프로그램 실행 중 발생할 수 있는 예외는 AppController에서 처리한다.</li>
 * </ul>
 * </p>
 */
public class Main {
    static public void main(String[] args) {
        /**
         * 프로그램 종료 시 Config에 저장된 서비스 키를 초기화한다.
         * <p>
         * 이 작업은 프로그램 종료 시점에만 수행되며, 프로그램이 정상적으로
         * 종료될 때만 실행된다. 이를 통해 사용자가 로그인을 유지하지 않도록
         * 설정한 경우, 서비스 키가 초기화되어 보안을 강화한다.
         * </p>
         * <b>참고:</b>
         * .weather_config 파일의 KEEP_LOGIN이 "true"가 아닌 경우에만 서비스 키를
         * 초기화한다. 이는 사용자가 로그인 유지 설정을 하지 않은 경우에만 적용된다는 의미이다.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String keepLogin = Config.getConfig(ConfigConstants.KEEP_LOGIN);
            if (!ConfigConstants.TRUE.equals(keepLogin)) {
                Config.setConfig(ConfigConstants.SERVICE_KEY, "");
            }
        }));
        
        AppController.run();
    }
}