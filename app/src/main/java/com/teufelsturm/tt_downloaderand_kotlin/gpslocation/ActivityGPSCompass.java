package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.util.Date;

public class ActivityGPSCompass extends Activity implements LocationListener {
	private final static String TAG = ActivityGPSCompass.class.getSimpleName();

	private static SensorManager sensorService;
	private static MyCompassView compassView;
	private static TT_Summit_AND aTT_Summit_AND;
	private static Sensor sensor;
	private static LocationManager locationManager;
	private static String provider;
	private static GeomagneticField geoField;
	private static Location myLocation;
	private static float myHeading;

	public static Location getMyLocation() {
		return myLocation;
	}

	public static float getMyHeading() {
		return myHeading;
	}

	private static Location targetLocation;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(getClass().getSimpleName(), "onCreate erreicht...");
		Intent intent = getIntent();
		aTT_Summit_AND = intent.getParcelableExtra("TT_Gipfel_AND");
		targetLocation = new Location("reverseGeocoded");
		targetLocation.setLatitude(aTT_Summit_AND.getDbl_GpsLat());
		targetLocation.setLongitude(aTT_Summit_AND.getDbl_GpsLong());
		targetLocation.setTime(new Date().getTime());

		Log.v(getClass().getSimpleName(), "targetLocation erreicht...");
		compassView = new MyCompassView(this, targetLocation, aTT_Summit_AND);
		setContentView(compassView);
		Log.v(getClass().getSimpleName(),
				"setContentView(compassView); erreicht...");
		// *********************************************************************
		// ********** COMPASS **************************************************
		sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (sensor != null) {
			sensorService.registerListener(mySensorEventListener, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			Log.i("ActivityGPSCompass",
					"Registerered for ORIENTATION Sensor");

		} else {
			Log.e("ActivityGPSCompass",
					"Registerered for ORIENTATION Sensor");
			Toast.makeText(this, "ORIENTATION Sensor not found",
					Toast.LENGTH_LONG).show();
			finish();
		}
		// *********************************************************************
		// ********** GPS ******************************************************
		geoField = null;
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locationManager.getBestProvider(criteria, false);
		Log.v(TAG, String.valueOf( provider ));
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(R.string.gps_disabled_message))
					.setCancelable(false)
					.setPositiveButton(this.getResources().getString(R.string.YES_ONLY)
							, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(intent);
								}
							})
					.setNegativeButton(this.getResources().getString(R.string.NO_ONLY)
							, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
			locationManager.requestLocationUpdates(provider, 0, 1, this);
			AlertDialog alert = builder.create();
			alert.show();
		}
		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		}
		compassView.invalidateLocation();
	}

	// *********************************************************************
	// ********** GPS ******************************************************
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locationManager.requestLocationUpdates(provider, 250, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
		// invalidate Location in MyCompassView
		compassView.invalidateLocation(); 
	}

	@Override
	public void onLocationChanged(Location location) {
		// ToDo: improve, see e.g. http://stackoverflow.com/questions/2021176/how-can-i-check-the-current-status-of-the-gps-receiver 
		myLocation = location; 
//		Log.v(getClass().getSimpleName(),
//					"onLocationChanged --> location.equals(null): " + location.equals(null));
		geoField = new GeomagneticField(Double.valueOf(location.getLatitude())
				.floatValue(), Double.valueOf(location.getLongitude())
				.floatValue(), Double.valueOf(location.getAltitude())
				.floatValue(), System.currentTimeMillis());
//		Log.v(getClass().getSimpleName(), "Rufe auf: compassView.updateData(location);");
		compassView.updateData(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	// ***********************************************************
	// ****** Compass *******************************************
	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float myBearing = 0f;
//			Log.v(getClass().getSimpleName(),
//					"geoField.equals(null): " + geoField.equals(null));
			// heading: your heading from the hardware compass.
			// This is in degrees east of magnetic north
			// angle between the magnetic north direction
			// 0=North, 90=East, 180=South, 270=West
			myHeading = event.values[0];
			if (geoField != null) {
				// First adjust your heading with the declination:
				myHeading += geoField.getDeclination();
				// bearing: the bearing from your location to the destination
				// location. This is in degrees east of true north.
				myBearing = myLocation.bearingTo(targetLocation);
//				Log.v(getClass().getSimpleName(), "myBearing: " + myBearing);
				// Second, you need to offset the direction in which the phone
				// is facing (heading) from the target destination rather than
				// true north.
				myBearing = myBearing - myHeading;
//				myHeading = myLocation.bearingTo(targetLocation) - ( event.values[0] + geoField.getDeclination() );
//				Log.v(getClass().getSimpleName(), "heading: " + heading);
			}
			compassView.updateData( -myBearing );
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sensor != null) {
			sensorService.unregisterListener(mySensorEventListener);
		}
	}
}