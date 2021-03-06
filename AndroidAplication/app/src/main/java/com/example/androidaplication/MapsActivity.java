package com.example.androidaplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private static final String PLATFORM_CHANNEL = "speechDataChannel";
    private String SpeechText = "";
    private Marker locationMarker;
    private String textTitle = "-";
    private String textAuthor = "-";
    private String textLocation = "-";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            location = locationManager.getLastKnownLocation(provider);
            Log.i("provider",provider);
            if (location != null) {
                break;
            }
        }
        Log.i("location",String.valueOf(location));
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void speak(){
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault());
        FlutterEngineCache.getInstance().put("my_engine_id",flutterEngine);
        startActivity(FlutterActivity.withCachedEngine("my_engine_id").build(this));

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), PLATFORM_CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                if (call.method.equals("getSpeechText")){
                    String speechText = getSpeechText();
                    result.success(speechText);
                }
                if (call.method.equals("getTextTitle")){
                    String textTitle = getTextTitle();
                    Log.i("textTitle",textTitle);
                    result.success(textTitle);
                }
                if (call.method.equals("getTextAuthor")){
                    String textAuthor = getTextAuthor();
                    result.success(textAuthor);
                }
                if (call.method.equals("getTextLocation")){
                    String textLocation = getTextlocation();
                    result.success(textLocation);
                }
            }
        });
    }

    private String getTextlocation() {
        return textLocation;
    }

    private String getTextAuthor() {
        return textAuthor;
    }

    private String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public void setTextAuthor(String textAuthor) {
        this.textAuthor = textAuthor;
    }

    public void setTextLocation(String textLocation) {
        this.textLocation = textLocation;
    }


    private void updateSpeachText(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
        new SpeechDataHandler().execute(this);
        speak();
    }

    private String getSpeechText() {
        return this.SpeechText;
    }

    public void setSpeechText(String speechText) {
        SpeechText = speechText;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
        updateSpeachText();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location","onLocationChanged");
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.locationMarker.setPosition(new LatLng(latitude,longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(longitude, latitude), 12.0f));
        updateSpeachText();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
