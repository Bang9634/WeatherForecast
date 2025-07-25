package com.bang9634.model;

import java.util.Map;
import java.util.LinkedHashMap;

 /**
  * 이 클래스는 기상예보 데이터를 저장하는데 사용되며,
  * 각 카테고리 코드에 해당하는 값을 저장하는 LinkedHashMap을 포함한다.
  * 카테고리 코드는 FcstDataMapper.CATEGORY_CODE_MAP 매핑 테이블에서 정의된 값들을 사용한다.
  * 이 클래스는 기상예보 데이터를 처리하는데 필요한 구조를 제공한다.   
  */
public class FcstData {
    /** 
     * FcstData 객체가 생성될 때, 데이터를 저장할 LinkedHashMap을 초기화한다. <p>
     * 이 LinkedHashMap은 카테고리 코드와 해당 값을 저장하는데 사용된다. <p>
     * 예를 들어, PTY(강수형태), SKY(하늘상태) 등의 카테고리 코드가 여기에 저장된다.
     */
    public Map<String, String> data;

    /**
     * 기본 생성자. 빈 LinkedHashMap을 초기화한다.
     * <p>
     * 이 생성자는 FcstData 객체를 생성할 때, 데이터를 저장할 빈 맵을 준비한다.
     */
    public FcstData() {
        this.data = new LinkedHashMap<>();
    }

    /**
     * 매개변수로 받은 데이터를 저장하는 생성자.
     * <p>
     * 이 생성자는 외부에서 데이터를 받아 FcstData 객체를 초기화할 때 사용된다.
     * 
     * @param   data  
     *          카테고리 코드와 해당 값을 저장하는 맵
     */
    public FcstData(Map<String, String> data) {
        this.data = data;
    }
}
