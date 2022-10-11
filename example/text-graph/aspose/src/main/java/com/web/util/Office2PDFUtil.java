package com.web.util;

import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.*;

public class Office2PDFUtil {

    public static void doc2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        Document doc = new Document(inputStream);
        doc.save(outputStream, SaveFormat.PDF);
    }

    public static void excel2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        Workbook wb = new Workbook(inputStream);
        wb.save(outputStream, SaveFormat.PDF);
    }

    public static void ppt2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        Presentation pres = new Presentation(inputStream);
        pres.save(outputStream, SaveFormat.PDF);
    }

}
