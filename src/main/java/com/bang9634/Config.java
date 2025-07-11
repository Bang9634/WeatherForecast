package com.bang9634;

import java.io.*;
import java.util.*;

/**
 * serviceKey와 같은 설정을 불러오는 클래스 <p>
 * 
 * macOS기준 <p>
 * 사용자 home 디렉토리 내부에 .weather_config 파일이 존재해야한다. <p>
 * SERVICE_KEY=(인증 키) 작성 시 프로그램에서 인증 키를 인식하여 활성화된 인증 키라면 
 * 프로그램이 정상적으로 빌드된다. 그렇지 않다면 부적절한 서비스 키 메세지를 출력한다.
 */
public class Config {
    /** 저장할 Config File의 이름 */
    private static final String CONFIG_FILENAME = ".weather_config";
    /** 사용자 홈 디렉토리 경로에 CONFIG_FILENAME 이름인 파일 객체 */
    private static final File CONFIG_FILE = new File(System.getProperty("user.home"), CONFIG_FILENAME);

    /** 
     * 홈 디렉토리에 Config File 파일을 불러온 props 프로퍼티를 반환한다. <p>
     * 
     * @return  Config File 내용을 불러온 props 객체를 반환한다. Config File이 존재하지 않으면
     *          빈 props를 반환한다.
     */
    public static Properties loadConfig() throws IOException {
        Properties props = new Properties();
        /** FileReader로 configFile을 읽어온다. 이후 읽어온 파일을 props객체로 불러온다. */
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                props.load(reader);
            }
        }   
        return props;
    }

    /**
     * 사용자 홈 디렉토리에 .weather_config 파일이 존재하지 않으면 config파일을 생성한다.
     */
    public static void constructConfig() throws IOException {
        if (CONFIG_FILE.exists()) return;
        Properties props = new Properties();
        props.setProperty("SERVICE_KEY", "");
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            props.store(writer, "Weather API Config");
        }
    }

    /**
     * 사용자 홈 디렉토리에 존재하는 .weather_config에 serviceKey를 set한다.
     * 
     * @param   serviceKey
     *          API를 제공하는 웹사이트에서 부여하는 인증키를 매개변수로 전달한다.
     * 
     * @throws  IOException
     *          홈 디렉토리에 .weather_config 파일이 존재하지않으면 IOException 예외를 던진다.
     */
    public static void setServiceKeyOnConfig(String serviceKey) throws IOException {
        Properties props = loadConfig();
        props.setProperty("SERVICE_KEY", serviceKey);
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            props.store(writer, "Weather API Config");
        }
    }

    /** 
     * .weather_config 파일 유무를 참 거짓으로 반환한다.
     * 
     * @return  .weather_config 파일이 존재하면 true, 존재하지않으면 false를 반환한다.
     */
    public static boolean isConfigFileExists() {
        return CONFIG_FILE.exists();
    }

    /**
     * 사용자 홈 디렉토리에 존재하는 Config File 객체를 반환한다.
     * 
     * @return  Config File객체를 반환한다.
     */
    public static File getConfigFile() {
        return CONFIG_FILE;
    }
    
    /**
     * 사용자 홈 디렉토리의 Config File의 절대 경로를 반환한다.
     * 
     * @return  Config File의 절대 경로를 반환한다.
     */
    public static String getConfigFilePath() {
        return CONFIG_FILE.getAbsolutePath();
    }
}