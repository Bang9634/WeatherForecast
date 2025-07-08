package com.bang9634;

/**
 * 기상청 API로 요청한 단기예보 JSON 데이터에서 필요한 정보만 추출하여 저장, 전달을 하기위한 클래스. <p>
 * 
 * 전달받은 item의 category 코드에 따라 fcstValue 값의 의미가 변화한다. <p>
 * 아래에 category 코드값에 따라 fcstValue 값의 의미를 작성하였다. <p>
 * 
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
    public String fcstDate; // 예보일자
    public String fcstTime; // 예보시각
    public String category; // 카테고리
    public String fcstValue; // 값

    /** 
     * 매개변수 4개를 받는 Item 생성자 <p>
     * 
     * 예보일자, 예보시각, category, fcstValue 순으로 매개변수를 전달받는다.
     */
    public Item(String fcstDate, String fcstTime, String category, String fcstValue) {
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.category = category;
        this.fcstValue = fcstValue;
    }
}
