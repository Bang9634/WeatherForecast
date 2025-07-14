package com.bang9634.util;

/**
 * 날씨 관련 상수를 모아놓은 클래스
 */
public class WeatherConstants {
    public static final String LABEL_FCST_DATE = "예보일자";
    public static final String LABEL_FCST_TIME = "예보시각";

    /** 
     * 기기의 현재 LocalTime을 상수로 정의한다. 프로그램 실행 시 정의되어 고정된다.
     * 
     * TODO: 프로그램 실행 시간으로 고정되기에 추후 수정
     */
    public static final String LABEL_BASE_DATE = java.time.LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

    /** 
     * TODO: 추후 baseTime을 선택할 수 있는 기능 추후 추가
     */
    public static final String LABEL_BASE_TIME = "0500";
}
