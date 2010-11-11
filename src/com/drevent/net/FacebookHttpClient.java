/**
 * 
 */
package com.drevent.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.drevent.domain.Event;

/**
 * @author Izzet_Mustafayev
 * 
 */
public class FacebookHttpClient implements HttpClient, LocationListener {

	private static final String SCHEMA = "https";

	private static final int PORT = 443;
	/**
	 * The Facebook host to query.
	 */
	private static final String FACEBOOK_HOST = "graph.facebook.com";

	private static final String FACEBOOK_SEARCH_PATH = "/search";

	private Context context;

	private ArrayList<Event> events;

	public FacebookHttpClient(final Context context) {
		this.context = context;
	}

	/**
	 * Requests to Faceboook API to et events for specified location.
	 */
	public void events(final String event) {
		events = new ArrayList<Event>();

		String eventName = event;
		if (null == eventName || "".equals(eventName)) {
			LocationManager locationManager;
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);

		} else {
			requestEvents(eventName, events);
		}

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (null != provider) {

		}
	}

	public void onProviderEnabled(String provider) {
		if (null != provider) {

		}
	}

	public void onProviderDisabled(String provider) {
		if (null != provider) {

		}

	}

	public void onLocationChanged(android.location.Location location) {

		List<Address> addresses;
		try {
			addresses = new Geocoder(context).getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);
			Address address = addresses.get(0);
			requestEvents(address.getLocality(), events);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("http", e.getMessage());
		}

	}

	/**
	 * @param eventName
	 * @param events
	 */
	private void requestEvents(String eventName, ArrayList<Event> events) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", eventName));
		params.add(new BasicNameValuePair("type", "event"));
		params.add(new BasicNameValuePair("center", "50.43,30.27"));
		params.add(new BasicNameValuePair("distance", "1000"));
		try {
			HttpGet get = new HttpGet(URIUtils.createURI(SCHEMA, FACEBOOK_HOST,
					PORT, FACEBOOK_SEARCH_PATH,
					URLEncodedUtils.format(params, "UTF-8"), null));
			get.setHeader("Accept-Charset", "utf-8");
			// send request
			HttpResponse response = new DefaultHttpClient().execute(get);
			HttpEntity entity = response.getEntity();
			if (null == entity) {
				Log.d("http", "Received entity is null");
			}
			if (null != entity) {
				events.addAll(parseJsonEvents(entity.getContent()));
			}
		} catch (URISyntaxException exc) {
			// TODO do something with exception
			Log.e("http", exc.getMessage());
			exc.printStackTrace();
		} catch (ClientProtocolException exc) {
			// TODO do something with exception
			Log.e("http", exc.getMessage());
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO do something with exception
			Log.e("http", exc.getMessage());
			exc.printStackTrace();
		}
	}

	private ArrayList<Event> parseJsonEvents(final InputStream stream)
			throws IOException {
		ArrayList<Event> events = new ArrayList<Event>();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();

		String line = null;

		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		stream.close();

		try {
			JSONObject object = new JSONObject(sb.toString());
			JSONArray jsonEventList = object.getJSONArray("data");
			if (null != jsonEventList) {
				for (int i = 0; i < jsonEventList.length(); i++) {
					JSONObject eventObj = jsonEventList.getJSONObject(i);
					Event event = createEventFromJson(eventObj);
					events.add(event);
				}
			}
		} catch (JSONException e) {
			Log.d("http", e.getMessage());
		}
		return events;
	}

	/**
	 * @param eventObj
	 * @return
	 * @throws JSONException
	 */
	private Event createEventFromJson(final JSONObject eventObj)
			throws JSONException {
		Event event = new Event();
		event.setId(eventObj.getString("id"));
		event.setName(eventObj.getString("name"));
		event.setLocationName(eventObj.getString("location"));
		event.setStartTime(eventObj.getString("start_time"));
		event.setEndTime(eventObj.getString("end_time"));
		event.setDescription(eventObj.getString("description"));
		return event;
	}
}
