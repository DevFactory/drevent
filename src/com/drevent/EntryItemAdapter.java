package com.drevent;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drevent.domain.Event;

public class EntryItemAdapter extends ArrayAdapter<Event> {

	private int mResource;

	public EntryItemAdapter(Context context, int resource, ArrayList<Event> items) {
		super(context, resource, items);
		mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout entryView;

		if (convertView == null) {
			entryView = new LinearLayout(getContext());
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			li.inflate(mResource, entryView, true);
		} else {
			entryView = (LinearLayout) convertView;
		}

		Event entry = (Event) getItem(position);
		Log.d("Adapter", entry.toString());
		if (entry.getName() != null) {
			((TextView)entryView.findViewById(R.id.event_entry_name)).setText(entry.getName());
		}
		if (entry.getLocationName() != null) {
			((TextView)entryView.findViewById(R.id.event_entry_location)).setText(entry.getLocationName());
		}
		if (entry.getStartTime() != null) {
			((TextView)entryView.findViewById(R.id.event_entry_start_time)).setText("Starts: " + entry.getStartTime());
		}
		if (entry.getEndTime() != null) {
			((TextView)entryView.findViewById(R.id.event_entry_end_time)).setText("Ends: " + entry.getEndTime());
		}

		return entryView;
	}
}
