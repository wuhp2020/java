package com.web.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static <T> List<T> readExcel(MultipartFile excel, Class<? extends BaseRowModel> clazz,
                                        int sheetNo, int headLineMun) throws Exception {
        ExcelListener<T> excelListener = new ExcelListener<T>();
        String filename = excel.getOriginalFilename();

        if (filename == null || (!filename.toLowerCase().endsWith(".xls")
                && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new Exception("文件格式错误！");
        }

        try {
            InputStream inputStream = new BufferedInputStream(excel.getInputStream());
            ExcelReader reader = new ExcelReader(inputStream, null, excelListener, false);
            reader.read(new Sheet(sheetNo, headLineMun, clazz));
            return excelListener.getDatas();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readExcel(File file, Class<? extends BaseRowModel> clazz,
                                        int sheetNo, int headLineMun) throws Exception {
        ExcelListener<T> excelListener = new ExcelListener<T>();
        String filename = file.getName();

        if (filename == null || (!filename.toLowerCase().endsWith(".xls")
                && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new Exception("文件格式错误！");
        }

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            ExcelReader reader = new ExcelReader(inputStream, null, excelListener, false);
            reader.read(new Sheet(sheetNo, headLineMun, clazz));
            return excelListener.getDatas();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readExcel(InputStream inputStream, Class<? extends BaseRowModel> clazz,
                                        int sheetNo, int headLineMun) throws Exception {
        ExcelListener<T> excelListener = new ExcelListener<T>();

        try {
            ExcelReader reader = new ExcelReader(inputStream, null, excelListener, false);
            reader.read(new Sheet(sheetNo, headLineMun, clazz));
            return excelListener.getDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ExcelListener<T> extends AnalysisEventListener {

        private List<T> datas = new ArrayList<T>();

        @Override
        public void invoke(Object o, AnalysisContext analysisContext) {
            datas.add((T) o);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<T> getDatas() {
            return datas;
        }

        public void setDatas(List<T> datas) {
            this.datas = datas;
        }
    }
}
