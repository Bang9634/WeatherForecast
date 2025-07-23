package com.bang9634.util;

/**
 * 편의성 유틸 메서드들이 있는 클래스이다. <p>
 * 
 * 해당 클래스에는 여러 곳에서 재사용할 수 있는 공통 메서드들을 작성한다.
 */
public class CommonUtils {

    /**
     * 문자열이 숫자로 이루어져있는 지 판별하는 메서드이다. <p>
     * 
     * 정수, 음수, 소수, 과학적 표기법 등 모든 숫자 형태를 판별할 수 있다.
     * 예외처리 방식을 이용하여 판별한다.
     * 
     * @param   str
     *          숫자로 이루어져있는지 확인하고자 하는 문자열을 매개변수로 넘긴다.
     * 
     * @return  문자열이 숫자로 이루어져있으면 true, 그렇지 않으면 예외처리를 통해 false를 반환한다.
     */
    public static boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** 
     * Missing 값을 판별하기 위한 메서드이다. <p>
     * 
     * 매개변수 str이 숫자로 이루어져 있지 않으면 false를 반환한다.
     * 숫자로 이루어져있다면 -900 이하 또는 900 이상의 값을 Missing 값으로 취급하여 true를 반환한다.
     * 
     * @param   str
     *          Missing 값인지 판별할 문자열을 매개변수로 전달한다.
     *  
     * @return  str이 숫자로 이루어져있지 않으면 false를 반환하며,
     *          숫자로 이루어진 경우엔 900이상, -900이하륾 만족하면 true를 반환한다.
     */
    public static boolean isMissingValue(String str) {
        if (!isNumeric(str)) {
            return false;
        }
        double val = Double.parseDouble(str);
        return val >= 900 || val <= -900;
    }
}
