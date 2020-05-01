package com.example.androidaplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpeechDataHandler extends AsyncTask<MapsActivity, Void, String> {
    @Override
    protected String doInBackground(MapsActivity... activity) {
        StringBuffer content = new StringBuffer();
        try {
            String urlAddress ="http://192.168.1.91:5000/quotes?latitude=47.10028&longitude=27.34438&radius=10";
            URL url = new URL(urlAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            Log.e("my_http",e.toString());
        }
        activity[0].setSpeechText(content.toString());
        Log.i("toSpeech",content.toString());

        return content.toString();
    }
}
