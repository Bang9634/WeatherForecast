package com.bang9634.util;

import com.bang9634.Item; // 기상예보 데이터 전달을 위한 클래스
import com.bang9634.FcstData; // 기상예보 데이터를 저장하기 위한 클래스
import com.fasterxml.jackson.databind.JsonNode; // JSON 파싱
import com.fasterxml.jackson.databind.ObjectMapper; // JSON 파싱
import java.util.List; // 기상예보 데이터를 저장
import java.util.ArrayList; // 기상예보 데이터를 저장

/**
 * 기상예보 데이터를 처리를 담당하는 클래스. <p>
 * 
 * JSON데이터를 파싱하거나 데이터를 정리하는 함수들을 포함하고 있다. <p>
 * 파싱 관련 함수를 구현하기 위해 외부 라이브러리(Jackson)을 의존한다. <p>
 */
public class FcstDataReader {

    /** 
     * 기상예보 JSON 데이터를 Item 클래스 리스트로 파싱하여 반환한다. <p>
     * 
     * 매개변수로 GET요청한 데이터의 Response를 전달하고, Jackson 라이브러리의 <p>
     * 함수를 이용하여 fcstDate, fcstTime, category, fcstValue 데이터만 추출한다. <p>
     * 추출한 데이터를 저장한 Item클래스를 List에 추가한다. <p>
     * Response로 전달받은 모든 데이터를 추가한 List를 반환한다. <p>
     * 
     * @param   vilageFcstJsonData
     *          서버로부터 전달받은 Response
     * 
     * @return  Response에서 fcstDate, fcstTime, category, fcstValue <p>
     *          값을 추출하여 Item 클래스에 저장한 리스트를 반환한다.
     */
    public static List<Item> parseVilageFcstJsonData(String vilageFcstJsonData) {
        try {
            /** 기상예보 데이터를 담을 List<Item> 선언 및 할당한다. */
            List<Item> fcstItems = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(vilageFcstJsonData); 
            
            /** items에 response/body/items/item 경로의 모든 데이터를 저장한다. */
            JsonNode items = root.path("response").path("body").path("items").path("item");
            
            /** 
             * items에 들어있는 각 item 블록의 fcstData, fcstTime, category, <p>
             * fcstValue 데이터를 생성자 매개변수로 Item클래스를 만들어 <p>
             * fcstData 리스트에 추가한다. <p>
             * items의 모든 item 데이터가 추가될 때까지 반복한다.
             */
            for (JsonNode item : items) {
                String fcstDate = item.path("fcstDate").asText();
                String fcstTime = item.path("fcstTime").asText();
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();
                fcstItems.add(new Item(fcstDate, fcstTime, category, fcstValue));
            }
            
            /** fcstData 반환 */
            return fcstItems;
        } catch (Exception e) {
            System.out.println("JSON 데이터 파싱 실패");

            /** 예외 상황 발생시 빈 리스트 반환 */
            return new ArrayList<>();
        }
    }

    /**
     * 파싱된 기상예보 데이터 리스트들을 정리하여 저장한 FcstData 클래스를 반환한다.
     * 
     * parseVilageFcstJsonData() 메서드에 의해 List<Item> 형태로 분석된 데이터를 매개변수로 넘기면
     * Item들의 속성에 저장되어있는 코드값과 수치 기상정보들을 FcstCodeMapper 클래스에 정의되어있는 매핑테이블에
     * 해당하는 정보와 값으로 변경시켜 FcstData 클래스의 Map<String, String> 타입 속성에 저장한다.
     * 
     * @param   fcstItems
     *          parseVilageFcstJsonData() 메서드의 반환 값을 매개변수로 넘긴다.
     * 
     * @return  각각의 기상정보가 저장되어있는 FcstData 클래스 객체가 반환된다.
     */
    public static FcstData getVilageFcstData(List<Item> fcstItems) {
        FcstData fcstData = new FcstData();
        fcstData.data.put("예보일자", fcstItems.get(0).fcstDate);
        fcstData.data.put("예보시각", fcstItems.get(0).fcstTime);
        for (Item item : fcstItems) {
            /** 
             * FcstCodeMapper.CATEGORY_CODE_MAP 매핑 테이블에서 item.category 값과 일치하는
             * 키를 찾아 키 값을 fcstDataCategory에 저장한다.
             */
            String fcstDataCategory = FcstCodeMapper.CATEGORY_CODE_MAP.get(item.category);

            /** 키에 해당하는 값으로 변환시켜 fcstData put한다. */
            if (FcstCodeMapper.getSubMappingTableValue(item) != null) {
                fcstData.data.put(fcstDataCategory, FcstCodeMapper.getSubMappingTableValue(item));
            }
            else {
                fcstData.data.put(fcstDataCategory, item.fcstValue);
            }
        }
        return fcstData;
    }
}
