package com.bang9634.provider;

/**
 * WeatherProvider 인터페이스는 기상 데이터를 제공하는 클래스들이 구현해야 하는 메서드를 정의한다.
 * <p>
 * 이 인터페이스는 외부 API를 호출하여 날씨 데이터를 가져오는 기능을 제공한다.
 * <p>
 * 주요 메서드:
 * <ul>
 *   <li>{@link #fetchRawWeatherData(String, String, String, String)}
 *       - 외부 API를 호출하여 날씨 데이터를 가져온다.</li>
 * </ul>
 * <p>
 * 이 인터페이스를 구현하는 클래스는 기상청의 공공 데이터 포털 API와 같은 외부 API를 사용하여
 * 날씨 데이터를 요청하고 응답받는 기능을 제공해야 한다.
 * 
 * @see PublicDataPortalProvider
 * @author bangdeokjae
 */
public interface WeatherProvider {

    /**
     * 외부 API를 호출하여 날씨 데이터를 가져온다.
     * <p>
     * 이 메서드는 baseDate, baseTime, nx, ny 좌표를 사용하여
     * 날씨 데이터를 요청하고, 응답받은 데이터를 문자열 형태로 반환한다.
     * @param   baseDate
     *          발표일자
     * @param   baseTime
     *          발표시각
     * @param   nx
     *          예보지점 x좌표
     * @param   ny
     *          예보지점 y좌표
     * @return  raw 날씨 데이터를 문자열 형태로 반환한다.
     * @throws  Exception
     *          API 호출 중 오류가 발생할 경우 예외를 던진다.
     */
    String fetchRawWeatherData(String baseDate, String baseTime, String nx, String ny) throws Exception;
}
