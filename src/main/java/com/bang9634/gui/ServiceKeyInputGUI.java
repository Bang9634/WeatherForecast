package com.bang9634.gui;

import com.bang9634.Config;
import com.bang9634.WeatherApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ServiceKeyInputGUI extends JFrame {
    private JTextField keyField;
    private JButton submitButton;
    private JLabel statusLabel;

    public ServiceKeyInputGUI() {
        setTitle("Service Key 입력");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        keyField = new JTextField(25);
        submitButton = new JButton("확인");
        statusLabel = new JLabel("서비스키를 입력하세요");
        
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

    private void onSubmit(ActionEvent e) {
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            statusLabel.setText("서비스키를 입력하세요.");
            return;
        }
        WeatherApiClient client = new WeatherApiClient(key);
        if (client.isValiedServiceKey()) {
            try {
                Config.setServiceKeyOnConfig(key);
                statusLabel.setText("인증 성공");
                dispose();

                javax.swing.SwingUtilities.invokeLater(com.bang9634.Main::showWeatherGUI);
            } catch (IOException ex) {
                statusLabel.setText("설정 파일 저장 실패" + ex.getMessage());
            }
        }
        else {
            statusLabel.setText("인증 실패: unavalible Service key");
        }
    }
}
