package com.bang9634.gui;

import com.bang9634.Config;
import com.bang9634.WeatherApiClient;
import com.bang9634.util.ConfigConstants;
import com.bang9634.util.MsgConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * TODO: baseFrame 만들어서 닫기 단축키나레이아웃 등등 상속해주는 인터페이스 만들기
 */

/**
 * ServiceKey를 입력하는 화면을 출력하는 GUI 클래스
 */
public class ServiceKeyInputGUI extends JFrame {
    private JTextField keyField; /** 키 입력창 */
    private JButton submitButton; /** submit 버튼 */
    private JLabel statusLabel; /** 상태창 */
    private Runnable onSuccess; /** 인증 성공 시 다음 동작을 담을 코드 블럭 변수 */
    private JCheckBox keepLoginCheckBox; /** 로그인 유지 체크박스 */

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
        initComponents();
        initListeners();
        initLayout();
        /** 패널 add */
        add(createStatusPanel(), BorderLayout.SOUTH);
        add(createMsgPanel(), BorderLayout.CENTER);

        KeyStroke closeKey = KeyStroke.getKeyStroke("meta W"); // macOS용
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKey, "closeWindow");
        getRootPane().getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 컴포넌트를 초기화한다.
     */
    private void initComponents() {
        keyField = new JTextField(25);
        submitButton = new JButton(MsgConstants.BUTTON_ACCEPT);
        statusLabel = new JLabel(MsgConstants.MSG_INPUT_SERVICE_KEY);
        keepLoginCheckBox = new JCheckBox(MsgConstants.MSG_KEEP_LOGIN);
    }

    /**
     * 이벤트 리스너를 초기화한다.
     */
    private void initListeners() {
        submitButton.addActionListener(this::onSubmit);
        keyField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            /** 텍스트가 추가될 때 */
            public void insertUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            /** 텍스트가 삭제될 때 */
            public void removeUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            /** 속성이 변경될 때 */
            public void changedUpdate(javax.swing.event.DocumentEvent e) { resetStatus(); }
            /** 위 이벤트들이 감지될 때마다 statusLabel을 초기화한다. */
            private void resetStatus() {
                statusLabel.setText(MsgConstants.MSG_INPUT_SERVICE_KEY);
            }
        });
        keyField.addActionListener(this::onSubmit);
    }

    /**
     * 레이아웃을 초기화한다.
     */
    private void initLayout() {
        setTitle(MsgConstants.TITLE);
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * 메세지 패널을 만든다.
     * 
     * @return  메세지 패널 객체를 반환한다.
     */
    private JPanel createMsgPanel() {
        JPanel msgPanel = new JPanel();
        msgPanel.add(new JLabel("Service Key : "));
        msgPanel.add(keyField);
        msgPanel.add(submitButton);
        msgPanel.add(keepLoginCheckBox);
        return msgPanel;
    }
    
    /**
     * 상태 패널을 만든다.
     * 
     * @return  상태 패널 객체를 반환한다.
     */
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        return statusPanel;
    }


    /**
     * 버튼 클릭 액션을 감지했을 때 실행된다. <p>      
     */
    private void onSubmit(ActionEvent e) {
        /** keyField가 비어져있으면, 서비스키 입력 메세지를 statusLabel에 출력한다. */
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            statusLabel.setText(MsgConstants.MSG_INPUT_SERVICE_KEY);
            return;
        }

        WeatherApiClient client = new WeatherApiClient(key);
        if (client.isValiedServiceKey()) {
            /** 로그인 유지 체크박스가 선택되어있으면 Config파일의 KEEP_LOGIN에 true, 아니면 false를 저장한다. */
            if (keepLoginCheckBox.isSelected()) {
                Config.setConfig(ConfigConstants.KEEP_LOGIN, ConfigConstants.TRUE);
            } else {
                Config.setConfig(ConfigConstants.KEEP_LOGIN, ConfigConstants.FALSE);
            }
            
            /** serviceKey가 유효하면 Config 파일에 SERVICE_KEY에 해당 serviceKey를 덮어씌운다. */
            Config.setConfig(ConfigConstants.SERVICE_KEY, key);

            /** 인증 성공을 statusLabel에 출력하고 창을 해제(release)한다. */
            statusLabel.setText(MsgConstants.MSG_AUTH_SUCCESS);

            /** 인증 성공 팝업을 띄운다. */
            JOptionPane.showMessageDialog(this, MsgConstants.MSG_AUTH_SUCCESS, MsgConstants.NOTICE, JOptionPane.PLAIN_MESSAGE, null);

            dispose();

            /** 콜백 호출 */
            if (onSuccess != null) onSuccess.run();
        }
        else {
            /** 인증 실패 팝업을 띄운다. */
            JOptionPane.showMessageDialog(this, MsgConstants.MSG_AUTH_FAIL, MsgConstants.ERROR, JOptionPane.PLAIN_MESSAGE, null);
            
            /** 
             * 한글 입력 시 한글IME와 Swing 호환성 문제로 포커스를 잃고 잠시 정지하는 현상이 존재한다.
             * 아래 코드는 이를 해결하기 위해 팝업이 닫힌 뒤 입력창에 포커스를 강제로 부여하는 코드이다.
             */
            keyField.requestFocusInWindow();

            /** 유효하지 않은키면 인증 실패를 statusLabel에 출력한다. */
            statusLabel.setText(MsgConstants.MSG_AUTH_FAIL);
        }
    }
}
