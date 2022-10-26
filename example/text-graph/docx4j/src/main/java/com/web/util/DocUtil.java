package com.web.util;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.*;
import java.util.Map;

public class DocUtil {

    /**
     * 不能替换问题解决方案:
     * 把要写的占位符先写到记事本中, 然后再复制到word中, 就可以替换了
     * @param inputStream
     * @param data
     * @return
     * @throws Exception
     */
    public OutputStream wordFillIn(InputStream inputStream, Map<String, String> data) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
        VariablePrepare.prepare(wordMLPackage);
        documentPart.variableReplace(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wordMLPackage.save(outputStream);
        return outputStream;
    }

    /**
     * 不能替换问题解决方案:
     * 把要写的占位符先写到记事本中, 然后再复制到word中, 就可以替换了
     * @param inputPath
     * @param outputPath
     * @param data
     * @throws Exception
     */
    public void wordFillIn(String inputPath, String outputPath, Map<String, String> data) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(inputPath));
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
        VariablePrepare.prepare(wordMLPackage);
        documentPart.variableReplace(data);
        OutputStream outputStream = new FileOutputStream(new File(outputPath));
        wordMLPackage.save(outputStream);
        outputStream.close();
    }
}
