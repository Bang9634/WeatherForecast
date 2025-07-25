package com.bang9634.provider.impl;

import java.net.http.HttpClient; // 클라이언트 클래스 객체 생성
import java.net.http.HttpRequest; // 서버 GET요청 전송
import java.net.http.HttpResponse; // 서버로부터 응답

import com.bang9634.provider.WeatherProvider;

import java.net.URI; // GET요청문 Build

/**
 * PublicDataPortalProvider 클래스는 기상청의 공공 데이터 포털 API를 이용하여
 * 단기예보 데이터를 요청하고 응답받는 기능을 제공하는 클래스이다.
 * <p>
 * 이 클래스는 WeatherProvider 인터페이스를 구현하며, 인증키를 사용하여 API에 접근한다.
 * <p>
 * 주요 기능:
 * <ul>
 *   <li>{@link #fetchRawWeatherData(String, String, String, String)}
 *       - 단기예보 데이터를 요청하고 응답받는다.</li>
 * </ul>
 * <p>
 * 이 클래스는 기상청 API의 단기예보 조회 서비스를 사용하여, 특정 날짜와 시간, 좌표에 대한 기상 정보를 요청한다.
 * <p>
 * 사용 예시:
 * <pre>
 *     PublicDataPortalProvider provider = new PublicDataPortalProvider("your_service_key");
 *     String weatherData = provider.fetchRawWeatherData("20231001", "0200", "60", "127");
 *     System.out.println(weatherData);
 * </pre>
 * <p>
 * 이 클래스는 기상청 API의 응답을 JSON 형식으로 처리하며,
 * 응답받은 데이터를 파싱하여 필요한 정보를 추출할 수 있다.
 * <p>
 * @see WeatherProvider
 * @see HttpClient
 * @see HttpRequest
 * @see HttpResponse
 * @see URI
 * @author bangdeokjae   
 */
public class PublicDataPortalProvider implements WeatherProvider {
    /** 인증키 값 */
    private final String serviceKey;

    /** 
     * PublicDataPortalProvider 생성자 
     * <p>
     * 인자로 인증 키 값을 전달받아 해당 클래스의 속성 serviceKey에 저장한다.
     * 인증키는 기상청 API에 접근하기 위한 필수 요소로,
     * 기상청의 공공 데이터 포털에서 발급받아야 한다.
     * <p>
     * @param   serviceKey
     *          전달할 인증키 값
     */
    public PublicDataPortalProvider(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * 단기예보조회 API를 이용하여 발표 일자 및 시각, 좌표를 전달해 해당하는 기상 예보를 응답받아 반환한다.
     * <p>
     * 매개변수로 전달받은 정보들을 이용해 기상청 서버에 단기예보 데이터를 GET요청하기 위해
     * GET요청문을 Build하고 서버에 전송한다.
     * 응답받은 Response에서 단기예보 데이터가 담겨있는 body부분만 추출하여 반환한다.
     * 기상청41_단기예보 조회서비스 엑셀 문서를 통해 국내 지역의 좌표 값 확인 가능 (북한 및 국외 불가능)
     * <p>
     * @param   baseDate
     *          발표일자 (yyyyMMdd)
     * @param   baseTime
     *          발표시각 (hhMM)
     *          0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
     * @param   nx
     *          예보지점 x 좌표
     * @param   ny
     *          예보지점 y 좌표
     * @return  인자로 전달한 날짜와 시각, 좌표에 해당하는 json 타입의 기상 정보
     *          응답 메세지 명세
     *          numOfRows = 한 페이지 결과 수
     *          pageNo = 페이지 번호
     *          totalCount = 데이터 총 개수
     *          resultCode = 응답메세지 코드
     *          resultMsg = 응답메세지 내용
     *          dataType = 데이터 타입
     *          baseData = 발표날짜
     *          baseTime = 발표시각
     *          fcstDate = 예보일자
     *          fcstTime = 예보시각
     *          category = 자료구분문자
     *          fcstValue = 예보 값
     *          nx = 예보지점 X 좌표
     *          ny = 예보지점 Y 좌표
     * @throws Exception  API 호출 중 오류가 발생하면 예외를 던진다.
     * <p>
     */
    @Override
    public String fetchRawWeatherData(String baseDate, String baseTime, String nx, String ny) throws Exception {

        /** 전달할 정보를 포함한 요청메세지(url)를 선언한다. */
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // 단기예보조희 API
                   + "?serviceKey=" + serviceKey // 인증키
                   + "&pageNo=1&" // 페이지 번호
                   + "numOfRows=12" // 한 페이지 결과 수
                   + "&dataType=JSON"// 요청자료형식(XML/JSON)
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
        String responseBody = response.body();

        System.out.println("API 응답 : " + responseBody);

        /** 
         * XML로 API응답이 왔을 경우,
         * SERVICE_KEY_IS_NOT_REGISTERED_ERROR 메세지가 포함되어있으면 예외를 던지고 인증키 에러 메세지를 출력한다.
         * 그 외의 경우는 JSON 데이터 타입이 아니므로, 예외를 던지고 XML 에러 응답을 출력한다.
         * 
         * TODO: basetime이 아직 예보가 없는 경우와 같이 NODATA 에러도 XML로 옴. 예외처리 추가적으로 필요
         */
        if (responseBody.trim().startsWith("<")) {
            if (responseBody.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                throw new IllegalArgumentException("API 호출 실패 : 인증키가 등록되지 않았거나 잘못되었습니다.");
            }
            else {
                throw new IllegalArgumentException("API 호출 실패 : XML 에러 응답\n" + responseBody);
            }
        }
        /** response에 단기예보 데이터를 담고 있는 body부분만 반환한다. */
        return responseBody;
    }
}