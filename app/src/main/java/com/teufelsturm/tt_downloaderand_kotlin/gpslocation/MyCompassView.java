package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyCompassView extends View {
	private static final int NUMBER_OF_ADJACENTSUMMIT = 4;
	private Paint paint;
	private float heading = 0;
	private Location currentLocation;
	private Location targetLocation;
	private DecimalFormat decimalFormat;
	private SimpleDateFormat sdf;
	private TT_Summit_AND aTT_Summit_AND = null;
	private int[] arrColors = new int[NUMBER_OF_ADJACENTSUMMIT];

	public MyCompassView(Context context) {
		super(context);
		this.targetLocation = null;
		init();
	}

	public MyCompassView(Context context, Location targetLocation,
			TT_Summit_AND aTT_Summit_AND) {
		super(context);
		this.targetLocation = targetLocation;
		this.aTT_Summit_AND = aTT_Summit_AND;
		init();
		FindAdjacentSummits.loadSummitLocationData(context);
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setTextSize(25);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		arrColors[0] = Color.RED;
		arrColors[1] = Color.BLUE;
		arrColors[2] = Color.YELLOW;
		arrColors[3] = Color.GREEN;
		currentLocation = null;

		decimalFormat = new DecimalFormat(",###");
		sdf = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);

		Log.v(getClass().getSimpleName(), "Init abgeschlossen!");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int xPoint = getMeasuredWidth() / 2;
		int yPoint = getMeasuredHeight() / 2;

		float radius = (float) (Math.min(xPoint, yPoint) * 0.8);
		canvas.drawCircle(xPoint, yPoint, radius, paint);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

		// Log.v(getClass().getSimpleName(), "onDraw --> 1");
		if (this.currentLocation != null) {

			// Handling the search for the neighboring summit
			for (int intSumLoc = 0; intSumLoc < NUMBER_OF_ADJACENTSUMMIT; intSumLoc++) {
				// Change Color
				paint.setColor(arrColors[intSumLoc]);
				// Draw summit name & distance
				canvas.drawText(
						FindAdjacentSummits.getAllSummitLocations()
								.get(intSumLoc).getName()
								+ ": "
								+ decimalFormat.format(FindAdjacentSummits
										.getAllSummitLocations().get(intSumLoc)
										.getDistanceTo()) + "m", 10,
						100 + (25 * intSumLoc), paint);
				// Draw arrow to summit
				canvas.drawLine(
						xPoint,
						yPoint,
						(float) (xPoint + radius
								* Math.sin((double) (-FindAdjacentSummits
										.getAllSummitLocations().get(intSumLoc)
										.getBearingTo()) / 180 * 3.143)),
						(float) (yPoint - radius
								* Math.cos((double) (-FindAdjacentSummits
										.getAllSummitLocations().get(intSumLoc)
										.getBearingTo()) / 180 * 3.143)), paint);
			}
			paint.setColor(Color.WHITE);

			// Handling the distance and the heading to the searched summit
			canvas.drawLine(
					xPoint,
					yPoint,
					(float) (xPoint + radius
							* Math.sin((double) (-heading) / 180 * 3.143)),
					(float) (yPoint - radius
							* Math.cos((double) (-heading) / 180 * 3.143)),
					paint);
			String aString = "\nRichtung:\n"
					+ decimalFormat.format(
							(heading < 0) ? -heading : 360 - heading )
					+ " °\n"
					+ "Entfernung:\n"
					+ decimalFormat.format(currentLocation
							.distanceTo(targetLocation)) + "m\n"
					+ "Positionsbestimmung\n von: "
					+ sdf.format(currentLocation.getTime());
			yPoint = drawMulitiLineText(aString, xPoint, yPoint, canvas);		} else {
			drawMulitiLineText("\nno GPS Location\n", xPoint, yPoint, canvas);
		}

		canvas.drawText("Gipfel: " + aTT_Summit_AND.getStr_SummitName() + " ("
				+ aTT_Summit_AND.getInt_SummitNumberOfficial() + ")", 10, 25,
				paint);
		canvas.drawText("GPS:", 10, 50, paint);
		canvas.drawText(
				Location.convert(aTT_Summit_AND.getDbl_GpsLat(),
						Location.FORMAT_DEGREES)
						+ " N; "
						+ Location.convert(aTT_Summit_AND.getDbl_GpsLong(),
								Location.FORMAT_DEGREES) + " O", 10, 75, paint);
	}

	private int drawMulitiLineText(String aString, int xPoint, int yPoint,
			Canvas canvas) {
		for (String line : aString.split("\n")) {
			canvas.drawText(line, xPoint, yPoint, paint);
			yPoint -= paint.ascent() + paint.descent() - 1;
		}
		return yPoint;
	}

	/**
	 * Sets the heading used in this class and for the classes depending on this.
	 * @param heading The heading of this device according to the north
	 */
	public void updateData(float heading) {
		this.heading = heading;
		// Log.v(getClass().getSimpleName(), "heading: " + heading);
		invalidate();
	}

	/**
	 * Checks a GPS location for being usable as a valid new Location. Forces
	 * this View to be redrawn by invalidating it.
	 * 
	 * @param newLocation
	 *            a GPS Location, as readable from the mobile device
	 */
	public void updateData(Location newLocation) {
		if (CheckGPSLocation.isQuiteNewLocation(newLocation)
				&& CheckGPSLocation.isBetterLocation(newLocation,
						this.currentLocation)) {
			this.currentLocation = newLocation;
			Log.v(FindAdjacentSummits.class.getSimpleName(),
					"Noch da (1): updateData() in MyCompassView");
			FindAdjacentSummits.updateData();
			invalidate();
		}
	}

	public void invalidateLocation() {
		this.currentLocation = null;
	}
}