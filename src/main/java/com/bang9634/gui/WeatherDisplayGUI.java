package com.bang9634.gui;

import com.bang9634.FcstData;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class WeatherDisplayGUI extends JFrame {
    private JTextArea textArea;

    public WeatherDisplayGUI() {
        setTitle("Weather Forecast");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void displayWeather(FcstData fcstData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : fcstData.data.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        textArea.setText(sb.toString());
    }
}
