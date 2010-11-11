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

import android.util.Log;

import com.drevent.domain.Event;
import com.drevent.domain.Location;

/**
 * @author Izzet_Mustafayev
 * 
 */
public class FacebookHttpClient implements HttpClient {

	private static final String SCHEMA = "https";

	private static final int PORT = 443;
	/**
	 * The Facebook host to query.
	 */
	private static final String FACEBOOK_HOST = "graph.facebook.com";

	private static final String FACEBOOK_SEARCH_PATH = "/search";

	public static ArrayList<Event> cachedData = new ArrayList<Event>();
	
	/**
	 * Requests to Faceboook API to et events for specified location.
	 */
	public ArrayList<Event> events(final String event, final Location location) {
		// clear
		cachedData.clear();
		
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", event));
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
		
		// cache
		cachedData = events;
		
		return events;
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
		return event;
	}
}
