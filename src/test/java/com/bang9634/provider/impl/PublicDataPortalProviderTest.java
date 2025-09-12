package com.bang9634.provider.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** JUnit5에 Mockito 기능을 사용하는 테스트 클래스라는 것을 알려준다. */
@ExtendWith(MockitoExtension.class)
public class PublicDataPortalProviderTest {

    /** Mockito가 가짜 HttpClient 객체를 만들어 넣어준다. */
    @Mock
    private HttpClient mockHttpClient;

    /** Mockito가 가짜 HttpResponse 객체를 만들어 넣어준다. */
    @Mock
    private HttpResponse<String> mockHttpResponse;

    /**
     * fetchRawWeatherData 메서드를 호출했을 때, API가 성공적으로 반환한 경우,
     * 해당 JSON문자열을 그대로 반환하는 지 테스트한다.
     * @throws  Exception
     */
    @Test
    @DisplayName("API가 성공적으로 JSON을 반환하면, 해당 JSON 문자열을 그대로 반환해야 한다.") /** 테스트 이름 */
    @SuppressWarnings("unchecked") /** Type safety 경고 무시 */
    void fetchRawWeatherData_shouldReturnJsonString_whenApiSucceeds() throws Exception {

        /** 가짜 응답으로 받고 싶은 JSON 문자열을 준비한다. */
        String expectedJson = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL_SERVICE\"}}}";

        /** 가짜 HttpResponse가 body() 메서드를 호출하면, 위에서 만든 JSON을 반환하라고 설정한다. */
        when(mockHttpResponse.body()).thenReturn(expectedJson);

        /** 가짜 HttpClient가 send() 메서드를 (어떤 요청이든) 호출하면, 위에서 만든 가짜 HttpResponse를 반환하라고 설정한다. */
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockHttpResponse);

        /** 테스트할 대상 객체를 생성한다. 이때, 생성자에 우리가 만든 가짜 HttpClient를 주입한다. */
        PublicDataPortalProvider provider = new PublicDataPortalProvider("fakeServiceKey", mockHttpClient);

        /** when - 실제 테스트 실행 단계 */
        String actualResponse = provider.fetchRawWeatherData("20250725", "0500", "60", "127");

        /** 
         * then - 결과 검증 단계
         * 실제 반환된 값이 우리가 예상한 JSON과 같은지 확인한다.
        */
        assertEquals(expectedJson, actualResponse);
 
    }

    /** 
     * fetchRawWeatherData를 호출했을 때, API 인증키 오류로 인해 XML 문자열을 반환하면, 
     * IllegalArgumentException을 던지는지 테스트한다.
     * @throws  IllegalArgumentException
     */
    @Test
    @DisplayName("API가 인증키 오류 XML을 반환하면, IllegalArgumentException을 던져야 한다.")
    @SuppressWarnings("unchecked") /** Type safety 경고 무시 */
    void  fetchRawWeatherData_shouldThrowException_whenServiceKeyIsInvalid() throws Exception {
        /** 가짜 응답으로 받을 XML 오류 문자열 */
        String errorXml = "<OpenAPI_ServiceResponse>\n" +
                        "        <cmmMsgHeader>\n" +
                        "                <errMsg>SERVICE ERROR</errMsg>\n" +
                        "                <returnAuthMsg>SERVICE_KEY_IS_NOT_REGISTERED_ERROR</returnAuthMsg>\n" +
                        "                <returnReasonCode>30</returnReasonCode>\n" +
                        "        </cmmMsgHeader>";
        /** HttpResponse가 body 메서드를 호출하면 errorXml을 반환하도록 설정한다. */
        when(mockHttpResponse.body()).thenReturn(errorXml);

        /** 가짜 HttpClient가 send() 메서드를 (어떤 요청이든) 호출하면, 위에서 만든 가짜 HttpResponse를 반환하라고 설정한다. */
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockHttpResponse);

        /** 테스트할 대상 객체를 생성, 가짜 클라이언트 주입 및 유효하지 않은 서비스 키를 생성자 매개변수로 넘긴다. */
        PublicDataPortalProvider provider = new PublicDataPortalProvider("invalid_key", mockHttpClient);

        /**
         * when & then - 실행 및 검증
         * provider.fetchRawWeatherData(...)를 실행했을 때,
         * IllegalArgumentException 타입의 예외가 발생하는 것을 기대하고 검증한다.
         */
        assertThrows(IllegalArgumentException.class, () -> {
            provider.fetchRawWeatherData("20250725", "0500", "60", "127");
        });
    }

    /**
     * fetchRawWeatherData를 호출했을 때, API가 NODATA 에러 XML 문자열읇 반환하면,
     * IllegalStateException을 던지는 테스트한다.
     * @throws  IllegalStateException
     */
    @Test
    @DisplayName("API가 NODATA 에러 XML을 반환하면, IllegalStateException을 던져야 한다.")
    @SuppressWarnings("unchecked")
    void fetchRawWeatherData_shouldThrowException_whenResponseIsNoDataError() throws Exception {
        /** 가짜응답으로 받을 NODATA 에러 XML 문자열 */
        String noDataErrorXml = """
                <response>
                    <header>
                        <resultCode>03</resultCode>
                        <resultMsg>NO_DATA</resultMsg>
                    </header>
                </response>
                """;

        /** HttpResponse가 body 메서드를 호출하면 noDataErrorXml을 반환하도록 설정한다. */
        when(mockHttpResponse.body()).thenReturn(noDataErrorXml);

        /** 가짜 HttpClient가 send() 메서드를 (어떤 요청이든) 호출하면, 위에서 만든 가짜 HttpResponse를 반환하라고 설정한다. */
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockHttpResponse);

        /** 테스트할 대상 객체를 생성, 가짜 클라이언트를 주입한다. */
        PublicDataPortalProvider provider = new PublicDataPortalProvider("any_key", mockHttpClient);

        /** fetchRawWeatherData 메서드를 호출하고 IllegalStateException을 던지는지 확인한다. */
        assertThrows(IllegalStateException.class, () -> {
            provider.fetchRawWeatherData("20250728", "0500", "60", "120");
        });
    }

}
