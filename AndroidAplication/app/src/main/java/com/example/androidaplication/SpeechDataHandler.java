package com.example.androidaplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
        Log.i("jsonObject",content.toString());
        try {
            JSONObject jsonObject = new JSONObject(content.toString());
            String text = jsonObject.getString("quote");
            String author = jsonObject.getString("author");
            String title = jsonObject.getString("title");
            String location = jsonObject.getString("place");
            activity[0].setSpeechText(text);
            activity[0].setTextAuthor(author);
            activity[0].setTextLocation(location);
            activity[0].setTextTitle(title);
            Log.i("toSpeech",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
