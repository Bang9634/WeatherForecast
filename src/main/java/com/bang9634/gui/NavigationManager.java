package com.bang9634.gui;

import com.bang9634.FcstData;

import javax.swing.*;

/**
 * 화면 전환을 담당하는 클래스
 */
public class NavigationManager {
    private JFrame currentFrame; /** 현재 Frame을 저장한다. */

    /**
     * serviceKeyInput 화면을 출력한다.
     * serviceKey가 유효할 때 실행할 코드 블록(onSuccess)를 매개변수로 넘긴다.
     * 
     * @param   onSuccess
     *          콜백 구조를 위해 serviceKey가 유효할 때 실행할 코드 블록을 매개변수로 받는다.
     */
    public void showServiceKeyInput(Runnable onSuccess) {
        switchFrame(new ServiceKeyInputGUI(onSuccess));
    }

    /**
     * 매개 변수로 기상 정보를 받고 WeatherDisplay 화면을 출력한다.
     * 
     * @param   fcstData
     *          WeatherDisplay에 출력하기 위한 기상 예보 정보를 매개변수로 받는다.
     */
    public void showWeatherDisplay(FcstData fcstData, Runnable onNext) {
        switchFrame(new WeatherDisplayGUI(fcstData, onNext));
    }
    
    /**
     * Frame간 전환을 한다.
     * currentFrame이 존재한다면 해당 Frame을 dispose하고 매개변수로 받은 newFrame으로 교체한다.
     * 
     * @param   newFrame
     *          전환하고자 하는 새로운 Frame을 매개변수로 받는다.
     */
    private void switchFrame(JFrame newFrame) {
        if (currentFrame != null) currentFrame.dispose();;
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }

}
