package com.bang9634;

import com.bang9634.gui.WeatherDisplayGUI;
import com.bang9634.util.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * @todo    지금 인증키랑 날짜 시각, 좌표 사용자 입력으로 조정할 수 있도록 입출력 클래스 구현 <p>
 *          가능하면 CLI말고 GUI로 구현할 수 있도록.. (swwing? awt? )
 *          
 */

public class Main {
    static public void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void run() throws Exception {
        /**
         * Config 클래스를 이용해 사용자 홈 디렉토리에 있는 설정 파일을 읽어온다.
         */
        String serviceKey = getServiceKey();
        FcstData fcstData = fetchWeatherData(serviceKey);
        WeatherDisplayGUI gui = new WeatherDisplayGUI();
        gui.setVisible(true);
        gui.displayWeather(fcstData);
        for (Map.Entry<String, String> entry : fcstData.data.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /** 
     * 
     */
    private static String getServiceKey() throws IOException {
        if(!Config.isConfigFileExists()) {
            Scanner stdIn = new Scanner(System.in);
            Config.constructConfig();
            System.out.print("ServiceKey : ");
            String input = stdIn.next();
            Config.setServiceKeyOnConfig(input);
            stdIn.close();
        }
        Properties config = Config.loadConfig();
        return config.getProperty("SERVICE_KEY");
    }

    private static FcstData fetchWeatherData(String serviceKey) throws Exception {
        String baseData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500";
        String nx = "60";
        String ny = "127";
        WeatherApiClient client = new WeatherApiClient(serviceKey);
        String json = client.getWeather(baseData, baseTime, nx, ny);
        return FcstDataReader.getVilageFcstData(FcstDataReader.parseVilageFcstJsonData(json));
    }
}
