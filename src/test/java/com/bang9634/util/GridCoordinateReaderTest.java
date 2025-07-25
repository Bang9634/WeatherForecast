package com.bang9634.util;

import com.bang9634.util.reader.GridCoordinateReader;

public class GridCoordinateReaderTest {
    public static void main(String[] args) {
        int[] coord = GridCoordinateReader.ADDRESS_COORD_TREE.get("서울특별시").get("종로구").get("창신제1동");
        System.out.println(coord[0] + " " + coord[1]);
    }
}
