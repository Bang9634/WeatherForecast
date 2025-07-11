package com.bang9634;

import java.net.http.HttpClient; // 클라이언트 클래스 객체 생성
import java.net.http.HttpRequest; // 서버 GET요청 전송
import java.net.http.HttpResponse; // 서버로부터 응답
import java.net.URI; // GET요청문 Build

/**
 * 공공 데이터 포털의 기상 API를 이용하여 서버에 기상 정보를 요청 및 전달받는 클라이언트 클래스. <p>
 * 좌표 값 및 기타 신호 코드들의 자세한 정보는 기상청41_단기예보 조회서비스 문서 참고하기 바란다. <p>
 * getWeather() 함수를 이용해 JSON타입의 단기예보 데이터를 요청할 수 있다. <p>
 */
public class WeatherApiClient {
    /** 인증키 값을 저장 */
    private final String serviceKey;

    /** 
     * WeatherApiClient의 생성자 <p>
     * 
     * 인자로 인증 키 값을 전달받아 해당 클래스의 속성 serviceKey에 저장 <p>
     * 
     * @todo    클래스 속성에 저장하지 않고
     * 
     * @param   serviceKey 
     *          전달할 인증키 값
     */
    public WeatherApiClient(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /** 
     * 단기예보조회 API를 이용하여 발표 일자 및 시각, 좌표를 전달해 해당하는 기상 예보를 응답받아 반환 <p>
     * 
     * 매개변수로 전달받은 정보들을 이용해 기상청 서버에 단기예보 데이터를 GET요청하기 위해 <p>
     * GET요청문을 Build하고 서버에 전송한다. <p>
     * 응답받은 Response에서 단기예보 데이터가 담겨있는 body부분만 추출하여 반환한다. <p>
     * 기상청41_단기예보 조회서비스 엑셀 문서를 통해 국내 지역의 좌표 값 확인 가능 (북한 및 국외 불가능) <p>
     * 
     * @param   baseDate 
     *          발표일자 (yyyyMMdd)
     * 
     * @param   baseTime
     *          발표시각 (hhMM)
     *          0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
     * 
     * @param   nx
     *          예보지점 x 좌표 <p>
     * 
     * @param   ny
     *          예보지점 y 좌표 <p>
     * 
     * @return  인자로 전달한 날짜와 시각, 좌표에 해당하는 json 타입의 기상 정보 <p>
     *          응답 메세지 명세 <p>
     *          numOfRows = 한 페이지 결과 수 <p>
     *          pageNo = 페이지 번호 <p>
     *          totalCount = 데이터 총 개수 <p>
     *          resultCode = 응답메세지 코드 <p>
     *          resultMsg = 응답메세지 내용 <p>
     *          dataType = 데이터 타입 <p>
     *          baseData = 발표날짜 <p>
     *          baseTime = 발표시각 <p>
     *          fcstDate = 예보일자 <p>
     *          fcstTime = 예보시각 <p>
     *          category = 자료구분문자 <p>
     *          fcstValue = 예보 값 <p>
     *          nx = 예보지점 X 좌표 <p>
     *          ny = 예보지점 Y 좌표 <p>
     */
    public String getWeather(String baseDate, String baseTime, String nx, String ny) throws Exception {

        /** 전달할 정보를 포함한 요청메세지(url)를 선언한다. */
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // 단기예보조희 API
                   + "?serviceKey=" + serviceKey // 인증키
                   + "&pageNo=1&numOfRows=10&dataType=JSON"// 페이지 번호, 한 페이지 결과 수, 요청자료형식(XML/JSON)
                   + "&base_date=" + baseDate // 발표일자
                   + "&base_time=" + baseTime // 발표시각
                   + "&nx=" + nx // 예보지점 X 좌표
                   + "&ny=" + ny; // 예보지점 Y 좌표

        /** 클라이언트 객체 생성한다. */
        HttpClient client = HttpClient.newHttpClient();

        /** 서버에 전송할 GET요청문 build한다. */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        /** 클라이언트 객체가 서버에 GET요청 전송 후, client.send() 메서드는 서버의 Response을 반환, reponse 지역변수에 저장한다. */
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        /** response에 단기예보 데이터를 담고 있는 body부분만 반환한다. */
        return response.body();
    }
}