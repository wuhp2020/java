package com.web.util;

import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class DocUtil {
    public static void main(String[] args) {
        WordprocessingMLPackage wordprocessingMLPackage = new WordprocessingMLPackage();
        PresentationMLPackage presentationMLPackage = new PresentationMLPackage();
        SpreadsheetMLPackage spreadsheetMLPackage = new SpreadsheetMLPackage();
    }
}
