package com.bang9634.gui;

import com.bang9634.FcstData;
import com.bang9634.Config;
import com.bang9634.util.ConfigConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * 기상 예보 정보를 출력하는 GUI 클래스
 */
public class WeatherDisplayGUI extends JFrame {
    private JTextArea textArea;
    private JButton initServiceKeyButton;
    private Runnable onNext;

    /**
     * GUI 생성자
     */
    public WeatherDisplayGUI(FcstData fcstData, Runnable onNext) {
        this.onNext = onNext;
        setTitle("Weather Forecast");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        initServiceKeyButton = new JButton("서비스 키 초기화");
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        /** 
         * 서비스 키 초기화 버튼 액션 리스너를 추가한다.
         * 액션 감지시 onInitServiceKey 메서드를 호출한다.
         */
        initServiceKeyButton.addActionListener(this::onInitServiceKey);

        JPanel panel = new JPanel();
        panel.add(textArea);
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(initServiceKeyButton);
        add(buttonPanel, BorderLayout.SOUTH);

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

    /**
     * 서비스 키 초기화 버튼 액션을 감지했을 때 호출된다. <p>
     * 
     * Config 파일에 저장되어있는 serviceKey를 공백으로 편집하고,
     * 초기 화면으로 되돌아간다.
     */
    private void onInitServiceKey(ActionEvent e) {
        Config.setConfig(ConfigConstants.SERVICE_KEY, "");
        if (onNext != null) onNext.run();
    }
}
