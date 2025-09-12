package com.bang9634.provider.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bang9634.model.FcstData;
import com.bang9634.util.constants.WeatherConstants;

import java.util.Map;
import java.util.LinkedHashMap;

/** WeatherDataParserTest의 퍼블릭 메서드 테스트 클래스 */
public class WeatherDataParserTest {
    /**
     * WeatherDataParser의 parse 메서드가 JSON 데이터를 정상적으로 파싱했을 때
     * 반환해야될 데이터가 저장된 FcstData 인스턴스를 반환하는지 테스트한다.
     * @throws  Exception
     */
    @Test
    @DisplayName("메서드가 성공적으로 호출되었다면, 데이터가 담긴 FcstData 인스턴스를 반환해야한다.")
    void parse_shouldReturnCorrectFcstData_forValidJson() throws Exception {
        /** 파싱할 JSON 문자열 */
        String exampleRawJson = """
            {"response":{"header":{"resultCode":"00","resultMsg":"NORMAL_SERVICE"},"body":{"dataType":"JSON","items":{"item":[{"baseDate":"20250909","baseTime":"0500","category":"TMP","fcstDate":"20250909","fcstTime":"0600","fcstValue":"22","nx":60,"ny":127}]},"pageNo":1,"numOfRows":12,"totalCount":907}}}
            """;

        /** exampleRawJson을 파싱했을 때 반환해야하는 데이터를 expected 인스턴스에 저장한다. */
        Map<String, String> data = new LinkedHashMap<>();
        data.put(WeatherConstants.LABEL_FCST_DATE, "20250909");
        data.put(WeatherConstants.LABEL_FCST_TIME, "0600");
        data.put("1시간 기온(°C)", "22");
        FcstData expected = new FcstData(data);

        /** exampleRawJson을 파싱한 결과가 예상한 데이터를 반환하는지 검사한다. */
        WeatherDataParser parser = new WeatherDataParser(); 
        assertEquals(expected, parser.parse(exampleRawJson));
    }
}