package com.drevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.drevent.domain.Event;
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
        
        eventsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
			
				launchEventDetailsActivity((Event) parent
						.getItemAtPosition(position));
			}
		});
    }
    
	private void launchEventDetailsActivity(Event event) {
		Intent i = new Intent(this, EventDetailsActivity.class);
		i.putExtra("event_id", event.getId());
		startActivity(i);
	}
}
