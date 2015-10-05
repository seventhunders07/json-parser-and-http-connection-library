package com.example.seven.jsonlibrary;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Seven on 10/5/2015.
 */
public class HttpConnect {

    private static HttpConnect firstInstance = null;

    private HttpConnect() {

    }

    static boolean firstThread = true;

    private static HttpConnect getFirstInstance() {

        if (firstInstance == null) {
            if(firstThread){
                firstThread = false;
                Thread.currentThread();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (HttpConnect.class){
                if(firstInstance == null){
                    firstInstance = new HttpConnect();
                }
            }


        }

        return firstInstance;

    }

    //Making Http Request Without Authorization Headers
    public String httpMakeRequest(String url, Map<String,String> param, String method){
       String json = null;
        List<NameValuePair> httpParams = new ArrayList<>();

        for(Map.Entry<String,String> entry: param.entrySet()){
            httpParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }

        try {
            URL connUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connUrl.openConnection();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getQuery(httpParams));
            writer.flush();
            writer.close();
            os.close();
            conn.setRequestMethod(method);
            conn.connect();
            Log.d("response code","Response Code: "+conn.getResponseCode());
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder builder = new StringBuilder();

            String inputStr;
            while ((inputStr = reader.readLine())!=null){
                builder.append(inputStr);
            }
            json = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException io){
            io.printStackTrace();
        }

        return json;
    }

    //Making Http Requests with Headers
    public String makeHttpRequest(String url, Map<String,String> param, Map<String, String> headers, String method){
        String json = null;
        List<NameValuePair> httpParams = new ArrayList<>();
        List<NameValuePair> httpHeaders = new ArrayList<>();

        for(Map.Entry<String,String> entry: param.entrySet()){
            httpParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        for(Map.Entry<String,String> entry: headers.entrySet()){
            httpHeaders.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }

        try {
            URL connUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connUrl.openConnection();
            for(int i = 0; i< httpHeaders.size(); i++){
                conn.addRequestProperty(httpParams.get(i).getName(), httpParams.get(i).getValue());
            }
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getQuery(httpParams));
            writer.flush();
            writer.close();
            os.close();
            conn.setRequestMethod(method);
            conn.connect();
            Log.d("response code","Response Code: "+conn.getResponseCode());
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder builder = new StringBuilder();

            String inputStr;
            while ((inputStr = reader.readLine())!=null){
                builder.append(inputStr);
            }
            json = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException io){
            io.printStackTrace();
        }

        return json;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


}
