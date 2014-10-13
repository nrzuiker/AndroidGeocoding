package com.example.geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;
import android.os.AsyncTask;

public class AsyncMapRequest extends AsyncTask<String, Integer, JSONObject> {
	
	public JSONObject doInBackground(String ... strings) {
		try{
			URL url = new URL(strings[0]);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while(line != null) {
				builder.append(line);
				line = reader.readLine();
			}
			JSONObject response = new JSONObject(builder.toString());
			return response;			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
