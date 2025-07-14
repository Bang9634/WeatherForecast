package com.bang9634.gui;

import com.bang9634.Config;
import com.bang9634.WeatherApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ServiceKey를 입력하는 화면을 출력하는 GUI 클래스
 */
public class ServiceKeyInputGUI extends JFrame {
    private JTextField keyField; /** 키 입력창 */
    private JButton submitButton; /** submit 버튼 */
    private JLabel statusLabel; /** 상태창 */
    private Runnable onSuccess; /** 인증 성공 시 다음 동작을 담을 코드 블럭 변수 */

    /** 
     * GUI 생성자 <p>
     * 창크기의 설정과 메시지 및 버튼생성, 버튼에 액션 리스너를 추가한다.
     * 매개 변수로는 유효성 검사 통과 시 실행할 코드 블록(다음 동작)을 받는다.
     * 
     * @param   onSuccess
     *          ServiceKeyInputGUI 생성자 호출 후, serviceKey가 유효할 때 실행할 코드 블록을 뜻하는 콜백 패턴이다.
     */
    public ServiceKeyInputGUI(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        setTitle("Service Key 입력");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /**
         * 키를 입력할 텍스트 필드, 버튼, 메세지를 띄울 라벨을 선언한다.
         */
        keyField = new JTextField(25);
        submitButton = new JButton("확인");
        statusLabel = new JLabel("서비스 키를 입력하세요");

        keyField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            private void resetStatus() {
                statusLabel.setText("서비스 키를 입력하세요");
            }
        });
        
        /** 
         * 버튼에 액션 리스너를 추가한다.
         * 버튼이 액션을 감지하면 onSubmit() 메서드를 호출한다.
         */
        submitButton.addActionListener(this::onSubmit);

        /** Service Key : 메세지를 띄울 패널을 추가한다. */
        JPanel panel = new JPanel();
        panel.add(new JLabel("Service Key : "));
        panel.add(keyField);
        panel.add(submitButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        /** statusLabel 중앙 아래로 정렬해 추가한다. */
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
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
            /** serviceKey가 유효하면 Config 파일에 SERVICE_KEY에 해당 serviceKey를 덮어씌운다. */
            Config.setConfig("SERVICE_KEY", key);

            /** 인증 성공 메세지 출력과 함께 창을 해제(release)한다. */
            statusLabel.setText("인증 성공");
            dispose();

            /** 콜백 호출 */
            if (onSuccess != null) onSuccess.run();
        }
        else {
            /** 유효하지 않은키면 인증 실패 메세지를 출력한다. */
            statusLabel.setText("인증 실패");
        }
    }
}
