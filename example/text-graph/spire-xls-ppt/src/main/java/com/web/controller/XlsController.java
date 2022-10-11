package com.web.controller;

import com.spire.xls.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/v1/xls")
@Api(tags = "excel文件")
@Slf4j
public class XlsController {

    @GetMapping("/view")
    @ApiOperation(value = "预览")
    public void view(HttpServletResponse response) throws Exception {

        // 输出格式
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=a.pdf");

        // 获取本地文件或网络文件
        Resource resource = new ClassPathResource("1.xlsx");
        InputStream inputStream = resource.getInputStream();
        OutputStream outputStream = response.getOutputStream();

        // xls或xlsx转pdf
        // 加载excel表格
        Workbook workbook = new Workbook();
        workbook.loadFromStream(inputStream);
        // 获取第一个工作表
        Worksheet worksheet = workbook.getWorksheets().get(0);
        // 删除多余字段
        worksheet.deleteColumn(1,2);
        worksheet.deleteColumn(5,2);
        // 将含有Y放到上方
        workbook.getDataSorter().getSortColumns().add(1, SortComparsionType.Values, OrderBy.Top);
        workbook.getDataSorter().getSortColumns().add(2, SortComparsionType.Values, OrderBy.Ascending);

        //指定需要排序的列索引以及排序的方式(基于单元格的值)
        //获取有数据的区域
        CellRange dataRange =worksheet.getAllocatedRange();
        //使用条件格式将数据为Y的背景色设为绿色
        ConditionalFormatWrapper format1 = dataRange.getConditionalFormats().addCondition();
        format1.setFormatType(ConditionalFormatType.Formula);
        format1.setFirstFormula("=IF($B1=\"Y\",true,false)");
        format1.setBackColor(Color.green);
        //使用条件格式将数据为N有编号的背景色设为黄色
        ConditionalFormatWrapper format2 = dataRange.getConditionalFormats().addCondition();
        format2.setFormatType(ConditionalFormatType.Formula);
        format2.setFirstFormula("=AND(IF($A1=\"\",false,true),IF($B1=\"N\",true,false))");
        format2.setBackColor(Color.yellow);
        //使用条件格式将数据为N无编号的背景色设为红色
        ConditionalFormatWrapper format3 = dataRange.getConditionalFormats().addCondition();
        format3.setFormatType(ConditionalFormatType.Formula);
        format3.setFirstFormula("=AND(IF($A1=\"\",true,false),IF($B1=\"N\",true,false))");
        format3.setBackColor(Color.pink);
        //排序是否包含标题(默认第一个数据为标题,不会对它进行排序)
        workbook.getDataSorter().isIncludeTitle(true);
        //指定要排序的单元格范围并进行排序
        workbook.getDataSorter().sort(worksheet.getRange());
        //设置转换后的PDGF页面高宽适应工作表的内容大小
        workbook.getConverterSetting().setSheetFitToPage(true);
        //设置转换后PDF的页面宽度适应工作表的内容宽度
        workbook.getConverterSetting().setSheetFitToWidth(true);
        //复制workbook
        //遍历所有行
        int count = 0;
        int lastRow = worksheet.getLastRow();
        for (int i =1;i<=lastRow;i++) {
            //判断行是否空白
            if(worksheet.getCellRange(i,1).isBlank()){
                count++;
                CellRange cellRange = worksheet.getCellRange(i, 1, i, worksheet.getLastColumn());
                CellRange range = worksheet.getCellRange(worksheet.getLastRow() + 1, 1, worksheet.getLastRow() + 1, worksheet.getLastColumn());
                worksheet.copy(cellRange,range,true);
            }
        }
        for (int i = worksheet.getLastRow()-count; i > 0; i--) {
            if(worksheet.getCellRange(i,1).isBlank()){
                //删除指定行
                worksheet.deleteRow(i);
            }
        }
        //将生成的文档保存到指定路径
        workbook.saveToStream(outputStream, FileFormat.PDF);

        inputStream.close();
        outputStream.close();
    }
}
