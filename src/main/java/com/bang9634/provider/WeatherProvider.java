package com.bang9634.provider;

/**
 * 날씨 데이터를 제공하는 모든 외부 API 제공자의 인터페이스
 */
public interface WeatherProvider {
    
    /**
     * 외부 API를 호출하여 raw 날씨 데이터를 문자열 형태로 가져온다.
     * 
     * @param   baseDate
     *          발표일자
     *  
     * @param   baseTime
     *          발표시각
     * 
     * @param   nx
     *          예보지점 x좌표
     * 
     * @param   ny
     *          예보지점 y좌표
     * 
     * @return  raw 날씨 데이터를 문자열 형태로 반환한다.
     */
    String fetchRawWeatherData(String baseDate, String baseTime, String nx, String ny) throws Exception;
}
