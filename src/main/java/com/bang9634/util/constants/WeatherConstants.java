package com.bang9634.util.constants;

/**
 * WeatherConstants 클래스는 날씨 관련 상수들을 정의한다.
 * <p>
 * 이 클래스는 기상예보 데이터의 날짜, 시간 등의 라벨을 상수로 정의하여,
 * 코드 전반에서 일관되게 사용할 수 있도록 한다.
 * 
 * @author bangdeokjae
 */
public class WeatherConstants {
    public static final String LABEL_FCST_DATE = "예보일자";
    public static final String LABEL_FCST_TIME = "예보시각";

    /** 기기의 현재 LocalTime을 상수로 정의한다. 프로그램 실행 시 정의되어 고정된다. */
    public static final String LABEL_BASE_DATE = java.time.LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

    /** 
     * TODO: 추후 baseTime을 선택할 수 있는 기능 추후 추가
     */
    public static final String LABEL_BASE_TIME = "0500";
}
