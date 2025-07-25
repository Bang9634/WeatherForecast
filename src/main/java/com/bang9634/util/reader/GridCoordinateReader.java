package com.bang9634.util.reader;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 지역 별 nx, ny 좌표가 담긴 gridCoordinates.xlsx 파일을 읽어오기 위한 클래스 <p>
 * 지역명에 [nx, ny] 매핑을 한다. 지역명의 경우 RegionKey 클래스에
 * region(시/도), city(시/군/구), street(동/읍/면) 3단계로 나누어 저장된다.
 */

/** 
 * GridCoordinateReader 클래스는 지역 좌표 데이터를 읽어오는 유틸리티 클래스이다.
 * <p>
 * 이 클래스는 엑셀 파일에서 지역 좌표 데이터를 읽어와 트리 형태로 저장한다.
 * 지역(시/도) -> 시/군/구 -> 동/읍/면 -> 좌표 계층구조로 구현되어 있다.
 * <p>
 * 트리 구조는 Map 중첩을 통해 구현되며, 각 지역의 좌표(nx, ny)를 int 배열로 저장한다.
 */
public class GridCoordinateReader {
    /** 엑셀 파일에서 읽어온 지역 좌표 값을 Map 중첩으로 트리형 계층구조로 저장한다. */
    public static final Map<String, Map<String, Map<String, int[]>>> ADDRESS_COORD_TREE = readAddressCoordinatesMapFromExcel();

    /**
     * 엑셀 파일에서 지역 좌표 데이터를 읽어와 트리 형태로 만들어 반환한다. <p>
     * 
     * 지역(시/도) -> 시/군/구 -> 동/읍/면 -> 좌표 계층구조로 구현되어있다.
     * 
     * @return  지역 좌표 데이터를 저장한 Map 3단 중첩을 통해 구현한 트리 구조를 반환한다.
     */

    /**
     * 엑셀 파일에서 지역 좌표 데이터를 읽어와 트리 형태로 만들어 반환한다.
     * <p>
     * 지역(시/도) -> 시/군/구 -> 동/읍/면 -> 좌표 계층구조로 구현되어 있다.
     * <p>
     * 이 메서드는 엑셀 파일을 읽어와 각 지역의 좌표(nx, ny)를 Map 중첩 구조로 저장한다.
     * 
     * @return  지역 좌표 데이터를 저장한 Map 3단 중첩을 통해 구현한 트리 구조를 반환한다.
     */
    public static Map<String, Map<String, Map<String, int[]>>> readAddressCoordinatesMapFromExcel() {
        /**
         * ZipSecureFile의 최소 압축 해제 비율을 설정한다.
         * 이 설정은 Zip bomb 공격을 방지하기 위해 사용되며, 압축 해제 비율이 너무 낮은 경우 예외를 발생시킨다.
         * 여기서는 0.0001로 설정하여, 압축 해제 비율이 0.0001 이상인 경우에만 압축 해제를 허용한다.
         */
        ZipSecureFile.setMinInflateRatio(0.0001);

        /** 지역 좌표 데이터를 저장할 Map 3단 중첩 구조를 초기화한다. */
        Map<String, Map<String, Map<String, int[]>>> addressTree = new LinkedHashMap<>();
        
        /** 지역 좌표 데이터가 담겨 있는 엑셀 파일을 스트림으로 읽어 Excel Workbook에 저장한다. */

        /** 
         * 엑셀 파일을 읽어오기 위해 InputStream을 사용하여 Workbook 객체를 생성한다.
         * Workbook 객체는 엑셀 파일의 시트, 행, 셀 등의 데이터를 읽어오는 데 사용된다.
         * <p>
         * 이 과정에서 발생할 수 있는 예외를 처리하기 위해 try-with-resources 문을 사용한다.
         */
        try (InputStream is = GridCoordinateReader.class.getResourceAsStream("/gridCoordinates.xlsx");
        Workbook workbook = new XSSFWorkbook(is)) {
            
            /** 0 index의 시트를 불러온다. */
            Sheet sheet = workbook.getSheetAt(0);
            /** 각 열에서 데이터를 뽑아온다. */
            for (Row row : sheet) {
                /** 각 열의 첫 번째 줄은 무시한다. */
                if (row.getRowNum() == 0) continue;

                /** row의 2열 Cell의 String 값을 region에 저장한다. */
                String region = row.getCell(2).getStringCellValue().trim();
                
                /** row의 3열 Cell을 불러와 Cell 값이 null인 경우 ""를, 아니면 String 값을 city에 저장한다. */
                Cell cityCell = row.getCell(3);
                String city = (cityCell != null) ? cityCell.getStringCellValue().trim() : "";

                /** row의 4열 Cell을 불러와 Cell 값이 null인 경우 ""를, 아니면 String 값을 city에 저장한다. */
                Cell streetCell = row.getCell(4);
                String street = (streetCell != null) ? streetCell.getStringCellValue().trim() : "";
                
                /** row의 5열과 6열 값을 int형으로 파싱해 불러와 nx, ny에 저장한다. */
                int nx = parseCellAsInt(row.getCell(5));
                int ny = parseCellAsInt(row.getCell(6));
                
                /**
                 * 매개변수로 넘긴 값과 일치한 키가 존재하지 않으면, 새로운 LinkedHashMap을
                 * 만들어 넣고, 있으면 기존 값을 반환한다. 이를 반복하여 최종적으로
                 * 마지막 LinkedHashMap에는 좌표값을 저장한다.
                 * 없으면 생성, 있으면 그대로 사용하여 트리 구조를 간결하게 만들 수 있다.
                 * <p>    
                 * region(시/도), city(시/군/구), street(동/읍/면) 3단계로 나누어
                 * 지역 좌표 데이터를 저장한다.
                 * <p>
                 * region은 시/도 이름, city는 시/군/구 이름, street은 동/읍/면 이름을 나타낸다.
                 * <p>
                 * 마지막 단계에서는 nx, ny 좌표를 int 배열로 저장한다.
                 */
                addressTree.computeIfAbsent(region, r -> new LinkedHashMap<>())
                    .computeIfAbsent(city, c -> new LinkedHashMap<>())
                    .put(street, new int[]{nx, ny});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** 시트의 모든 데이터를 추출해 put한 LinkedHashMap을 반환한다. */
        return addressTree;
    }


    /**
     * cell에 있는 데이터를 int형으로 파싱한다.
     * <p>
     * cell이 null인 경우 0을 반환하고, CellType이 NUMERIC인 경우
     * 해당 값을 그대로 반환한다. CellType이 STRING인 경우 문자열을
     * trim한 후 Integer.parseInt()를 사용하여 int형으로 변환한다.
     * 만약 문자열이 숫자로 변환할 수 없는 경우 NumberFormatException이 발생하면
     * 0을 반환한다.
     * 
     * @param   cell
     *          파싱할 Cell 객체   
     * @return  int
     *          파싱된 int 값 또는 0
     */
    private static int parseCellAsInt(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            /** 데이터가 숫자라면 그대로 읽어온다. */
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            /** 문자열로 저장되어 있다면 문자열 그대로 읽어온 다음 Integer 형태로 파싱한다. */
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
