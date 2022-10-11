package com.web.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFUtil {

    public static InputStream netFileUrlToStream(String netFileUrl) throws Exception {
        URL url = new URL(netFileUrl);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();
        InputStream is = httpUrl.getInputStream();
        httpUrl.disconnect();
        return is;
    }
}
