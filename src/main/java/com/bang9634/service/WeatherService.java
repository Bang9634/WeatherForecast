package com.bang9634.service;

import com.bang9634.provider.WeatherProvider;
import com.bang9634.provider.parser.WeatherDataParser;
import com.bang9634.util.constants.WeatherConstants;
import com.bang9634.model.FcstData;

public class WeatherService {
    private final WeatherProvider weatherProvider;
    private final WeatherDataParser weatherDataParser;

    public WeatherService(WeatherProvider weatherprovider, WeatherDataParser weatherDataParser) {
        this.weatherProvider = weatherprovider;
        this.weatherDataParser = weatherDataParser;
    }

    public FcstData getWeather(String nx, String ny) throws Exception {
        String rawJson;
        try {
            rawJson = weatherProvider.fetchRawWeatherData(
                WeatherConstants.LABEL_BASE_DATE, 
                WeatherConstants.LABEL_BASE_TIME, 
                nx, ny
            );
        } catch (Exception e) {
            throw new Exception("외부 API에서 데이터 가져오는 중 오류 발생", e);
        }
        return weatherDataParser.parse(rawJson);
    }

    public boolean isServiceKeyValid() {
        try {
            getWeather("60", "127");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
