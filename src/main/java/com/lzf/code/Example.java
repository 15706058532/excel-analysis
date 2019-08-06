package com.lzf.code;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Objects;

public class Example {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject postRequestFromUrl(String url, String body) throws IOException, JSONException {
        URL realUrl = new URL(url);
        URLConnection conn = realUrl.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(body);
        out.flush();

        InputStream instream = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(instream, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            instream.close();
        }
    }

    public static JSONObject getRequestFromUrl(String url) throws IOException, JSONException {
        URL realUrl = new URL(url);
        URLConnection conn = realUrl.openConnection();
        InputStream inStream = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            inStream.close();
        }
    }

    /**
     * 服务号返回0  订阅号返回1 ，请求失败返回-1
     *
     * @param uid
     * @return
     */
    public static int handel(String uid) {
        for (int i = 0; i < 5; ) {
            try {
                // 请求示例 url 默认请求参数已经做URL编码
                String url = "http://api01.idataapi.cn:8000/post/weixin?apikey=IaWwyvKgGu04BSsnAHSdqurWros3OyIeaK2BkoEvZFp1rpjVWPZi7S5EFow85zav&uid=" + uid;
                JSONObject json = getRequestFromUrl(url);

                if (Objects.equals(json.getString("retcode"), "100301")) {
                    System.out.println("服务号================" + uid + "==========================");
                    System.out.println(json.toString());
                    return 0;
                } else {
                    System.out.println("订阅号================" + uid + "==========================");
                    System.out.println(json.toString());
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                i++;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws InterruptedException {
        String[] strings = {"cn_marykay", "sz_greenleaf", "hayao010"};
        for (String string : strings) {
            handel(string);
            Thread.sleep(300);
        }
    }
}