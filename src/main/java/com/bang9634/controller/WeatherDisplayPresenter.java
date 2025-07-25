package com.bang9634.controller;

import com.bang9634.gui.WeatherDisplayGUI;
import com.bang9634.service.WeatherService;
import com.bang9634.util.reader.GridCoordinateReader;
import com.bang9634.model.FcstData;

import java.util.Map;
import javax.swing.*;

public class WeatherDisplayPresenter {
    private final WeatherService weatherService;
    private WeatherDisplayGUI view;

    public WeatherDisplayPresenter(WeatherService weatherSerivce) {
        this.weatherService = weatherSerivce;
    }

    public void setView (WeatherDisplayGUI view) {
        this.view = view;
    }

    public void onRegionSelected(String region) {
        if (region == null) return;
        Map<String, Map<String, int[]>> cityMap = GridCoordinateReader.ADDRESS_COORD_TREE.get(region);
        view.updateCityComboBox(cityMap.keySet().toArray(new String[0]));
    }

    public void onCitySelected(String region, String city) {
        if (region == null || city == null) return;
        Map<String, int[]> streetMap = GridCoordinateReader.ADDRESS_COORD_TREE.get(region).get(city);
        view.updateStreetComboBox(streetMap.keySet().toArray(new String[0]));
    }

    public void onLocationChanged(String region, String city, String street) {
        if (region == null || city == null || street == null) return;
        int[] coord = GridCoordinateReader.ADDRESS_COORD_TREE.get(region).get(city).get(street);
        fetchAndUpdateWeather(coord[0], coord[1]);
    }

    public void loadInitialData() {
        fetchAndUpdateWeather(60, 127);
        onRegionSelected("서울특별시");
    }

    private void fetchAndUpdateWeather(int nx, int ny) {
        view.showLoadingState();
        new SwingWorker<FcstData, Void>() {
            @Override
            protected FcstData doInBackground() throws Exception {
                return weatherService.getWeather(String.valueOf(nx), String.valueOf(ny));
            }

            @Override
            protected void done() {
                try {
                    FcstData data = get();
                    view.setWeatherData(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}