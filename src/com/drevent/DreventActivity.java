package com.drevent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.drevent.net.FacebookHttpClient;

public class DreventActivity extends Activity {

	public static final String TAG = "Drevent";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button findBtn = (Button) findViewById(R.id.find_btn);
		findBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new SearchEventsTask().execute();
			}
		});
	}

	private class SearchEventsTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			// start progress dialog
			progressDialog = ProgressDialog.show(DreventActivity.this, "",
					getString(R.string.searching_events_progress_label), true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d(TAG, "Loading events.");
			Looper.prepare();
			new FacebookHttpClient(getApplicationContext()).events("");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			Log.d(TAG, "Events have been loaded. Task finished!");
			// Intent i = new Intent(this, TopRingtones.class);
			// startActivity(i);
		}
	};
}