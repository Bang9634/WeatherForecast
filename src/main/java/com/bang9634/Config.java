package com.bang9634;

import java.io.*;
import java.util.*;

import com.bang9634.util.ConfigConstants;

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
     * 
     * @exception   IOException
     *              Config 파일 생성에 실패 시 예외를 던진다.
     */
    public static void constructConfig() throws IOException {
        if (CONFIG_FILE.exists()) return;
        Properties props = new Properties();
        props.setProperty(ConfigConstants.SERVICE_KEY, "");
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            props.store(writer, "Weather API Config");
        }
    }

    /**
     * 사용자 홈 디렉토리에 존재하는 .weather_config에 key, value값을 받아 set한다. <p>
     * Config 파일에는 (key)=(value) 방식으로 저장된다.
     * 
     * @param   key
     *          Config 파일에 저장할 key를 매개변수로 받는다.
     * 
     * @param   value
     *          해당하는 key에 value를 저장한다.
     * 
     */
    public static void setConfig(String key, String value) {
        try {
            Properties props = loadConfig();
            props.setProperty(key, value);
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                props.store(writer, "Weather API Config");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Config 파일로부터 매개변수 key에 해당하는 값을 String타입으로 반환한다.
     * 매개변수 key와 일치하는 값이 없을 경우 디폴트 값으로 ""를 반환한다.
     * 
     * @param   key
     *          Config파일에서 불러오고자 하는 값의 key를 인자로 넘긴다.
     * 
     * @return  Config 파일에서 매개변수 key에 해당하는 값을 반환한다.
     */
    public static String getConfig(String key) {
        try {
            Properties config = Config.loadConfig();
            return config.getProperty(key, "");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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