package com.stumbleapp.stumble;

/**
 * Created by Me on 22/03/2016.
 */

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public String makeHttpRequest(String s_url, String method, ContentValues values) {

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        String parameters = "";

        for (String key : values.keySet()) {
            String myKey = key;
            parameters = parameters + myKey +"=" +values.get(key)+"?";

        }
        Log.i("Params",parameters);
//        String _name = values.getAsString("name");
//        String _location = values.getAsString("location");
//        String _url = values.getAsString("url");

        URL url = null;
        String response = null;

        try {
            url = new URL(s_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod(method);

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            Log.i("response :", response);
            // You can perform UI operations here

            isr.close();
            reader.close();

        } catch (IOException e) {
            // Error
        }

        return response;
    }
}