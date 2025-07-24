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
public class GridCoordinateReader {
    /** 엑셀 파일에서 읽어온 지역 좌표 값을 Map 중첩으로 트리형 계층구조로 저장한다. */
    public static final Map<String, Map<String, Map<String, int[]>>> ADDRESS_COORD_TREE = readAddressCoordinatesMapFromExcel1();

    /**
     * 엑셀 파일에서 지역 좌표 데이터를 읽어와 트리 형태로 만들어 반환한다. <p>
     * 
     * 지역(시/도) -> 시/군/구 -> 동/읍/면 -> 좌표 계층구조로 구현되어있다.
     * 
     * @return  지역 좌표 데이터를 저장한 Map 3단 중첩을 통해 구현한 트리 구조를 반환한다.
     */
    public static Map<String, Map<String, Map<String, int[]>>> readAddressCoordinatesMapFromExcel1() {
        /** Apache POI의 압축 해제 비율을 낮춰 Zib bomb 보안 제한을 완화한다. */
        ZipSecureFile.setMinInflateRatio(0.0001);

        Map<String, Map<String, Map<String, int[]>>> addressTree = new LinkedHashMap<>();
        
        /** 지역 좌표 데이터가 담겨 있는 엑셀 파일을 스트림으로 읽어 Excel Workbook에 저장한다. */
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
     * cell에 있는 데이터를 int형으로 파싱한다. <p>
     * 
     * 엑셀 파일에 nx, ny 좌표가 숫자가 아닌 문자열로 저장이 되어있어
     * 문자열로 cell의 데이터를 읽어온 다음, Integer 형태로 파싱해 리턴한다.
     * 
     * @param   cell
     *          데이터를 읽어올 cell을 매개변수로 넘긴다.
     * 
     * @return  cell에 저장되어 있던 데이터를 int 타입으로 파싱해 반환한다.
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
