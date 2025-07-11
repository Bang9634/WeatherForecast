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
     * 기상 정보를 텍스트 공간에 순차적으로 출력한다.
     * 
     * @param   fcstData 
     *          기상 예보 데이터를 인자로 넘겨 GUI로 출력한다.
     */
    public void displayWeather(FcstData fcstData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : fcstData.data.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    /**
     * 정적 메서드로써 객체를 생성하지 않고 호출이 가능하다. <p>
     * 메서드 내부에서 WeatherDisplayGUI 객체를 생성하여 displayWeather() 메서드를 호출한다.
     * 
     * @param   fcstData
     *          기상 예보 데이터를 인자로 넘겨 GUI로 출력한다.
     */
    public static void show(FcstData fcstData) {
        WeatherDisplayGUI gui = new WeatherDisplayGUI();
        gui.setVisible(true);
        gui.displayWeather(fcstData);
    }
}
