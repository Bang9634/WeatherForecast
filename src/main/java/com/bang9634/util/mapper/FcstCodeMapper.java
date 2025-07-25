package com.bang9634.util.mapper;

import java.util.Map; // Map을 사용

import com.bang9634.model.Item;

/**
 * FcstCodeMapper 클래스는 기상예보 데이터의 카테고리 코드와 해당 값을 매핑하는 상수들을 정의한다.
 * <p>
 * 이 클래스는 기상예보 데이터의 카테고리 코드와 해당 값을 일관되게 관리하기 위해 사용된다.
 * 
 * TODO: JSON과 같은 외부파일로 만들어 관리. 상수들도 properties 파일로 관리하기.
 * 
 * @author bangdeokjae
 */
public class FcstCodeMapper {
    /**
     * 기상예보 데이터의 카테고리 코드와 해당 값을 매핑하는 테이블.
     * <p>
     * 이 테이블은 기상예보 데이터의 카테고리 코드와 해당 값을 일관되게 관리하기 위해 사용된다.
     */
    public static final Map<String, String> CATEGORY_CODE_MAP = Map.ofEntries(
        Map.entry("POP", "강수확률(%)"),
        Map.entry("PTY", "강수형태"), // 값의 매핑 테이블 존재
        Map.entry("PCP", "1시간 강수량(범주:1mm)"),
        Map.entry("REH", "습도(%)"),
        Map.entry("SNO", "1시간 신적설(범주:1cm)"),
        Map.entry("SKY", "하늘상태"), // 값의 매핑 테이블 존재
        Map.entry("TMP", "1시간 기온(°C)"),
        Map.entry("TMN", "일 최저기온(°C)"),
        Map.entry("TMX", "일 최고기온(°C)"),
        Map.entry("UUU", "풍속(동서성분)(m/s)"),
        Map.entry("VVV", "풍속(남북성분)(m/s)"),
        Map.entry("WAV", "파고(M)"),
        Map.entry("VEC", "풍향(deg)"),
        Map.entry("WSD", "풍속(m/s)")
    );

    /**
     * PTY(강수형태) 코드값 매핑 테이블
     * <p>
     * 이 테이블은 PTY 카테고리 코드에 해당하는 값을 매핑한다.
     */
    public static final Map<Integer, String> PTY_CODE_MAP = Map.ofEntries(
        Map.entry(0, "없음"),
        Map.entry(1, "비"),
        Map.entry(2, "비/눈"),
        Map.entry(3, "눈"),
        Map.entry(4, "소나기")
    );

    /** 
     * SKY(하늘상태) 코드값 매핑 테이블
     * <p>
     * 이 테이블은 SKY 카테고리 코드에 해당하는 값을 매핑한다.
     */
    public static final Map<Integer, String> SKY_CODE_MAP = Map.ofEntries(
        Map.entry(1, "맑음"),
        Map.entry(3, "구름많음"),
        Map.entry(4, "흐림")
    );

    /**
     * 하위 매핑 테이블 값을 반환한다.
     * <p>
     * PTY(강수형태)와 SKY(하늘상태) 카테고리 코드에 대해서는
     * 해당하는 매핑 테이블에서 값을 찾아 반환한다.
     * 그 외의 카테고리 코드에 대해서는 fcstValue 값을 그대로 반환한다.
     * 
     * @param   item
     *          기상예보가 담긴 Item 클래스 타입 매개변수를 넘긴다.
     * @return  하위 매핑 테이블의 값 또는 fcstValue 값
     *          <p>
     *          item의 category가 PTY 또는 SKY인 경우, 해당하는 매핑 테이블의 값을 반환한다.
     *          <p>
     *          item의 category가 PTY 또는 SKY가 아닌 경우, fcstValue 값을 그대로 반환한다. 
     */
    public static String getSubMappingTableValue(Item item) {
        String subMappingTableCode = item.getCategory();
        if (subMappingTableCode.equals("PTY")) {
            return PTY_CODE_MAP.get(Integer.parseInt(item.getFcstValue()));
        }
        if (subMappingTableCode.equals("SKY")) {
            return SKY_CODE_MAP.get(Integer.parseInt(item.getFcstValue()));
        } 
        return item.getFcstValue();
    }
}