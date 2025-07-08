package com.bang9634;

import com.bang9634.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

/**
 * @todo    지금 인증키랑 날짜 시각, 좌표 사용자 입력으로 조정할 수 있도록 입출력 클래스 구현 <p>
 *          가능하면 CLI말고 GUI로 구현할 수 있도록.. (swwing? awt? )
 *          
 */

public class Main {
    static public void main(String[] args) {
        run();
    }

    static private void run() {
        String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500";
        String nx = "60";
        String ny = "127";
        try {
            /**
             * Config 클래스를 이용해 사용자 홈 디렉토리에 있는 설정 파일을 읽어온다.
             */
            Properties config = Config.loadConfig();
            String serviceKey = config.getProperty("SERVICE_KEY");

            WeatherApiClient client = new WeatherApiClient(serviceKey);

            try {
                String json = client.getWeather(baseDate, baseTime, nx, ny);
                FcstData fcstData = FcstDataReader.getVilageFcstData(FcstDataReader.parseVilageFcstJsonData(json));
                for (Map.Entry<String, String> entry : fcstData.data.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

            } catch (Exception e) {
                System.out.println("API 호출 실패 : " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("UNAVALIBLE SERVICE_KEY");
        }
    }
}
