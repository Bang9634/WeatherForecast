package com.bang9634.util.mapper;

import java.util.Map; // Map을 사용

import com.bang9634.model.Item;

/** 
 * 기상예보 코드값의 의미를 매핑한 테이블을 가진 클래스. <p>
 * 
 * 키에는 각 코드와 값에는 의미가 매핑되어있으며, 모든 값들은 불변(immutable)이다. <p>
 * get() 메서드를 호출하여 각 코드 값과 매핑된 데이터를 읽을 수 있다. <p>
 * 
 * TODO: JSON과 같은 외부파일로 만들어 관리. 상수들도 properties 파일로 관리하기.
 */
public class FcstCodeMapper {
    /** 
     * category 코드값 매핑 테이블
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
     */
    public static final Map<Integer, String> SKY_CODE_MAP = Map.ofEntries(
        Map.entry(1, "맑음"),
        Map.entry(3, "구름많음"),
        Map.entry(4, "흐림")
    );

    /**
     * 기상예보 item의 카테고리 코드를 확인하고 값이 매핑테이블이 존재할 경우,
     * 해당하는 해당하는 매핑테이블 값을 반환한다.
     * 
     * @param   item
     *          기상예보가 담긴 Item 클래스 타입 매개변수를 넘긴다.
     *         
     * @return  item의 category의 값이 하위 매핑 테이블이 존재하면 fcstValue 값에 해당하는 키의 
     *          하위 메핑 테이블의 값을 반환하며, 존재하지 않으면 null을 반환한다.
     */
    public static String getSubMappingTableValue(Item item) {
        String subMappingTableCode = item.category;
        if (subMappingTableCode.equals("PTY")) {
            return PTY_CODE_MAP.get(Integer.parseInt(item.fcstValue));
        }
        if (subMappingTableCode.equals("SKY")) {
            return SKY_CODE_MAP.get(Integer.parseInt(item.fcstValue));
        } 
        return null;
    }
}