package com.bang9634.service;

import com.bang9634.provider.WeatherProvider;
import com.bang9634.provider.parser.WeatherDataParser;
import com.bang9634.util.constants.WeatherConstants;
import com.bang9634.model.FcstData;

/**
 * WeatherService 클래스는 날씨 정보를 제공하는 서비스로, 
 * WeatherProvider와 WeatherDataParser를 사용하여 날씨 데이터를 가져오고 파싱한다.
 * <p>
 * 이 클래스는 외부 API에서 날씨 정보를 가져오고, 이를 FcstData 객체로 변환하는 기능을 제공한다.
 * 또한, 서비스 키의 유효성을 검사하는 메서드도 포함되어 있다.
 * 
 * @author bangdeokjae
 */
public class WeatherService {
    /**
     * WeatherProvider와 WeatherDataParser를 사용하여 날씨 정보를 가져오고 파싱하는 서비스 클래스.
     * <p>
     * 이 클래스는 외부 API에서 날씨 정보를 가져오고, 이를 FcstData 객체로 변환하는 기능을 제공한다.
     * 또한, 서비스 키의 유효성을 검사하는 메서드도 포함되어 있다.
     */
    private final WeatherProvider weatherProvider;
    private final WeatherDataParser weatherDataParser;

    /**
     * WeatherService 생성자.
     * <p>
     * WeatherProvider와 WeatherDataParser를 초기화한다.
     * 
     * @param   weatherprovider
     *          날씨 데이터를 제공하는 WeatherProvider 객체
     * @param   weatherDataParser
     *          날씨 데이터를 파싱하는 WeatherDataParser 객체
     */
    public WeatherService(WeatherProvider weatherprovider, WeatherDataParser weatherDataParser) {
        this.weatherProvider = weatherprovider;
        this.weatherDataParser = weatherDataParser;
    }

    /**
     * 주어진 좌표(nx, ny)에 대한 날씨 정보를 가져온다.
     * <p>
     * 이 메서드는 외부 API를 호출하여 날씨 데이터를 가져오고, 이를 FcstData 객체로 변환한다.
     * 
     * @param   nx
     *          예보지점 x좌표
     * @param   ny
     *          예보지점 y좌표
     * @return  FcstData 객체로 변환된 날씨 정보
     * @throws  Exception
     *          외부 API 호출 중 오류가 발생할 경우 예외를 던진다.
     */
    public FcstData getWeather(String nx, String ny) throws Exception {
        String rawJson;
        try {
            rawJson = weatherProvider.fetchRawWeatherData(
                WeatherConstants.BASE_DATE, 
                WeatherConstants.BASE_TIME, 
                nx, ny
            );
        } catch (Exception e) {
            throw new Exception("외부 API에서 데이터 가져오는 중 오류 발생", e);
        }
        return weatherDataParser.parse(rawJson);
    }

    /**
     * 서비스 키의 유효성을 검증한다.
     * <p>
     * 서비스 키를 검증하기 위해 임의의 파라미터로 getWeather 메소드를 호출한다.
     * 만약 예외가 발생하지 않으면 유효한 서비스 키로 간주한다.
     * 
     * @return  boolean
     *          서비스 키가 유효하면 true, 그렇지 않으면 false를 반환한다.
     */
    public boolean isServiceKeyValid() {
        try {
            getWeather("60", "127");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
