package com.bang9634;

import java.io.*;
import java.util.*;

/**
 * serviceKey와 같은 설정을 불러오는 클래스 <p>
 * 
 * macOS기준 <p>
 * 사용자 home 디렉토리 내부에 .weather_config 파일이 존재해야함. <p>
 * SERVICE_KEY=(인증 키) 작성 시 프로그램에서 인증 키를 인식하여 활성화된 인증 키라면 
 * 프로그램이 정상적으로 빌드됨. 그렇지 않다면 부적절한 서비스 키 메세지 출력.
 */
public class Config {
    /** 
     * 홈 디렉토리에 .weather_config 파일을 열어 props 프로퍼티가 읽도록 하며 이를 반환한다.
     */
    public static Properties loadConfig() throws IOException {
        String home = System.getProperty("user.home");
        File configFile = new File(home, ".weather_config");
        Properties props = new Properties();
        try (FileReader reader = new FileReader(configFile)) {
            props.load(reader);
        }
        return props;
    }
}
