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

public class WeatherDataParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public WeatherDataParser() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    /**
     * 
     * 
     * @param rawJson
     * @return
     * @throws Exception
     */
    public FcstData parse(String rawJson) throws Exception {
        try {
            JsonNode root = mapper.readTree(rawJson);
            /** 
             * JSON로 API 응답이 왔을 경우.
             * resultCode와 resultMsg를 확인하고, 정상 메세지인 "00"이 아니라면 예외를 던지고 내용을 출력한다.
             */
            String resultCode = root.path("response").path("header").path("resultCode").asText();
            String resultMsg = root.path("response").path("header").path("resultMsg").asText();
            if (!"00".equals(resultCode)) {
                throw new IllegalArgumentException("API 호출 실패: " + resultMsg + " (resultCode=" + resultCode + ")");
            }

            List<Item> items = parseItem(root);
            return createFcstDataFromItems(items);
        } catch (Exception e) {
            throw new Exception("데이터파싱 중 오류 발생", e);
        }
    }

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
