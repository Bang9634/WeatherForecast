package com.bang9634.gui;

import com.bang9634.FcstData;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * 기상 예보 정보를 출력하는 GUI 클래스
 */
public class WeatherDisplayGUI extends JFrame {
    private JTextArea textArea;

    /**
     * GUI 생성자
     */
    public WeatherDisplayGUI() {
        setTitle("Weather Forecast");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    /**
     * 매개변수로 기상 정보 FcstData를 받는 생성자. <p>
     * 
     * 디폴트 생성자를 호출하고, setWeatherData 메서드에 매개변수 fcstData를 넘겨
     * 객체 생성과 동시에 데이터 초기화를 진행한다.
     * 
     * @param   fcstData
     *          객체 생성과 동시에 데이터 초기화를 위한 기상 예보 데이터를 매개변수로 받는다.
     */
    public WeatherDisplayGUI(FcstData fcstData) {
        this();
        setWeatherData(fcstData);
    }

    /** 
     * 기상 정보를 텍스트 공간에 순차적으로 출력할 수 있도록 세팅한다.
     * 
     * @param   fcstData 
     *          기상 예보 데이터를 인자로 넘겨 출력할 수 있도록 세팅한다.
     */
    public void setWeatherData(FcstData fcstData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : fcstData.data.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        textArea.setText(sb.toString());
    }
}
