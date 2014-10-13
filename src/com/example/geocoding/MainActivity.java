package com.example.geocoding;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	GoogleMap map;
	LatLng userLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
	}

	public void submitInfo(View v) {
		EditText input = (EditText) this.findViewById(R.id.input);
		String inputString = input.getText().toString();
		String requestString = formatRequest(inputString);
		JSONObject response;
		try {
			response = new AsyncMapRequest().execute(requestString).get();
			if(!response.getString("status").equals("OK"))
				return;
			JSONObject results = response.getJSONArray("results").getJSONObject(0);
			String fullAddress = results.getString("formatted_address");
			JSONObject location = results.getJSONObject("geometry").getJSONObject("location");
			LatLng position = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
			String locStr = "Latitude: " + location.getString("lat") + " Longitude: " + location.getString("lng");
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
			map.addMarker(new MarkerOptions()
            .title("Result")
            .snippet(fullAddress)
            .position(position))
            .showInfoWindow();		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void standardMap(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}
	
	public void hybridMap(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	}
	
	public void terrainMap(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	}

	public String formatRequest(String input) {
		String out = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String[] items = input.split(" ");
		for (int i = 0; i < items.length; i++) {
			if (i < items.length - 1) {
				out += items[i] + "+";
			} else {
				out += items[i];
			}
		}
		out += "&key=AIzaSyA0OtETiETySDye_6lYiIkY6VG6yiWkNWg";
		return out;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
