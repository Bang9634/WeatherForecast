package com.bang9634.model;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 기상예보 데이터를 저장하기 위한 가변(mutable) 클래스. <p>
 */
public class FcstData {
    /** 
     * 데이터를 저장할 가변(mutable) LinkedHashMap <p>
     * 
     * 키에는 FcstDataMapper.CATEGORY_CODE_MAP 매핑 테이블의 값이 저장되며 
     * 값에는 각 category에 해당하는 fcstValue 값이 저장된다. <p>
     * 
     * FcstDataMapper.CATEGORY_CODE_MAP 매핑 테이블에서 PTY, SKY처럼 값이 코드인 경우에 
     * 해당 카테고리의 매핑 테이블에서 코드에 해당하는 값을 data의 값에 저장한다.
     * */
    public Map<String, String> data;

    public FcstData() {
        this.data = new LinkedHashMap<>();
    }

    public FcstData(Map<String, String> data) {
        this.data = data;
    }
}
