package com.bang9634.provider.parser;

import com.bang9634.model.FcstData;
import com.bang9634.util.reader.FcstDataReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherDataParser {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 
     * 
     * @param rawJson
     * @return
     * @throws Exception
     */
    public FcstData parse(String rawJson) throws Exception {
        try {
            /** 
             * XML로 API응답이 왔을 경우,
             * SERVICE_KEY_IS_NOT_REGISTERED_ERROR 메세지가 포함되어있으면 예외를 던지고 인증키 에러 메세지를 출력한다.
             * 그 외의 경우는 JSON 데이터 타입이 아니므로, 예외를 던지고 XML 에러 응답을 출력한다.
             * 
             * TODO: basetime이 아직 예보가 없는 경우와 같이 NODATA 에러도 XML로 옴. 예외처리 추가적으로 필요
             */
            if (rawJson.trim().startsWith("<")) {
                if (rawJson.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                    throw new IllegalArgumentException("API 호출 실패 : 인증키가 등록되지 않았거나 잘못되었습니다.");
                }
                else {
                    throw new IllegalArgumentException("API 호출 실패 : XML 에러 응답\n" + rawJson);
                }
            }

            /** 
             * JSON로 API 응답이 왔을 경우.
             * resultCode와 resultMsg를 확인하고, 정상 메세지인 "00"이 아니라면 예외를 던지고 내용을 출력한다.
             */
            JsonNode root = mapper.readTree(rawJson);
            String resultCode = root.path("response").path("header").path("resultCode").asText();
            String resultMsg = root.path("response").path("header").path("resultMsg").asText();
            if (!"00".equals(resultCode)) {
                throw new IllegalArgumentException("API 호출 실패: " + resultMsg + " (resultCode=" + resultCode + ")");
            }

            FcstData fcstData = FcstDataReader.getVilageFcstData(FcstDataReader.parseVilageFcstJsonData(rawJson));

            
            return fcstData;
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
