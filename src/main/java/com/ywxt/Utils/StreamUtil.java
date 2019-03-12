package com.ywxt.Utils;

import java.io.*;

public class StreamUtil {

    public static String convertStreamToString(InputStream inputStream) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "<n>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String str = sb.toString();
//        if(str.contains("<br/>")){
//            str = str.replace("\\n","");
//            str = str.replaceAll("<br/>","\\\\n");
//        }

        return str;
    }
}
