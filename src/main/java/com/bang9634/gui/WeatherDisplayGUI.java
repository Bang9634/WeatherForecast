package com.bang9634.gui;

import com.bang9634.Config;
import com.bang9634.model.FcstData;
import com.bang9634.util.ConfigConstants;
import com.bang9634.util.GridCoordinateReader;
import com.bang9634.util.MsgConstants;
import com.bang9634.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * 기상 예보 정보를 출력하는 GUI 클래스
 */
public class WeatherDisplayGUI extends JFrame {
    private JTextArea textArea;                     /** 기상 정보를 출력할 텍스트 에어리어 */
    private JButton initServiceKeyButton;           /** 서비스키 초기화 버튼 */
    private Runnable onNext;
    private JComboBox<String> regionCoordComboBox;  /** 시/도 콤보박스 */
    private JComboBox<String> cityCoordComboBox;    /** 시/군/구 콤보박스 */
    private JComboBox<String> streetCoordComboBox;  /** 동/읍/면 콤보박스 */

    /**
     * GUI 생성자
     * 
     * * TODO: 컴포넌트 생성, 레이아웃 구성, 이벤트 리스너 등록 등 각 메서드로 분리해서 작성하기
     */
    public WeatherDisplayGUI(FcstData fcstData, Runnable onNext) {
        this.onNext = onNext;

        /** 컴포넌트 생성 */
        textArea = new JTextArea();     
        textArea.setEditable(false);
        textArea.setFont(new Font("AppleGothic", Font.PLAIN, 12));
        initServiceKeyButton = new JButton(MsgConstants.BUTTON_INIT_SERVICE_KEY);
        regionCoordComboBox = new JComboBox<>(GridCoordinateReader.ADDRESS_COORD_TREE
            .keySet()
            .toArray(new String[0]));

        cityCoordComboBox = new JComboBox<>(GridCoordinateReader.ADDRESS_COORD_TREE
            .get(regionCoordComboBox.getSelectedItem())
            .keySet()
            .toArray(new String[0]));

        streetCoordComboBox = new JComboBox<>(GridCoordinateReader.ADDRESS_COORD_TREE
            .get(regionCoordComboBox.getSelectedItem())
            .get(cityCoordComboBox.getSelectedItem())
            .keySet()
            .toArray(new String[0]));

        /** 이벤트 리스너 등록 */
        initServiceKeyButton.addActionListener(this::onInitServiceKey);
        regionCoordComboBox.addActionListener(this::onRegionSelected);
        cityCoordComboBox.addActionListener(this::onCitySelected);
        streetCoordComboBox.addActionListener(this::onStreetSelected);

        /** 레이아웃 및 패널 구성 */
        setTitle(MsgConstants.TITLE);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /** 텍스트 영역 스크롤 포함 */
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        JScrollPane textScrollPane = new JScrollPane(textArea);
            
        /** 콤보박스 패널 */
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));
        comboBoxPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        regionCoordComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cityCoordComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        streetCoordComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboBoxPanel.add(regionCoordComboBox);
        comboBoxPanel.add(Box.createVerticalStrut(4));
        comboBoxPanel.add(cityCoordComboBox);
        comboBoxPanel.add(Box.createVerticalStrut(4));
        comboBoxPanel.add(streetCoordComboBox);
        comboBoxPanel.setMaximumSize(new Dimension(200, 150));
        comboBoxPanel.add(Box.createVerticalGlue());
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        /** 메인 패널 */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(textScrollPane);
        mainPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(comboBoxPanel);

        /** 버튼 패널 */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(initServiceKeyButton);

        /** 패널 add */
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        /** 데이터 set */
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
            sb.append(entry.getKey())
                .append(" : ")
                .append(entry.getValue())
                .append("\n");
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
        JOptionPane.showMessageDialog(
            this, 
            MsgConstants.MSG_DIALOG_INIT_SERVICE_KEY, 
            MsgConstants.NOTICE, 
            JOptionPane.PLAIN_MESSAGE, 
            null
        );
        if (onNext != null) onNext.run();
    }

    /** 
     * 콤보 박스 item 선택 액션을 감지했을 때 호출된다. <p>
     * 
     * item 선택 시, 콤보박스 내용물 초기화 및 좌표 값을 불러와 이를 토대로 기상정보를 업데이트한다.
     */
    private void onRegionSelected(ActionEvent e) {
        String selectedRegion = (String) regionCoordComboBox.getSelectedItem();
        cityCoordComboBox.removeAllItems();
        streetCoordComboBox.removeAllItems();
        if (selectedRegion != null) {
            Map<String, Map<String, int[]>> cityMap = GridCoordinateReader.ADDRESS_COORD_TREE.get(selectedRegion);
            for (String city : cityMap.keySet()) {
                cityCoordComboBox.addItem(city);
            }
            updateWeatherBySelectedComboBox();
        }
    }
    
    /** 
     * city 콤보박스 이벤트를 감지했을 때 실행된다.
    */
    private void onCitySelected(ActionEvent e) {
        String selectedRegion = (String) regionCoordComboBox.getSelectedItem();
        String selectedCity = (String) cityCoordComboBox.getSelectedItem();
        streetCoordComboBox.removeAllItems();
        if (selectedRegion != null && selectedCity != null) {
            Map<String, int[]> streetMap = GridCoordinateReader.ADDRESS_COORD_TREE
                .get(selectedRegion)
                .get(selectedCity);
                
            for (String street : streetMap.keySet()) {
                streetCoordComboBox.addItem(street);
            }
            updateWeatherBySelectedComboBox();
        }
    }

    /**
     * street 콤보박스 이벤트를 감지했을 때 실행된다.
     */
    private void onStreetSelected(ActionEvent e) {
        String selectedRegion = (String) regionCoordComboBox.getSelectedItem();
        String selectedCity = (String) cityCoordComboBox.getSelectedItem();
        String selectedStreet = (String) streetCoordComboBox.getSelectedItem();

        if (selectedRegion != null && selectedCity != null && selectedStreet != null) {
            updateWeatherBySelectedComboBox();
        }
    }

    /**
     * 콤보박스 선택시 기상 예보 데이터를 업데이트한다. <p>
     * 
     * 3개의 콤보박스로 선택한 지역 좌표를 통해 API를 호출해 기상 예보정보를 가져와
     * 백그라운드에서 작업을 수행한다.
     */
    private void updateWeatherBySelectedComboBox() {
        String selectedRegion = (String) regionCoordComboBox.getSelectedItem();
        String selectedCity = (String) cityCoordComboBox.getSelectedItem();
        String selectedStreet = (String) streetCoordComboBox.getSelectedItem();

        if (selectedRegion != null && selectedCity != null && selectedStreet != null) {
            int[] coord = GridCoordinateReader.ADDRESS_COORD_TREE
            .get(selectedRegion)
            .get(selectedCity)
            .get(selectedStreet);

            if (coord != null) {
                String serviceKey = Config.getConfig(ConfigConstants.SERVICE_KEY);
                String nx = String.valueOf(coord[0]);
                String ny = String.valueOf(coord[1]);

                /** 백그라운드에서 API를 호출한다. */
                new SwingWorker<FcstData, Void>() {
                    @Override
                    protected FcstData doInBackground() {
                        return AppController.fetchWeatherData(serviceKey, nx, ny);
                    }
                    @Override
                    protected void done() {
                        try {
                            FcstData fcstData = get();
                            setWeatherData(fcstData);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            textArea.setText("기상 정보를 불러오지 못했습니다.");
                        }
                    }
                }.execute();
            }
        }   
    }
}
