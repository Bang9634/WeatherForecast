package com.bang9634.gui;

import com.bang9634.FcstData;
import javax.swing.*;

public class NavigationManager {
    private JFrame currentFrame;

    public void showServiceKeyInput(Runnable onSuccess) {
        switchFrame(new ServiceKeyInputGUI(onSuccess));
    }

    public void showWeatherDisplay(FcstData fcstData) {
        switchFrame(new WeatherDisplayGUI(fcstData));
    }
    
    private void switchFrame(JFrame newFrame) {
        if (currentFrame != null) currentFrame.dispose();;
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }

}
