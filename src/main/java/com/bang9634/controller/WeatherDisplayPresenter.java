package com.bang9634.controller;

import com.bang9634.gui.WeatherDisplayGUI;
import com.bang9634.model.FcstData;
import com.bang9634.service.WeatherService;
import com.bang9634.util.reader.GridCoordinateReader;

import javax.swing.*;
import java.util.Map;

/**
 * WeatherDisplayGUI(View)의 모든 로직과 상태를 관리하는 Presenter.
 * <p>
 * MVP(Model-View-Presenter) 아키텍처의 'Presenter' 역할. View(GUI)와 Model(Service) 사이의 중재자다.
 * View로부터 사용자 입력을 받아 비즈니스 로직(Service)을 호출하고, 그 결과를 다시 View에 반영하는 모든 책임을 진다.
 * 이 패턴으로 View는 화면 그리는 '멍청한(Dumb)' 역할에만 집중. 비즈니스 로직과 UI가 명확히 분리되어 테스트와 유지보수가 쉬워진다.
 *
 * <ul>
 *   <li>{@link #setView(WeatherDisplayGUI)} - 상호작용할 View를 설정한다.</li>
 *   <li>{@link #loadInitialData()} - 초기 날씨 데이터를 로드하고 View를 설정한다.</li>
 *   <li>{@link #onRegionSelected(String)} - '시/도' 콤보박스 선택 이벤트를 처리한다.</li>
 *   <li>{@link #onCitySelected(String, String)} - '시/군/구' 콤보박스 선택 이벤트를 처리한다.</li>
 *   <li>{@link #onLocationChanged(String, String, String)} - '동/읍/면' 콤보박스 선택 이벤트 처리하여 날씨를 갱신한다.</li>
 *   <li>{@link #fetchAndUpdateWeather(int, int)} - 날씨 정보를 비동기로 가져와 View를 업데이트한다.</li>
 * </ul>
 *
 * @author  bangdeokjae
 */
public class WeatherDisplayPresenter {
    private final WeatherService weatherService;
    private WeatherDisplayGUI view;

    /**
     * 의존성 주입(DI)으로 WeatherService를 받아 Presenter 생성한다.
     * <p>
     * Presenter는 WeatherService를 직접 만들지 않고, 외부(AppController)에서 주입받아 사용한다.
     * 이로써 WeatherService 구현이 바뀌어도 Presenter 코드는 영향받지 않으며, 유연성을 향상시킨다.
     * 
     * @param   weatherService    
     *          날씨 데이터 조회 등 비즈니스 로직을 처리하는 서비스 객체
     */
    public WeatherDisplayPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Presenter가 제어할 View(GUI) 객체를 설정, 양방향 관계를 만든다.
     * <p>
     * AppController가 호출하고, 이 연결로 Presenter는 View 상태를 바꾸고, View는 사용자 이벤트를 Presenter에게 전달 가능하다.
     * 
     * @param   view  
     *          화면 표시를 담당하며 Presenter와 상호작용할 WeatherDisplayGUI 인스턴스
     */
    public void setView(WeatherDisplayGUI view) {
        this.view = view;
    }

    /**
     * 사용자가 '시/도' 콤보박스 선택 시 이벤트를 처리한다.
     * <p>
     * 선택된 '시/도'에 맞는 '시/군/구' 목록을 GridCoordinateReader에서 찾아 View에게 콤보박스 갱신을 지시한다.
     * 
     * @param   region    
     *          View의 콤보박스에서 사용자가 선택한 '시/도' 이름
     */
    public void onRegionSelected(String region) {
        if (region == null) return;
        Map<String, Map<String, int[]>> cityMap = GridCoordinateReader.ADDRESS_COORD_TREE.get(region);
        view.updateCityComboBox(cityMap.keySet().toArray(new String[0]));
    }

    /**
     * 사용자가 '시/군/구' 콤보박스 선택 시 이벤트를 처리한다.
     * <p>
     * 선택된 '시/도'와 '시/군/구'에 맞는 '동/읍/면' 목록을 조회, View에게 콤보박스 갱신을 지시한다.
     * 
     * @param   region    
     *          선택된 '시/도' 이름
     * @param   city      
     *          선택된 '시/군/구' 이름
     */
    public void onCitySelected(String region, String city) {
        if (region == null || city == null) return;
        Map<String, int[]> streetMap = GridCoordinateReader.ADDRESS_COORD_TREE.get(region).get(city);
        view.updateStreetComboBox(streetMap.keySet().toArray(new String[0]));
    }

    /**
     * 사용자가 최종 지역('동/읍/면') 선택 시, 해당 지역의 날씨 정보 조회를 트리거한다.
     * <p>
     * 선택된 지역의 격자 좌표(nx, ny)를 찾아, {@link #fetchAndUpdateWeather} 메서드를 호출하여 실제 데이터 조회를 시작한다.
     * 
     * @param   region    
     *          선택된 '시/도' 이름
     * @param   city      
     *          선택된 '시/군/구' 이름
     * @param   street    
     *          선택된 '동/읍/면' 이름
     */
    public void onLocationChanged(String region, String city, String street) {
        if (region == null || city == null || street == null) return;
        int[] coord = GridCoordinateReader.ADDRESS_COORD_TREE.get(region).get(city).get(street);
        fetchAndUpdateWeather(coord[0], coord[1]);
    }

    /**
     * AppController가 호출. 초기 화면 데이터 로드 및 설정한다.
     * <p>
     * 기본 위치(서울) 날씨를 불러오고, 콤보박스도 초기 상태(서울특별시)로 설정, 사용자에게 완전한 초기 화면 제공한다.
     */
    public void loadInitialData() {
        fetchAndUpdateWeather(60, 127);
        onRegionSelected("서울특별시");
    }

    /**
     * 특정 좌표의 날씨 데이터를 비동기로 가져와 View를 업데이트하는 핵심 로직이다.
     * <p>
     * 네트워크 통신은 오래 걸릴 수 있으니 SwingWorker로 백그라운드 스레드에서 API를 호출한다.
     * 이걸로 통신 중 GUI 멈춤(Freezing) 현상 방지, 사용자 경험을 향상시킨다.
     * 작업 전 View에 로딩 상태 표시, 작업 끝나면 결과 받아 View에 데이터 그리며, 예외 터지면 잡아서 사용자에게 에러 메시지 표시하도록 지시한다.
     * 
     * @param   nx    
     *          날씨를 조회할 예보지점의 X 좌표
     * @param   ny    
     *          날씨를 조회할 예보지점의 Y 좌표
     */
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