package com.meetme.core.map;

import com.google.android.gms.maps.model.Marker;

public abstract class MarkerUtils {

	private MarkerUtils() {
	}
	
	/*
	 * Methods
	 */
	public static String getLocationTextFromMarker(Marker marker) {
		return marker.getTitle();
	}
	
	public static String getLocationGeoFromMarker(Marker marker) {
		StringBuilder locationGeo = new StringBuilder();
				
		locationGeo.append(marker.getPosition().latitude);
		locationGeo.append(',');
		locationGeo.append(marker.getPosition().longitude);
		
		return locationGeo.toString();
	}
}
