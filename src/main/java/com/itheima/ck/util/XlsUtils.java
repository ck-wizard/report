package com.itheima.ck.util;

import com.sun.istack.internal.NotNull;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsUtils {
    public static void readExcel2003Or2007(InputStream is, @NotNull XmlHock hock) throws IOException {
        //Excel
        HSSFWorkbook workbook = new HSSFWorkbook(is);

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            //Sheet
            HSSFSheet hssfSheet = workbook.getSheetAt(i);
            if (hssfSheet == null) {
                continue;
            }
            // 完整一行
            List<Map<Integer, String>> complateLine = new ArrayList<>();
            for (int rowNum = 0; rowNum < hssfSheet.getLastRowNum(); rowNum++) {
                //Row
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                // 获取最后一个单元格+1的值
                int cellNum = hssfSheet.getLastRowNum();
                Map<Integer, String> cellData = new HashMap<>();
                for (Integer cellIndex = 0; cellIndex < cellNum; cellIndex++) {
                    //Cell
                    HSSFCell cell = hssfRow.getCell(cellIndex);
                    String cellValue = getCellValue(cell);
                    cellData.put(cellIndex, cellValue);
                }
                if(hock.hasMore(cellData)) {
                    complateLine.add(cellData);
                    continue;
                } else {
                    hock.hock(complateLine, rowNum);
                    // 清除完整的一行数据
                    complateLine.clear();
                }
            }
        }
        workbook.close();
    }

    private static String getCellValue(HSSFCell cell) {
        if(cell == null) return null;
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
            case BOOLEAN:
            case FORMULA:
            case STRING:
                return cell.getStringCellValue();
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return null;
        }
    }
}
