package com.example.androidaplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpeechDataHandler extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... voids) {
        StringBuffer content = new StringBuffer();
        try {
            String urlAddress ="http://192.168.1.91:5000/quotes?latitude=47.165413&longitude=27.580620&radius=10";
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
            Log.e("http",e.toString());
        }
        return content.toString();
    }
}
