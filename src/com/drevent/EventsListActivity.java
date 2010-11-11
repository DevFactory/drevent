package com.drevent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.drevent.net.FacebookHttpClient;

public class EventsListActivity  extends Activity {

	public static final String TAG = "EventsList";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        EntryItemAdapter mAdapter = new EntryItemAdapter(this,
				R.layout.event_item, FacebookHttpClient.cachedData);
        
        ListView eventsListView = (ListView) findViewById(R.id.events_list);
        eventsListView.setAdapter(mAdapter);
    }
}
