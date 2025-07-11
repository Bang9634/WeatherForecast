package com.bang9634.gui;

import com.bang9634.Config;
import com.bang9634.WeatherApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * ServiceKey를 입력하는 화면을 출력하는 GUI 클래스
 */
public class ServiceKeyInputGUI extends JFrame {
    private JTextField keyField;
    private JButton submitButton;
    private JLabel statusLabel;

    /** 생성자. 객체 생성 순간에 화면 출력을 한다. */
    public ServiceKeyInputGUI() {
        setTitle("Service Key 입력");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /**
         * 키를 입력할 텍스트 필드, 버튼, 메세지를 띄울 라벨을 선언한다.
         */
        keyField = new JTextField(25);
        submitButton = new JButton("확인");
        statusLabel = new JLabel("서비스키를 입력하세요");
        
        /** 
         * 버튼에 액션 리스너를 추가한다.
         * 버튼이 액션을 감지하면 onSubmit() 메서드를 호출한다.
         */
        submitButton.addActionListener(this::onSubmit);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Service Key : "));
        panel.add(keyField);
        panel.add(submitButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * 버튼 클릭 액션을 감지했을 때 실행되는 메서드. <p>      
     */
    private void onSubmit(ActionEvent e) {
        /** keyField가 비어져있으면, 서비스키 입력 메세지를 출력한다. */
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            statusLabel.setText("서비스키를 입력하세요.");
            return;
        }

        WeatherApiClient client = new WeatherApiClient(key);
        if (client.isValiedServiceKey()) {
            try {
                /** serviceKey가 유효하면 Config 파일에 SERVICE_KEY에 해당 serviceKey를 덮어씌운다. */
                Config.setServiceKeyOnConfig(key);

                /** 인증 성공 메세지 출력과 함께 창을 해제(release)한다. */
                statusLabel.setText("인증 성공");
                dispose();

                /** 
                 * 기상정보를 출력하는 메서드를 호출한다.
                 * @todo 코드 꼬임 주범. Main에 과한 책임과 역할 부여. 수정필요함.
                 */
                javax.swing.SwingUtilities.invokeLater(com.bang9634.Main::showWeatherGUI);
            } catch (IOException ex) {
                /** 예외 발생시 메세지를 출력한다. */
                statusLabel.setText("설정 파일 저장 실패" + ex.getMessage());
            }
        }
        else {
            /** 유효하지 않은키면 인증 실패 메세지를 출력한다. */
            statusLabel.setText("인증 실패: unavalible Service key");
        }
    }
}
