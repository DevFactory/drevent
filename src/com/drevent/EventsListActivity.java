package com.drevent;

import android.app.Activity;
import android.os.Bundle;

public class EventsListActivity  extends Activity {

	
	public static final String TAG = "EventsList";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
    }
}
