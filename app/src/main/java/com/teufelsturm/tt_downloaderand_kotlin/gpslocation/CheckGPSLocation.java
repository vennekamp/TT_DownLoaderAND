package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.location.Location;

public class CheckGPSLocation {
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private static final int FIVE_MINUTES = 1000 * 60 * 5;

	/**
	 * Determines whether one Location reading is newer than 30min.
	 * @param location The new Location that you want to evaluate
	 * @return true, if the Location is newer than 5min.
	 */
	protected static boolean isQuiteNewLocation(Location location){
		long timeDelta = location.getTime() - location.getTime();
		return timeDelta < FIVE_MINUTES;
		}
	/**
	 * Determines whether one Location is identical to another Location regarding Latitude and Longitude
	 * @param location The new Location that you want to compare
	 * @param currentBestLocation The current Location fix, to which you want to compare the new one
	 * @return true, if the Latitude and longitude of the Locations are identical.
	 */
	protected static boolean equalsLocation(Location location, Location currentBestLocation) {
		if ( location.getLatitude() == currentBestLocation.getLatitude()
				&& location.getLongitude() == currentBestLocation.getLongitude() )
			return true;
		return false;
	}
	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
}
