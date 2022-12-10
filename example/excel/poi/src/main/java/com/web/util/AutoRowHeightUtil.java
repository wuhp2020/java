package com.web.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoRowHeightUtil {
    /**
     * 自适应excel行高
     * @param cell 列
     * @param margin 字体边距
     */
    public static void autoRowHeight(XSSFCell cell, short margin) {
        if(cell == null) {
            return ;
        }
        String cellVal = getStringCellValue(cell);
        if(StringUtils.isBlank(cellVal)) {
            return;
        }
        XSSFRow row = cell.getRow();
        XSSFWorkbook workbook = row.getSheet().getWorkbook();
        short charHeight = cell.getCellStyle().getFont().getFontHeightInPoints();
        // 字体像素
        float charPx = charHeight / (float)72 * (float)96;
        // 宽度像素
        float cellWidthPx = getCellWidth(cell);
        // 字符长度
        int charLength = cellVal.length();
        // 每一个列（包括合并的列）的字数
        int charInCell = (int)(cellWidthPx/charPx);
        // 在指定宽度的列中的字符展现行数
        int rowNum =  charLength / charInCell + (charLength % charInCell > 0 ? 1 : 0);
        // 自适应以后的行高点数
        short cellHeightPx = (short) (((short)rowNum) * (charHeight + margin));
        row.setHeightInPoints(cellHeightPx);
    }

    /**
     * 自适应excel行高
     * @param cell 列
     */
    private static String getStringCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_FORMULA:
                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }
                return cell.getCellFormula();
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    String dateStr = "";
                    int style = cell.getCellStyle().getDataFormat();
                    Date date = cell.getDateCellValue();
                    // 对不一样格式的日期类型作不一样的输出，与单元格格式保持一致
                    switch (style) {
                        case 178:
                            dateStr = new SimpleDateFormat("yyyy'年'M'月'd'日'").format(date);
                            break;
                        case 14:
                            dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            break;
                        case 179:
                            dateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
                            break;
                        case 181:
                            dateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm a ").format(date);
                            break;
                        case 22:
                            dateStr = new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss ").format(date);
                            break;
                        default:
                            break;
                    }
                    return dateStr;
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }

                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            default:
                return "";
        }
    }



    /**
     * 获取单元格及合并单元格的宽度
     * @param cell 单元格
     * @return 单元格及合并单元格的宽度（像素）
     */
    private static float getCellWidth(XSSFCell cell) {
        if(cell == null ) {
            return 0;
        }
        XSSFSheet sheet = cell.getSheet();
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        float width = sheet.getColumnWidthInPixels(columnIndex);
        boolean isPartOfRegion = false;
        int firstColumn = 0;
        int lastColumn = 0;
        int firstRow = 0;
        int lastRow = 0;
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            firstColumn = ca.getFirstColumn();
            lastColumn = ca.getLastColumn();
            firstRow = ca.getFirstRow();
            lastRow = ca.getLastRow();
            if (rowIndex == firstRow && rowIndex <= lastRow) {
                if (columnIndex == firstColumn && columnIndex <= lastColumn) {
                    isPartOfRegion = true;
                    break;
                }
            }
        }
        if(isPartOfRegion) {
            width = 0;
            for (int i = firstColumn; i <= lastColumn; i++) {
                width += sheet.getColumnWidthInPixels(i);
            }
        }
        return width;
    }
}
