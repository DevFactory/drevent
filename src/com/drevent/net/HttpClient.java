/**
 * 
 */
package com.drevent.net;

import java.util.ArrayList;

import com.drevent.domain.Event;
import com.drevent.domain.Location;

/**
 * @author Izzet_Mustafayev
 *
 */
public interface HttpClient {

	ArrayList<Event> events(String event, Location location);
}
