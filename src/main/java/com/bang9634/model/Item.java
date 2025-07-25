package com.bang9634.model;

/**
 * 기상청 API로 요청한 단기예보 JSON 데이터에서 필요한 정보만 추출하여 저장, 전달을 하기위한 클래스.
 * <p>
 * 전달받은 item의 category 코드에 따라 fcstValue 값의 의미가 변화한다. 
 * 이 클래스는 단기예보 데이터를 저장하고, 각 카테고리 코드에 해당하는 값을 저장하는 역할을 한다.
 * <p>
 * 단기예보 category 코드값 정보 <p>
 * POP  : 강수확률(%) <p>
 * PTY  : 강수형태(코드값) <p>
 * PCP  : 1시간 강수량(범주:1mm) <p>
 * REH  : 습도(%) <p>
 * SNO  : 1시간 신적설(범주:1cm) <p>
 * SKY  : 하늘상태(코드값) <p>
 * TMP  : 1시간 기온(°C) <p>
 * TMN  : 일 최저기온(°C) <p>
 * TMX  : 일 최고기온(°C) <p>
 * UUU  : 풍속(동서성분)(m/s) (동:+, 서:-) <p>
 * VVV  : 풍속(남북성분)(m/s) (북:+, 남:-) <p>
 * WAV  : 파고(M) <p>
 * VEC  : 풍향(deg) <p>
 * WSD  : 풍속(m/s) <p>
 */
public class Item {
    private String fcstDate;    // 예보일자
    private String fcstTime;    // 예보시각
    private String category;    // 카테고리
    private String fcstValue;   // 값


    /**
     * 기본 생성자. 
     * <p>
     * 이 생성자는 Item 객체를 생성할 때, 초기값을 설정하지 않고 빈 상태로 만든다.
     * <p>
     * 이 생성자는 주로 Jackson 라이브러리와 같은 JSON 파싱 라이브러리에서 사용된다.
     * JSON 데이터를 객체로 변환할 때, 기본 생성자가 필요하기 때문이다.
     * <p>
     * Jackson 라이브러리는 JSON 데이터를 객체로 변환할 때, 기본 생성자를 사용하여 객체를 생성한다.
     * 이후, JSON 데이터의 필드 값을 해당 객체의 필드에 매핑한다.
     * 따라서, 기본 생성자는 Jackson 라이브러리와의 호환성을 위해 필요하다.
     */
    public Item() {
    }
    
    /**
     * 매개변수로 받은 값을 사용하여 Item 객체를 초기화하는 생성자.
     * <p>
     * 이 생성자는 외부에서 fcstDate, fcstTime, category, fcstValue 값을 받아 Item 객체를 초기화할 때 사용된다.
     * 
     * @param   fcstDate
     *          예보일자
     * @param   fcstTime
     *          예보시각
     * @param   category
     *          카테고리 코드
     * @param   fcstValue
     *          해당 카테고리에 대한 값
     */
    public Item(String fcstDate, String fcstTime, String category, String fcstValue) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.category = category;
        this.fcstValue = fcstValue;
    }

    /**
     * 예보일자를 반환하는 메서드.
     * 
     * @return  예보일자
     */
    public String getFcstDate() {
        return fcstDate;
    }

    /**
     * 예보시각을 반환하는 메서드.
     * 
     * @return  예보시각
     */
    public String getFcstTime() {
        return fcstTime;
    }

    /**
     * 카테고리 코드를 반환하는 메서드.
     * 
     * @return  카테고리 코드
     */
    public String getCategory() {
        return category;
    }

    /**
     * 해당 카테고리에 대한 값을 반환하는 메서드.
     * 
     * @return  해당 카테고리에 대한 값
     */
    public String getFcstValue() {
        return fcstValue;
    }
}
