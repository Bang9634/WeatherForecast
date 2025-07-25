package com.bang9634.gui;

import com.bang9634.config.Config;
import com.bang9634.service.ServiceKeyValidator;
import com.bang9634.util.constants.ConfigConstants;
import com.bang9634.util.constants.MsgConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * TODO: baseFrame 만들어서 닫기 단축키나레이아웃 등등 상속해주는 인터페이스 만들기
 */

/**
 * 화면(JFrame) 전환을 전문적으로 처리하는 클래스.
 * <p>
 * AppController의 지시를 받아, 기존 화면을 닫고 새 화면을 여는 기계적인 역할만 수행함.
 * 이 클래스는 어떤 화면이 있는지, 어떻게 만드는지(new) 전혀 모름. 그냥 전달받은 JFrame을 띄우기만 함.
 * 이걸로 화면 생성 책임은 AppController에, 화면 표시 책임은 여기에 명확히 분리됨.
 *
 * @author  bangdeokjae
 */
public class ServiceKeyInputGUI extends JFrame {
    private JTextField keyField;                    /** 키 입력창 */
    private JButton submitButton;                   /** submit 버튼 */
    private JLabel statusLabel;                     /** 상태창 */
    private Runnable onSuccess;                     /** 인증 성공 시 다음 동작을 담을 코드 블럭 변수 */
    private JCheckBox keepLoginCheckBox;            /** 로그인 유지 체크박스 */
    private final ServiceKeyValidator keyValidator; /** 외부(AppController)에서 주입받는 키 검정 로직 */

    /** 
     * GUI 생성자. 화면을 구성하고 필요한 부품(의존성)을 외부에서 주입받는다.
     * <p>
     * AppController로부터 키 검증 로직(keyValidator)과 성공 시 실행할 코드(onSuccess)를 받아 멤버 변수에 저장한다.
     * 그 후, 화면에 필요한 모든 컴포넌트, 이벤트 리스너, 레이아웃을 초기화한다.
     * 
     * @param   keyValidator  
     *          키 유효성을 검증하는 로직이 담긴 객체. 이 GUI는 이 객체의 validate() 메서드만 호출하면 된다.
     * @param   onSuccess     
     *          키 인증 성공 후 실행될 코드 블록(Runnable). AppController의 goToWeatherDisplay()가 담겨서 온다.
     */
    public ServiceKeyInputGUI(ServiceKeyValidator keyValidator, Runnable onSuccess) {
        this.keyValidator = keyValidator;
        this.onSuccess = onSuccess;
        initComponents();
        initListeners();
        initLayout();

        /** 패널 add */
        add(createStatusPanel(), BorderLayout.SOUTH);
        add(createMsgPanel(), BorderLayout.CENTER);

        /** macOS에서 CMD+W 창닫기 단축키 설정 */
        KeyStroke closeKey = KeyStroke.getKeyStroke("meta W");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKey, "closeWindow");
        getRootPane().getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 화면에 사용할 Swing 컴포넌트들을 생성하고 초기화한다.
     */
    private void initComponents() {
        keyField = new JTextField(25);
        submitButton = new JButton(MsgConstants.BUTTON_ACCEPT);
        statusLabel = new JLabel(MsgConstants.MSG_INPUT_SERVICE_KEY);
        keepLoginCheckBox = new JCheckBox(MsgConstants.MSG_KEEP_LOGIN);
    }

    /**
     * 컴포넌트에 필요한 이벤트 리스너들을 설정한다.
     */
    private void initListeners() {
        /** '확인'버튼을 누르거나, keyField에서 <Enter>키를 누르면 onSubmit 메서드를 호출한다. */
        submitButton.addActionListener(this::onSubmit);
        keyField.addActionListener(this::onSubmit);

        /** keyField에 입력이 변경될 때마다 상태 메시지를 초기화한다. */
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
    }

    /**
     * JFrame의 기본적인 레이아웃과 속성을 설정한다.
     */
    private void initLayout() {
        setTitle(MsgConstants.TITLE);
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * 메인 패널(입력창, 버튼, 체크박스)을 생성한다.
     * 
     * @return  구성된 메인 패널 객체
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
     * 하단 상태 메시지 패널을 생성한다.
     * 
     * @return  구성된 상태 패널 객체
     */
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        return statusPanel;
    }


    /**
     * '확인' 버튼 클릭 또는 엔터 입력 시 실행되는 핵심 로직.
     * <p>
     * 입력된 키가 비어있으면 상태 메시지를 안내 메시지로 변경하고 리턴.
     * 키가 유효하면 Config에 서비스 키를 저장하고, 상태 메시지를 인증 성공으로 변경.
     * 로그인 유지 체크박스가 선택되면 Config에 KEEP_LOGIN을 true로 설정, 아니면 false로 설정.
     * 인증 성공 팝업을 띄우고 창을 닫는다.
     * 인증 성공 콜백(onSuccess)이 있다면 실행한다.
     *
     * @param   e
     *          액션 이벤트 객체 (사용 안 함)
     */
    private void onSubmit(ActionEvent e) {
        /** 입력된 키를 가져와 공백을 제거한다. */
        String key = keyField.getText().trim();
        if (key.isEmpty()) {
            statusLabel.setText(MsgConstants.MSG_INPUT_SERVICE_KEY);
            return;
        }

        /** 
         * 키 유효성 검증 로직을 호출하여 키가 유효한지 검사한다.
         * 만약 유효하지 않으면 인증 실패 메시지를 띄우고 리턴한다.
         * 실제 검증은 AppController에서 주입받은 keyValidator.validate(key) 메서드를 호출하여 검증한다.
         */
        if (keyValidator.validate(key)) {
            /** 로그인 유지 체크박스가 선택되어있으면 Config파일의 KEEP_LOGIN에 true, 아니면 false를 저장한다. */
            if (keepLoginCheckBox.isSelected()) {
                Config.setConfig(ConfigConstants.KEEP_LOGIN, ConfigConstants.TRUE);
            } else {
                Config.setConfig(ConfigConstants.KEEP_LOGIN, ConfigConstants.FALSE);
            }
            
            /** serviceKey가 유효하면 Config 파일에 SERVICE_KEY에 해당 serviceKey를 덮어씌운다. */
            Config.setConfig(ConfigConstants.SERVICE_KEY, key);

            /** 인증 성공을 statusLabel에 출력 및 팝업 창을 띄운다. */
            statusLabel.setText(MsgConstants.MSG_AUTH_SUCCESS);
            JOptionPane.showMessageDialog(this, MsgConstants.MSG_AUTH_SUCCESS, MsgConstants.NOTICE, JOptionPane.PLAIN_MESSAGE, null);

            /** 창을 닫고 메모리를 해제한다. */
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
