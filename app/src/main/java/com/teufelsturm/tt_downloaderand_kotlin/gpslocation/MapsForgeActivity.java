//package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;
//
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.teufelsturm.R;
//import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchSummit;
//import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;
//
//import org.mapsforge.android.maps.MapActivity;
//import org.mapsforge.android.maps.MapController;
//import org.mapsforge.android.maps.MapScaleBar;
//import org.mapsforge.android.maps.MapView;
//import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
//import org.mapsforge.android.maps.overlay.OverlayItem;
//import org.mapsforge.core.GeoPoint;
//import org.mapsforge.map.reader.header.FileOpenResult;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class MapsForgeActivity extends MapActivity {
//	private File MAP_FILE;
//	private TT_Summit_AND aTT_Summit_AND;
//	private MapController mapController;
//	private MapScaleBar mapScaleBar;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		constructor();
//	}
//
//	private void constructor() {
//		Log.v(getClass().getSimpleName(), "Rufe copyMapFile() auf");
//		checkOrCopyMapFile();
//		Log.v(getClass().getSimpleName(), "Rufe getIntent(); auf");
//		// Find the packet with the 'parent' object
//		Intent intent = getIntent();
//		aTT_Summit_AND = intent.getParcelableExtra("TT_Gipfel_AND");
//		Log.v(getClass().getSimpleName(), "Rufe new MapView(this); auf");
//		MapView mapView = new MapView(this);
//		mapView.setClickable(true);
//		mapView.setBuiltInZoomControls(true);
//		FileOpenResult fileOpenResult = mapView.setMapFile(MAP_FILE);
//		if (!fileOpenResult.isSuccess()) {
//			Toast.makeText(this, fileOpenResult.getErrorMessage(),
//					Toast.LENGTH_LONG).show();
//			finish();
//		}
//		// Text size:
//		mapView.setTextScale(2.0f);
//		// set initial zoom-level, depends on your need
//		mapController = mapView.getController();
//		mapController.setZoom(16);
//		// set ScaleBar
//		mapScaleBar = mapView.getMapScaleBar();
//		mapScaleBar.setImperialUnits(false);
//		mapScaleBar.setShowMapScaleBar(true);
//		// This point is in the "Saxony Swiss", Saxonia, Germany on the Summit.
//		GeoPoint aGeoPoint = new GeoPoint(aTT_Summit_AND.getDbl_GpsLat(),
//				aTT_Summit_AND.getDbl_GpsLong());
//		mapController.setCenter(aGeoPoint);
//		// ****************************************************************
//		// ********** This is the needle marker of the Target **********
//		// create a default marker for the overlay
//		Drawable defaultMarker = getResources().getDrawable(
//				R.drawable.location_pin);
//		// create an ItemizedOverlay with the default marker
//		ArrayItemizedOverlay itemizedOverlay = new ArrayItemizedOverlay(
//				defaultMarker);
//		OverlayItem itemOVL = new OverlayItem(aGeoPoint,
//				aTT_Summit_AND.getStr_SummitName(), aTT_Summit_AND
//						.getInt_TTGipfelNr().toString());
//		itemizedOverlay.addItem(itemOVL);
//		// add the ArrayItemizedOverlay to the MapView
//		mapView.getOverlays().add(itemizedOverlay);
//		setContentView(mapView);
//		mapView.getOverlays().add(itemizedOverlay);
//		// not working with current mapsforge Version - should be working with
//		// 0.4
//		// MyLocationOverlay myLocationOverlay = new MyLocationOverlay( );
//		// ****************************************************************
//		// ********** This might be the Text and Arrow to the Target ***
//		// rr = new RelativeLayout(this);
//		//
//		// Log.i(getClass().getSimpleName(),"0");
//		// TextView aTextView = new TextView(this);
//		// lp = new RelativeLayout.LayoutParams
//		// (LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		// lp.addRule(RelativeLayout.ALIGN_TOP );
//		// Log.i(getClass().getSimpleName(),"1");
//		// aTextView.setText("Time: "+System.currentTimeMillis());
//		// Log.i(getClass().getSimpleName(),"2");
//		// mapView.addView(aTextView,999999999);
//	}
//
//	/**
//	 * Copies the map-database from the assets to on the file system
//	 * */
//	public void checkOrCopyMapFile() {
//		// Check the installation date of this app
//		PackageManager pm = this.getPackageManager();
//		long installed;
//		Log.v(getClass().getSimpleName(), "Führe copyMapFile() aus 1");
//		try {
//			ApplicationInfo appInfo = pm.getApplicationInfo(
//					"com.teufelsturm.TT_DownLoaderAND", 0);
//			String appFile = appInfo.sourceDir;
//			installed = new File(appFile).lastModified();
//		} catch (NameNotFoundException e) {
//			installed = Long.MAX_VALUE;
//			Log.v(getClass().getSimpleName(), "Führe copyMapFile() aus --> Exception:" + e.getMessage());
//		}
//
//		MAP_FILE = new File(this.getFilesDir().getAbsolutePath(),
//				MainActivitySearchSummit.MAP_NAME);
//		// write MAP-File if not existing or newer MAP Version exists
//		if (MAP_FILE == null || !MAP_FILE.exists() || MAP_FILE.lastModified() < installed) {
//			try {
//				Log.v(getClass().getSimpleName(), "Führe copyMapFile() aus 2");
//				copyMapFile();
//			} catch (IOException e) {
//				throw new Error("Error copying Map File");
//
//			}
//		}
//	}
//
//	/**
//	 * Copies your MAP File from your local assets-folder to the system folder,
//	 * from where it can be accessed and handled. This is done by transfering
//	 * bytestream.
//	 * */
//	private void copyMapFile() throws IOException {
//
//		// Open your local db as the input stream
//		InputStream myInput = this.getAssets().open(
//				MainActivitySearchSummit.MAP_NAME);
//
//		// Path to the just created empty db
//		String outFileName = MAP_FILE.getAbsolutePath();
//
//		// Open the empty db as the output stream
//		OutputStream myOutput = new FileOutputStream(outFileName);
//
//		// transfer bytes from the inputfile to the outputfile
//		byte[] buffer = new byte[1024];
//		int length;
//		while ((length = myInput.read(buffer)) > 0) {
//			myOutput.write(buffer, 0, length);
//		}
//
//		// Close the streams
//		myOutput.flush();
//		myOutput.close();
//		myInput.close();
//		Log.v(getClass().getSimpleName(), "copyMapFile() durch geführt.");
//
//	}
//
//}