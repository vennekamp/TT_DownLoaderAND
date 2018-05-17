package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.location.Location;

public class SummitLocation implements Comparable<SummitLocation>{
	private String strName;
	private Location summitLocation;
	private float myDistanceTo;
	public SummitLocation(String strName, double dblGPS_Latitude, double dblGPS_Longitude){
		this.strName = strName;
		this.summitLocation = new Location("reverseGeocoded");
		this.summitLocation.setLatitude(dblGPS_Latitude);
		this.summitLocation.setLongitude(dblGPS_Longitude);
	}
	public String getName() {
		return strName;
	}
	public void setName(String name) {
		strName = name;
	}
	public Location getLocation() {
		return summitLocation;
	}
	public void setLocation(Location location) {
		this.summitLocation = location;
	}
	public float getDistanceTo() {
		return myDistanceTo;
	}
	public float getBearingTo() {
		// Bearing here to the North pole minus the one of this summit in degrees East of true North
		return ActivityGPSCompass.getMyHeading() 
				- ActivityGPSCompass.getMyLocation().bearingTo(this.summitLocation);
	}
	public void updateData(){
		// Distance to here in meters
		myDistanceTo = ActivityGPSCompass.getMyLocation().distanceTo(this.summitLocation);
//		Log.v(getClass().getSimpleName(), "Positionsdaten zu Gipfel: " + strName);
//		Log.v(getClass().getSimpleName(), "\tmyDistanceTo: " + myDistanceTo);
//		Log.v(getClass().getSimpleName(), "\tmyBearingTo: " + myBearingTo);
	}
	@Override
	public int compareTo(SummitLocation another) {
		return (int) (100 * (this.getDistanceTo() - another.getDistanceTo()));
	}
}