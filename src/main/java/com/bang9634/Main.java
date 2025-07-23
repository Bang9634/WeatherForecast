package com.bang9634;

import com.bang9634.util.ConfigConstants;

public class Main {
    static public void main(String[] args) {
        /** 
         * 프로그램 실행 중 종료 시 실행할 작업을 등록해놓는다.
         * addShutdownHook 매개변수에 Thread 객체와 그 생성자 매개변수로
         * 람다식을 넣어 종료 시 실행할 작업(task)를 등록한다.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String keepLogin = Config.getConfig(ConfigConstants.KEEP_LOGIN);
            if (!ConfigConstants.TRUE.equals(keepLogin)) {
                Config.setConfig(ConfigConstants.SERVICE_KEY, "");
            }
        }));
        
        AppController.run();
    }
}