package com.vakinc.testapp;

import androidx.annotation.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class RequestHandler {

    String sendPost(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;
        StringBuilder sb = new StringBuilder();

        try{
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

            conn.disconnect();
        }
        catch (Exception e) {
        e.printStackTrace();
        }
        return sb.toString();
    }

    String sendGet(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            conn.disconnect();
        } catch (Exception e) {
        }
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
