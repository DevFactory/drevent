package com.drevent;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drevent.domain.Event;
import com.drevent.net.FacebookHttpClient;
import com.drevent.net.HttpClient;

public class DreventActivity extends Activity {

	public static final String TAG = "Drevent";

	private HttpClient httpClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button findBtn = (Button) findViewById(R.id.find_btn);
		findBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText query = (EditText) findViewById(R.id.query);
				new SearchEventsTask().execute(query.getText().toString());
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		FacebookHttpClient.cachedData.clear();
	}

	private class SearchEventsTask extends
			AsyncTask<String, ArrayList<Event>, ArrayList<Event>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			// start progress dialog
			progressDialog = ProgressDialog.show(DreventActivity.this, "",
					getString(R.string.searching_events_progress_label), true);
		}

		@Override
		protected ArrayList<Event> doInBackground(String... arg0) {
			Log.d(TAG, "Loading events.");
			//Looper.prepare();
			new FacebookHttpClient(getApplicationContext()).events(arg0[0]);
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Event> result) {
			progressDialog.dismiss();

			Log.d(TAG, "Events have been loaded. Task finished!");
			
			if (FacebookHttpClient.cachedData.size() > 0) {
				Intent i = new Intent(DreventActivity.this,
						EventsListActivity.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(),
						"Nothing found. Try again later.", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
}