package com.bang9634.util;

import com.bang9634.util.constants.MsgConstants;

/**
 * CommonUtils 클래스는 애플리케이션 전반에서 공통적으로 사용되는 유틸리티 메서드를 정의한다.
 * <p>
 * 이 클래스는 문자열 처리, 숫자 판별, Missing 값 처리 등의 기능을 제공한다.
 * @author bangdeokjae
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
     * @return  str이 숫자로 이루어져있지 않으면 false를 반환하며,
     *          숫자로 이루어진 경우엔 900이상, -900이하를 만족하면 true를 반환한다.
     */
    public static boolean isMissingValue(String str) {
        if (!isNumeric(str)) {
            return false;
        }
        double val = Double.parseDouble(str);
        return val >= 900 || val <= -900;
    }

    /**
     * Missing 값을 처리하는 메서드이다. <p>
     * 
     * 매개변수 value가 Missing 값이면 MsgConstants.MSG_MISSING_VALUE를 반환하고,
     * 그렇지 않으면 원래의 value를 반환한다.
     * 
     * @param   value
     *          Missing 값을 처리할 문자열을 매개변수로 전달한다.
     * @return  value가 Missing 값이면 MsgConstants.MSG_MISSING_VALUE를 반환하고,
     *          그렇지 않으면 원래의 value를 반환한다.
     */
    public static String handleMissingValue(String value) {
        if (isMissingValue(value)) {
            return MsgConstants.MSG_MISSING_VALUE;
        } else {
            return value;
        }
    }
}
