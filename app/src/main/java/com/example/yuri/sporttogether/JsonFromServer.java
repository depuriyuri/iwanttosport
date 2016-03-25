package com.example.yuri.sporttogether;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yuri on 27-6-2015.
 */
public class JsonFromServer {
    public static JSONArray getJSON(String url) {
        String result="";
        JSONArray jArray = null;
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            // conn.setConnectTimeout(timeout);
            // conn.setReadTimeout(timeout);
            conn.connect();
            int status = conn.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    result = sb.toString();
                   // Log.v("result", result);
                    //return sb.toString();
                   // return result;
            }
        } catch (MalformedURLException ex) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          //  Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
             //       Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try{
            jArray = new JSONArray(result);
           // Gson gson = new Gson();
           // jArray=gson.toJson(result);
        }catch(JSONException e){
            Log.e("JSON exception", e.toString());
        }
        Log.i("result2", result);
        return jArray;
    }

}
/*String data = getJSON("http://192.168.178.18:8080/SportTogetherBackend/GiveJSONah");

Gson gson = new Gson();

Lobby[]items = new Gson().fromJson(data, Lobby[].class);
  String string_array[] = new String[items.length];
                for(int i=0;i<string_array.length;i++){
                    string_array[i].toString();
                    Log.v("vvvv", string_array[i].toString());
                }
//Lobby lb = new Gson().fromJson(data, Lobby.class);
for (Lobby lb : items) {
        //     Lobby l = new Lobby(lb.getSport(), lb.getDifficulty(), lb.getLocation(), lb.getTime(), lb.getMaxparticipants(), lb.getMaxparticipants());
        Log.v("tag", lb.getSport());
        }

        try {
        JSONArray jArray = new JSONArray(data);

        if (jArray != null) {
        for (int i = 0; i < jArray.length(); i++) {
        //   listdata.add(jArray.getJSONObject(i).toString());
        listdata.add(jArray.getJSONObject(i).toString());

        }
        }
        } catch (JSONException e) {
        Log.v("jsonexception", "exception");
        }

        Log.v("JSONdata", data);
        for (String m : listdata) {
        Log.v("mmmmm", m);
        }
Log.v("this is my array", "arr: " + Arrays.toString(datalist));*/
