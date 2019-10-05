package com.example.nikmul19.edubuddy;

import android.os.AsyncTask;

import java.io.OutputStreamWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ServerTest extends AsyncTask<String, Integer, Void> {
    public final static String AUTH_KEY_FCM = "AAAAUylprvo:APA91bFwC9-qtbSjVbRpKEus2VTqdIzzciDLR8_74nO80lBmHS0mNIH_yJ6Uppd3a22TiVokJfWp-Bzrm6WsJwTm-GcJVuHUR9YTK-TfFG33d5k3c0UuxIpKu9vlPDXrxA4Xqk3zqsCF";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    // userDeviceIdKey is the device id you will query from your database


    @Override
    protected Void doInBackground(String... strings) {

        try {

            String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
            String FMCurl = API_URL_FCM;
            String message = strings[2];
            String title = strings[1];
            String userDeviceIdKey = strings[0];

            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
            info.put("body", message); // Notification body
            info.put("type", "message");
            json.put("data", info);
            //System.out.println(json.toString());

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();
            conn.disconnect();

        } catch (Exception e) {
        }

        return null;
    }


}
