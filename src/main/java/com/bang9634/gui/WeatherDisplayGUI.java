package com.bang9634.gui;

import com.bang9634.config.Config;
import com.bang9634.controller.WeatherDisplayPresenter;
import com.bang9634.model.FcstData;
import com.bang9634.util.constants.ConfigConstants;
import com.bang9634.util.constants.MsgConstants;
import com.bang9634.util.reader.GridCoordinateReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * 기상 예보 정보를 보여주는 화면(GUI) 클래스.
 * <p>
 * MVP 패턴의 'View' 역할. 그저 화면을 그리고, 사용자 입력을 Presenter에게 전달하는 역할만 한다.
 * 날씨 데이터가 무슨 의미인지, 콤보박스 선택 시 뭘 해야 하는지 전혀 모른다.
 * 모든 로직은 Presenter가 처리하고, 이 클래스는 그저 Presenter가 시키는 대로 화면을 갱신만 한다.
 * 
 * @author  bangdeokjae
 */
public class WeatherDisplayGUI extends JFrame {
    private JTextArea textArea;                                 /** 기상 정보를 출력할 텍스트 에어리어 */
    private JButton initServiceKeyButton;                       /** 서비스키 초기화 버튼 */
    private Runnable onNext;                                    /** 인증 성공 후 다음 동작을 실행할 코드 블럭 */        
    private JComboBox<String> regionCoordComboBox;              /** 시/도 콤보박스 */
    private JComboBox<String> cityCoordComboBox;                /** 시/군/구 콤보박스 */
    private JComboBox<String> streetCoordComboBox;              /** 동/읍/면 콤보박스 */
    private WeatherDisplayPresenter weatherDisplayPresenter;    /** 기상 예보 정보를 처리하는 Presenter */

    /**
     * GUI 생성자.
     * <p>
     * AppController로부터 '키 초기화' 버튼을 눌렀을 때 실행될 콜백(onNext)을 받는다.
     * 화면에 필요한 모든 컴포넌트, 이벤트 리스너, 레이아웃을 초기화한다.
     * 데이터 로직은 전혀 없고, 오직 화면 구성만 책임진다.
     *
     * @param   onNext    
     *          '서비스 키 초기화' 버튼 클릭 시 실행될 콜백
     */
    public WeatherDisplayGUI(Runnable onNext) {
        this.onNext = onNext;
        initComponents();
        initListeners();
        initLayout();
        /** 텍스트 영역 스크롤 add */
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        /** 패널 add */
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        /** macOS에서 Cmd+W로 창 닫기 단축키 설정 */
        KeyStroke closeKey = KeyStroke.getKeyStroke("meta W"); /** macOS에서는 "meta" 키가 Command 키를 의미한다. */
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKey, "closeWindow");
        getRootPane().getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Presenter를 설정한다.
     * <p>
     * 이 메서드는 AppController에서 WeatherDisplayPresenter를 주입받아 호출된다.
     * Presenter는 View(GUI)와 상호작용하며, 날씨 데이터를 가져오고 화면을 갱신하는 모든 로직을 처리한다.
     *
     * @param   weatherDisplayPresenter   
     *          WeatherDisplayPresenter 인스턴스
     */
    public void setPresenter(WeatherDisplayPresenter weatherDisplayPresenter) {
        this.weatherDisplayPresenter = weatherDisplayPresenter;
    }

    /**
     * 날씨 데이터를 텍스트 영역에 표시한다.
     * <p>
     * FcstData 객체를 받아, 그 안의 데이터를 문자열로 변환하여 textArea에 설정한다.
     * 이 메서드는 Presenter가 날씨 데이터를 가져온 후 호출된다.
     *
     * @param   fcstData   
     *          날씨 예보 데이터를 담고 있는 FcstData 객체
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
     * 컴포넌트들을 초기화한다.
     * <p>
     * 화면에 표시할 텍스트 영역, 버튼, 콤보박스 등을 생성하고 기본 설정을 적용한다.
     */
    private void initComponents() {
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
    }

    /**
     * 이벤트 리스너를 초기화한다.
     * <p>
     * 버튼 클릭, 콤보박스 선택 등 사용자 입력 이벤트를 처리할 리스너를 등록한다.
     */
    private void initListeners() {
        initServiceKeyButton.addActionListener(this::onInitServiceKey);
        regionCoordComboBox.addActionListener(this::onRegionSelected);
        cityCoordComboBox.addActionListener(this::onCitySelected);
        streetCoordComboBox.addActionListener(this::onStreetSelected);
    }

    /**
     * 레이아웃을 초기화한다.
     * <p>
     * JFrame의 기본 설정을 적용하고, 컴포넌트들을 배치할 레이아웃을 설정한다.
     */
    private void initLayout() {
        setTitle(MsgConstants.TITLE);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * 콤보 박스 패널을 생성한다.
     * <p>
     * 시/도, 시/군/구, 동/읍/면 콤보박스를 세로로 배치하여 사용자에게 지역 선택 UI를 제공한다.
     *
     * @return  구성된 콤보 박스 패널 객체
     */
    private JPanel createComoboBoxPanel() {
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
        return comboBoxPanel;
    }

    /**
     * 메인 패널을 생성한다.
     * <p>
     * 텍스트 영역과 콤보 박스를 포함하는 패널을 생성하여 화면 중앙에 배치한다.
     *
     * @return  구성된 메인 패널 객체
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        /** 메인 패널에 스크롤 가능한 텍스트 영역을 추가한다. */
        mainPanel.add(new JScrollPane(textArea));
        mainPanel.add(Box.createHorizontalStrut(10));
        /** 메인 패널에 콤보 박스 패널을 추가한다. */
        mainPanel.add(createComoboBoxPanel());
        return mainPanel;
    }

    /**
     * 버튼 패널을 생성한다.
     * <p>
     * 서비스 키 초기화 버튼을 포함하는 패널을 생성하여 화면 하단에 배치한다.
     *
     * @return  구성된 버튼 패널 객체
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(initServiceKeyButton);
        return buttonPanel;
    }

    /**
     * '서비스 키 초기화' 버튼 클릭 시 실행되는 로직.
     * <p>
     * Config에서 서비스 키를 초기화하고, 사용자에게 알림 메시지를 띄운다.
     * 이후, 인증 성공 콜백(onNext)이 있다면 실행한다.
     *
     * @param   e
     *          액션 이벤트 객체 (사용 안 함)
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
     * 콤보 박스에 시/군/구 정보를 업데이트한다.
     * <p>
     * 지역 선택 콤보 박스에서 선택된 지역에 따라 시/군/구 콤보 박스의 내용을 갱신한다.
     *
     * @param   cities    
     *          시/군/구 정보 배열
     */
    public void updateCityComboBox(String[] cities) {
        cityCoordComboBox.removeAllItems();
        for (String city : cities) {
            cityCoordComboBox.addItem(city);
        }
    }

    /**
     * 콤보 박스에 동/읍/면 정보를 업데이트한다.
     * <p>
     * 도시/구/동 콤보 박스에서 선택된 도시/구에 따라 동/읍/면 콤보 박스의 내용을 갱신한다.
     *
     * @param   streets
     *          동/읍/면 정보 배열
     */
    public void updateStreetComboBox(String[] streets) {
        streetCoordComboBox.removeAllItems();
        for (String street : streets) {
            streetCoordComboBox.addItem(street);
        }
    }

    /**
     * 로딩 상태를 표시한다.
     * <p>
     * 날씨 정보를 불러오는 중임을 사용자에게 알리는 메시지를 텍스트 영역에 표시한다.
     */
    public void showLoadingState() {
        textArea.setText("날씨 정보를 불러오는 중");
    }



    /**
     * 사용자가 '시/도' 콤보박스 선택 시 이벤트를 처리한다.
     * <p>
     * 선택된 '시/도'에 맞는 '시/군/구' 목록을 조회, Presenter에게 콤보박스 이벤트를 전달한다.
     * 
     * @param   e     
     *          액션 이벤트 객체 (사용 안 함)
     */
    private void onRegionSelected(ActionEvent e) {
        weatherDisplayPresenter.onRegionSelected((String) regionCoordComboBox.getSelectedItem());
    }

    /**
     * 사용자가 '시/군/구' 콤보박스 선택 시 이벤트를 처리한다.
     * <p>
     * 선택된 '시/도'와 '시/군/구'에 맞는 '동/읍/면' 목록을 조회, Presenter에게 콤보박스 이벤트를 전달한다.
     * 
     * @param   e     
     *          액션 이벤트 객체 (사용 안 함)
     */
    private void onCitySelected(ActionEvent e) {
        weatherDisplayPresenter.onCitySelected(
            (String) regionCoordComboBox.getSelectedItem(),
            (String) cityCoordComboBox.getSelectedItem()
        );
    }

    /**
     * 사용자가 '동/읍/면' 콤보박스 선택 시 이벤트를 처리한다.
     * <p>
     * 선택된 '시/도', '시/군/구', '동/읍/면'에 맞는 날씨 정보를 조회, Presenter에게 콤보박스 이벤트를 전달한다.
     *
     * @param   e
     *          액션 이벤트 객체 (사용 안 함)
     */
    private void onStreetSelected(ActionEvent e) {
        weatherDisplayPresenter.onLocationChanged(
            (String) regionCoordComboBox.getSelectedItem(),
            (String) cityCoordComboBox.getSelectedItem(),
            (String) streetCoordComboBox.getSelectedItem()
        );
    }
}
