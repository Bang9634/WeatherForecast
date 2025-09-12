package com.bang9634.gui;

import javax.swing.*;

/**
 * 화면(JFrame) 전환을 전문적으로 처리하는 클래스.
 * <p>
 * AppController의 지시를 받아, 기존 화면을 닫고 새 화면을 여는 기계적인 역할만 수행한다.
 * 이 클래스는 어떤 화면이 있는지, 어떻게 만드는지(new) 모른다. 그냥 전달받은 JFrame을 띄우기만 한다.
 * 이걸로 화면 생성 책임은 AppController에, 화면 표시 책임은 여기에 명확히 분리된다.
 *
 * @author  bangdeokjae
 */
public class NavigationManager {
    /** 현재 화면에 떠 있는 JFrame 객체를 저장하는 변수이다. */
    private JFrame currentFrame;

    /**
     * AppController에서 이미 완전히 조립된 JFrame을 받아 화면에 표시한다.
     * <p>
     * MVP 패턴에서 주로 사용하며, 화면 생성과 초기화는 AppController와 Presenter가 담당하고, 
     * NavigationManager는 그저 띄우는 역할만 한다.
     * 
     * @param   frame 
     *          화면에 표시할, 이미 생성된 JFrame 객체
     */
    public void showFrame(JFrame frame) {
        switchFrame(frame);
    }
    
    /**
     * 실제 화면 전환 로직을 처리하는 private 헬퍼 메서드이다.
     * <p>
     * 만약 현재 떠 있는 화면(currentFrame)이 있으면, 먼저 dispose()로 닫아서 메모리 누수를 방지한다.
     * 그 다음, 새로 받은 프레임을 currentFrame으로 지정하고 setVisible(true)로 화면에 보여준다.
     * 
     * @param   newFrame 
     *          새로 표시할 JFrame 객체
     */
    private void switchFrame(JFrame newFrame) {
        if (currentFrame != null) currentFrame.dispose();
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }

}
