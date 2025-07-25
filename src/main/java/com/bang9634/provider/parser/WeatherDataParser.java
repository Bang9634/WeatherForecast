package com.bang9634.provider.parser;

import com.bang9634.model.FcstData;
import com.bang9634.model.Item;
import com.bang9634.util.mapper.FcstCodeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 기상청 API로부터 받은 날씨 데이터를 파싱하는 클래스.
 * <p>
 * 이 클래스는 JSON 형태의 날씨 데이터를 파싱하여 FcstData 객체로 변환한다.
 * 
 * @author bangdeokjae
 */
public class WeatherDataParser {
    /** 
     * Jackson ObjectMapper를 사용하여 JSON 데이터를 파싱한다.
     * <p>
     * 이 객체는 JSON 문자열을 Java 객체로 변환하는 데 사용된다.
     * <p>
     * ObjectMapper는 JSON 데이터를 Java 객체로 변환하는 데 필요한 다양한 기능을 제공
     * 예를 들어, JSON 문자열을 Item 객체의 리스트로 변환하거나, FcstData 객체로 변환하는 데 사용된다.
     * 또한, JSON 파싱 시 알 수 없는 속성이 있을 경우 예외를 발생시키지 않도록 설정되어 있다.
     * <p>
     * ObjectMapper는 JSON 데이터를 Java 객체로 변환하는 데 필요한 다양한 기능을 제공한다.
     * 예를 들어, JSON 문자열을 Item 객체의 리스트로 변환하거나, FcstData 객체로 변환하는 데 사용된다.
     * 또한, JSON 파싱 시 알 수 없는 속성이 있을 경우 예외를 발생시키지 않도록 설정되어 있다.
     * 이 설정은 API 응답의 형식이 변경되더라도 유연하게 대처할 수 있도록 도와준다.
     * <p>
     * 이 클래스는 WeatherDataParser를 사용하여 JSON 데이터를 파싱하고, FcstData 객체를 생성하는 역할을 한다.
    */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * WeatherDataParser 생성자.
     * <p>
     * ObjectMapper의 설정을 초기화한다.
     * 알 수 없는 속성이 있을 경우 예외를 발생시키지 않도록 설정한다.
     */
    public WeatherDataParser() {
        /** Jackson 라이브러리를 사용하여 JSON 데이터를 파싱하는 ObjectMapper 객체를 생성  
         * <p>
         * 이 객체는 JSON 문자열을 Java 객체로 변환하는 데 사용된다.
         * <p>
         * ObjectMapper는 JSON 데이터를 Java 객체로 변환하는 데 필요한 다양한 기능을 제공
         * 예를 들어, JSON 문자열을 Item 객체의 리스트로 변환하거나, FcstData 객체로 변환하는 데 사용된다.
         * 또한, JSON 파싱 시 알 수 없는 속성이 있을 경우 예외를 발생시키지 않도록 설정되어 있다.
         * 이 설정은 API 응답의 형식이 변경되더라도 유연하게 대처할 수 있도록 도와준다.
         */
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * JSON 형태의 날씨 데이터를 파싱하여 FcstData 객체로 변환한다.
     * <p>
     * 이 메서드는 JSON 문자열을 받아서, 해당 문자열을 파싱하고 필요한 정보를 추출하여 FcstData 객체를 생성한다.
     * 
     * @param   rawJson
     *          JSON 형태의 날씨 데이터 문자열
     * @return  파싱된 FcstData 객체
     * @throws  Exception
     *          JSON 파싱 중 오류가 발생할 경우 예외를 던진다.
     */
    public FcstData parse(String rawJson) throws Exception {
        try {
            JsonNode root = mapper.readTree(rawJson);
            /**
             * API 호출 결과의 상태 코드를 확인한다.
             * <p>
             * 상태 코드가 "00"이 아니면 API 호출이 실패한 것으로 간주하고,
             * 예외를 던진다.
             */
            String resultCode = root.path("response").path("header").path("resultCode").asText();
            String resultMsg = root.path("response").path("header").path("resultMsg").asText();
            if (!"00".equals(resultCode)) {
                throw new IllegalArgumentException("API 호출 실패: " + resultMsg + " (resultCode=" + resultCode + ")");
            }

            /**
             * JSON 데이터에서 "items" 노드를 찾아서, 그 안에 있는 "item" 노드를 파싱한다.
             * <p>
             * "item" 노드는 날씨 정보를 담고 있는 배열로, 각 요소는 Item 객체로 변환된다.
             * 이 메서드는 Item 객체의 리스트를 반환한다.
             */
            List<Item> items = parseItem(root);

            /**
             * Item 객체의 리스트를 FcstData 객체로 변환한다.
             */
            return createFcstDataFromItems(items);
        } catch (Exception e) {
            throw new Exception("데이터파싱 중 오류 발생", e);
        }
    }

    /**
     * JSON 데이터에서 "items" 노드를 찾아서, 그 안에 있는 "item" 노드를 파싱한다.
     * <p>
     * "item" 노드는 날씨 정보를 담고 있는 배열로, 각 요소는 Item 객체로 변환된다.
     * 이 메서드는 Item 객체의 리스트를 반환한다.
     * 
     * @param   root
     *          JSON 데이터의 루트 노드
     * @return  Item 객체의 리스트
     * @throws  JsonProcessingException
     *          JSON 파싱 중 오류가 발생할 경우 예외를 던진다.
     */
    private List<Item> parseItem(JsonNode root) throws JsonProcessingException {
        JsonNode itemNode = root
            .path("response")
            .path("body")
            .path("items")
            .path("item");
        return mapper.readValue(
            itemNode.toString(),
            TypeFactory.defaultInstance().constructCollectionType(List.class, Item.class)
        );
    }

    /**
     * Item 객체의 리스트를 FcstData 객체로 변환한다.
     * <p>
     * 이 메서드는 Item 객체의 리스트를 받아서, 각 Item 객체의 정보를 FcstData 객체에 매핑한다.
     * 
     * @param   items
     *          Item 객체의 리스트
     * @return  FcstData 객체
     */
    private FcstData createFcstDataFromItems(List<Item> items) {
        Map<String, String> data = new LinkedHashMap<>();
        for (Item item : items) {
            data.put(
                FcstCodeMapper.CATEGORY_CODE_MAP.get(item.getCategory()), 
                FcstCodeMapper.getSubMappingTableValue(item)
            );
        }
        return new FcstData(data);
    }
}
