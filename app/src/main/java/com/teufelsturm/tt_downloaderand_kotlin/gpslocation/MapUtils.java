/*
 * Copyright 2013-2014 Ludwig M Brinckmann
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.DistanceUnitAdapter;
import org.mapsforge.map.scalebar.MapScaleBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Utility functions that can be used across different mapsforge based
 * activities.
 */
public final class MapUtils {

	private static final String TAG = MapUtils.class.getSimpleName();
	private static File MAPFILE;
	/**
	 * @return a map file
	 */
//	public static MapDataStore getMapFile() {
//		return MAPFILE;
//	}
	/**
	 * Provides the directory of the map file, by default the Android external storage
	 * directory (e.g. sdcard).
	 * @return the directory of the map file
	 */
	private static File getMapFileDirectory(Context mContext) {
		return mContext.getFilesDir();

	}
	/**
	 * Provides the name  of the map file.
	 * @return the map file name to be used
	 */
	private static String getMapFileName() {
		return "saechsische_schweiz.map";
	}

	public static File getMapFile(Context mContext) {
	    if (MAPFILE == null) {
            MAPFILE = new File(getMapFileDirectory(mContext), MapUtils.getMapFileName());
        }
        return MAPFILE;
    }

	/**
	 * Copies the map-database from the assets to on the file system
	 * */
	public static void checkOrCopyMapFile(Context mContext) {
//		Log.v(Utils.class.getClass().getSimpleName(), "Führe copyMapFile() aus 1");
		File tmpMAPFILE = getMapFile(mContext);
		// {@see: http://stackoverflow.com/a/21200191/7122018}
		if (!tmpMAPFILE.exists()) {
			try {
				tmpMAPFILE.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		// write MAP-File if not existing or newer MAP Version exists
		if ( tmpMAPFILE.length() < 1000L) {
			try {
				copyMapFile(mContext, tmpMAPFILE);
			} catch (IOException e) {
				throw new Error("Error copying Map File");
			}
		}
		}
	}

	/**
	 * Copies your MAP File from your local assets-folder to the system folder,
	 * from where it can be accessed and handled. This is done by transfering
	 * bytestream.
	 * */
	private static void copyMapFile(Context mContext, File tmpMAPFILE) throws IOException {

		// Open your local db as the input stream
		InputStream myInput = mContext.getAssets().open(getMapFileName());

		// Path to the just created empty db
		String outFileName = tmpMAPFILE.getAbsolutePath();

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myInput.close();
		myOutput.close();
		Log.v(MapUtils.class.getSimpleName(), "copyMapFile() executed.");
	}

	/**
	 * Compatibility method.
	 *
	 * @param a the current mPresenter
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void enableHome(Activity a) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			a.getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Compatibility method.
	 *
	 * @param view       the view to set the background on
	 * @param background the background
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public static void setBackground(View view, Drawable background) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(background);
		} else {
			view.setBackgroundDrawable(background);
		}
	}

	public static Bitmap viewToBitmap(Context c, View view) {
		view.measure(View.MeasureSpec.getSize(view.getMeasuredWidth()),
				View.MeasureSpec.getSize(view.getMeasuredHeight()));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Drawable drawable = new BitmapDrawable(c.getResources(),
				android.graphics.Bitmap.createBitmap(view.getDrawingCache()));
		view.setDrawingCacheEnabled(false);
		return AndroidGraphicFactory.convertToBitmap(drawable);
	}

	/**
	 * private constructor; makes instantiating impossible
	 */
	private MapUtils() {
		throw new IllegalStateException();
	}

	private static final Logger LOGGER = Logger.getLogger(AndroidUtil.class.getName());

	/**
	 * Utility function to create a two-level tile cache along with its backends.
	 * This is the compatibility version that by default creates a non-persistent cache.
	 *
	 * @param c              the Android context
	 * @param id             name for the directory, which will be created as a subdirectory of the default cache directory (as
	 *                       returned by {@link android.content.Context#getExternalCacheDir()}).
	 * @param firstLevelSize size of the first level cache (tiles number)
	 * @param tileSize       tile size
	 * @return a new cache created on the external storage
	 */
	public static TileCache createExternalStorageTileCache(Context c, String id, int firstLevelSize, int tileSize) {
		return createExternalStorageTileCache(c, id, firstLevelSize, tileSize, false);
	}

	/**
	 * Utility function to create a two-level tile cache along with its backends.
	 *
	 * @param c              the Android context
	 * @param id             name for the directory, which will be created as a subdirectory of the default cache directory (as
	 *                       returned by {@link android.content.Context#getExternalCacheDir()}).
	 * @param firstLevelSize size of the first level cache (tiles number)
	 * @param tileSize       tile size
	 * @param persistent     whether the second level tile cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createExternalStorageTileCache(Context c,
														   String id, int firstLevelSize, int tileSize, boolean persistent) {
		return createExternalStorageTileCache(c.getExternalCacheDir(), id, firstLevelSize, tileSize, persistent);
	}

	/**
	 * Utility function to create a two-level tile cache along with its backends.
	 *
	 * @param cacheDir       the cache directory
	 * @param id             name for the directory, which will be created as a subdirectory of the cache directory
	 * @param firstLevelSize size of the first level cache (tiles number)
	 * @param tileSize       tile size
	 * @param persistent     whether the second level tile cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createExternalStorageTileCache(File cacheDir,
														   String id, int firstLevelSize, int tileSize, boolean persistent) {
		LOGGER.info("TILECACHE INMEMORY SIZE: " + Integer.toString(firstLevelSize));
		TileCache firstLevelTileCache = new InMemoryTileCache(firstLevelSize);
		if (cacheDir != null) {
			String cacheDirectoryName = cacheDir.getAbsolutePath() + File.separator + id;
			File cacheDirectory = new File(cacheDirectoryName);
			if (cacheDirectory.exists() || cacheDirectory.mkdirs()) {
				int tileCacheFiles = estimateSizeOfFileSystemCache(cacheDirectoryName, firstLevelSize, tileSize);
				if (cacheDirectory.canWrite() && tileCacheFiles > 0) {
					try {
						LOGGER.info("TILECACHE FILE SIZE: " + Integer.toString(tileCacheFiles));

						TileCache secondLevelTileCache = new FileSystemTileCache(tileCacheFiles, cacheDirectory,
								org.mapsforge.map.android.graphics.AndroidGraphicFactory.INSTANCE, persistent);
						return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
					} catch (IllegalArgumentException e) {
						LOGGER.warning(e.getMessage());
					}
				}
			}
		}
		return firstLevelTileCache;
	}

	/**
	 * Utility function to create a two-level tile cache with the right size. When the cache is created we do not
	 * actually know the size of the mapview, so the screenRatio is an approximation of the required size.
	 *
	 * @param c           the Android context
	 * @param cacheDir    the cache directory
	 * @param id          name for the storage directory
	 * @param tileSize    tile size
	 * @param screenRatio part of the screen the view takes up
	 * @param overdraw    overdraw allowance
	 * @param persistent  whether the second level tile cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(Context c, File cacheDir, String id, int tileSize,
											float screenRatio, double overdraw, boolean persistent) {
		int cacheSize = getMinimumCacheSize(c, tileSize, overdraw, screenRatio);
		return createExternalStorageTileCache(cacheDir, id, cacheSize, tileSize, persistent);
	}

	/**
	 * Utility function to create a two-level tile cache with the right size. When the cache is created we do not
	 * actually know the size of the mapview, so the screenRatio is an approximation of the required size.
	 *
	 * @param c           the Android context
	 * @param id          name for the storage directory
	 * @param tileSize    tile size
	 * @param screenRatio part of the screen the view takes up
	 * @param overdraw    overdraw allowance
	 * @param persistent  whether the second level tile cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(Context c, String id, int tileSize,
											float screenRatio, double overdraw, boolean persistent) {
		int cacheSize = getMinimumCacheSize(c, tileSize, overdraw, screenRatio);
		return createExternalStorageTileCache(c, id, cacheSize, tileSize, persistent);
	}

	/**
	 * Utility function to create a two-level tile cache with the right size. When the cache is created we do not
	 * actually know the size of the mapview, so the screenRatio is an approximation of the required size. This is the
	 * compatibility version that by default creates a non-persistent cache.
	 *
	 * @param c           the Android context
	 * @param id          name for the storage directory
	 * @param tileSize    tile size
	 * @param screenRatio part of the screen the view takes up
	 * @param overdraw    overdraw allowance
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(Context c, String id, int tileSize, float screenRatio, double overdraw) {
		return createTileCache(c, id, tileSize, screenRatio, overdraw, false);
	}

	/**
	 * Utility function to create a two-level tile cache with the right size, using the size of the map view.
	 *
	 * @param cacheDir   the cache directory
	 * @param id         name for the storage directory
	 * @param tileSize   tile size
	 * @param width      the width of the map view
	 * @param height     the height of the map view
	 * @param overdraw   overdraw allowance
	 * @param persistent whether the cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(File cacheDir, String id, int tileSize,
											int width, int height, double overdraw, boolean persistent) {
		int cacheSize = getMinimumCacheSize(tileSize, overdraw, width, height);
		return createExternalStorageTileCache(cacheDir, id, cacheSize, tileSize, persistent);
	}

	/**
	 * Utility function to create a two-level tile cache with the right size, using the size of the map view.
	 *
	 * @param c          the Android context
	 * @param id         name for the storage directory
	 * @param tileSize   tile size
	 * @param width      the width of the map view
	 * @param height     the height of the map view
	 * @param overdraw   overdraw allowance
	 * @param persistent whether the cache should be persistent
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(Context c, String id, int tileSize,
											int width, int height, double overdraw, boolean persistent) {
		int cacheSize = getMinimumCacheSize(tileSize, overdraw, width, height);
		return createExternalStorageTileCache(c, id, cacheSize, tileSize, persistent);
	}

	/**
	 * Utility function to create a two-level tile cache with the right size, using the size of the map view. This is
	 * the compatibility version that by default creates a non-persistent cache.
	 *
	 * @param c        the Android context
	 * @param id       name for the storage directory
	 * @param tileSize tile size
	 * @param width    the width of the map view
	 * @param height   the height of the map view
	 * @param overdraw overdraw allowance
	 * @return a new cache created on the external storage
	 */
	public static TileCache createTileCache(Context c, String id, int tileSize, int width, int height, double overdraw) {
		return createTileCache(c, id, tileSize, width, height, overdraw, false);
	}
	/**
	 * Utility method to create a standard tile renderer layer (without hillshading).
	 *
	 * @param tileCache       the cache
	 * @param mapViewPosition the position
	 * @param mapFile         the map file
	 * @param renderTheme     the render theme to use
	 * @param hasAlpha        if the layer is transparent (more memory)
	 * @param renderLabels    should usually be true
	 * @param cacheLabels     should usually be false
	 * @return the layer
	 */
	public static TileRendererLayer createTileRendererLayer(
			TileCache tileCache, MapViewPosition mapViewPosition,
			MapDataStore mapFile, XmlRenderTheme renderTheme, boolean hasAlpha,
			boolean renderLabels, boolean cacheLabels) {
		TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapFile,
				mapViewPosition, hasAlpha, renderLabels, cacheLabels, AndroidGraphicFactory.INSTANCE);
		tileRendererLayer.setXmlRenderTheme(renderTheme);
		return tileRendererLayer;
	}

	/**
	 * Utility method to create a tile renderer layer.
	 *
	 * @param tileCache         the cache
	 * @param mapViewPosition   the position
	 * @param mapFile           the map file
	 * @param renderTheme       the render theme to use
	 * @param hasAlpha          if the layer is transparent (more memory)
	 * @param renderLabels      should usually be true
	 * @param cacheLabels       should usually be false
	 * @param hillsRenderConfig may be null to omit hillshading
	 * @return the layer
	 */
	public static TileRendererLayer createTileRendererLayer(
			TileCache tileCache, MapViewPosition mapViewPosition,
			MapDataStore mapFile, XmlRenderTheme renderTheme, boolean hasAlpha,
			boolean renderLabels, boolean cacheLabels, HillsRenderConfig hillsRenderConfig) {
		TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapFile,
				mapViewPosition, hasAlpha, renderLabels, cacheLabels, AndroidGraphicFactory.INSTANCE,
				hillsRenderConfig);
		tileRendererLayer.setXmlRenderTheme(renderTheme);
		return tileRendererLayer;
	}

	/**
	 * @return true if the current thread is the UI thread, false otherwise.
	 */
	public static boolean currentThreadIsUiThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	/**
	 * @param cacheDirectoryName where the file system tile cache will be located
	 * @param firstLevelSize     size of the first level cache (tiles number), no point cache being smaller
	 * @param tileSize           tile size
	 * @return recommended number of files in FileSystemTileCache
	 */
	public static int estimateSizeOfFileSystemCache(String cacheDirectoryName, int firstLevelSize, int tileSize) {
		// assumption on size of files in cache, on the large side as not to eat
		// up all free space, real average probably 50K compressed
		final int tileCacheFileSize = 4 * tileSize * tileSize;
		final int maxCacheFiles = 2000; // arbitrary, probably too high

		// result cannot be bigger than maxCacheFiles
		int result = (int) Math.min(maxCacheFiles, getAvailableCacheSlots(cacheDirectoryName, tileCacheFileSize));

		if (firstLevelSize > result) {
			// no point having a file system cache that does not even hold the memory cache
			result = 0;
		}
		return result;
	}

	/**
	 * Get the number of tiles that can be stored on the file system.
	 *
	 * @param directory where the cache will reside
	 * @param fileSize  average size of tile to be cached
	 * @return number of tiles that can be stored without running out of space
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getAvailableCacheSlots(String directory, int fileSize) {
		StatFs statfs = new StatFs(directory);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			return statfs.getAvailableBytes() / fileSize;
		}
		// problem is overflow with devices with large storage, so order is important here
		// additionally avoid division by zero in devices with a large block size
		int blocksPerFile = Math.max(fileSize / statfs.getBlockSize(), 1);
		return statfs.getAvailableBlocks() / blocksPerFile;
	}

	/**
	 * Compute the minimum cache size for a view. When the cache is created we do not actually
	 * know the size of the mapview, so the screenRatio is an approximation of the required size.
	 * For the view size we use the frame buffer calculated dimension.
	 *
	 * @param c              the context
	 * @param tileSize       the tile size
	 * @param overdrawFactor the overdraw factor applied to the mapview
	 * @param screenRatio    the part of the screen the view covers
	 * @return the minimum cache size for the view
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getMinimumCacheSize(Context c, int tileSize, double overdrawFactor, float screenRatio) {
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int height;
		int width;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point p = new Point();
			display.getSize(p);
			height = p.y;
			width = p.x;
		} else {
			// deprecated since Android 13
			height = display.getHeight();
			width = display.getWidth();
		}

		// height * overdrawFactor / tileSize calculates the number of tiles that would cover
		// the view port, adding 1 is required since we can have part tiles on either side,
		// adding 2 adds another row/column as spare and ensures that we will generally have
		// a larger number of tiles in the cache than a TileLayer will render for a view.
		// Multiplying by screenRatio adjusts this somewhat inaccurately for MapViews on only part
		// of a screen (the result can be too low if a MapView is very narrow).
		// For any size we need a minimum of 4 (as the intersection of 4 tiles can always be in the
		// middle of a view.
		Dimension dimension = FrameBufferController.calculateFrameBufferDimension(new Dimension(width, height), overdrawFactor);
		return (int) Math.max(4, screenRatio * (2 + (dimension.height / tileSize))
				* (2 + (dimension.width / tileSize)));
	}

	/**
	 * Compute the minimum cache size for a view, using the size of the map view.
	 * For the view size we use the frame buffer calculated dimension.
	 *
	 * @param tileSize       the tile size
	 * @param overdrawFactor the overdraw factor applied to the mapview
	 * @param width          the width of the map view
	 * @param height         the height of the map view
	 * @return the minimum cache size for the view
	 */
	public static int getMinimumCacheSize(int tileSize, double overdrawFactor, int width, int height) {
		// height * overdrawFactor / tileSize calculates the number of tiles that would cover
		// the view port, adding 1 is required since we can have part tiles on either side,
		// adding 2 adds another row/column as spare and ensures that we will generally have
		// a larger number of tiles in the cache than a TileLayer will render for a view.
		// For any size we need a minimum of 4 (as the intersection of 4 tiles can always be in the
		// middle of a view.
		Dimension dimension = FrameBufferController.calculateFrameBufferDimension(new Dimension(width, height), overdrawFactor);
		return Math.max(4, (2 + (dimension.height / tileSize))
				* (2 + (dimension.width / tileSize)));
	}

	/**
	 * Restarts activity, from http://stackoverflow.com/questions/1397361/how-do-i-restart-an-android-activity
	 *
	 * @param activity the activity to restart
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void restartActivity(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			activity.recreate();
		} else {
			Intent intent = activity.getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			activity.finish();
			activity.overridePendingTransition(0, 0);

			activity.startActivity(intent);
			activity.overridePendingTransition(0, 0);
		}
	}

	/**
	 * Sets the scale bar on a map view with implicit arguments.
	 * If no distance unit adapters are
	 * supplied, there will be no scalebar, with only a primary adapter supplied, the mode will
	 * be single, with two adapters supplied, the mode will be dual.
	 *
	 * @param mapView                      the map view to change
	 * @param primaryDistanceUnitAdapter   primary scale
	 * @param secondaryDistanceUnitAdapter secondary scale
	 */
	public static void setMapScaleBar(MapView mapView,
									  DistanceUnitAdapter primaryDistanceUnitAdapter,
									  DistanceUnitAdapter secondaryDistanceUnitAdapter) {
		if (null == primaryDistanceUnitAdapter && null == secondaryDistanceUnitAdapter) {
			mapView.setMapScaleBar(null);
		} else {
			MapScaleBar scaleBar = mapView.getMapScaleBar();
			if (scaleBar == null) {
				scaleBar = new DefaultMapScaleBar(mapView.getModel().mapViewPosition, mapView.getModel().mapViewDimension,
						AndroidGraphicFactory.INSTANCE, mapView.getModel().displayModel);
				mapView.setMapScaleBar(scaleBar);
			}
			if (scaleBar instanceof DefaultMapScaleBar) {
				if (null != secondaryDistanceUnitAdapter) {
					((DefaultMapScaleBar) scaleBar).setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
					((DefaultMapScaleBar) scaleBar).setSecondaryDistanceUnitAdapter(secondaryDistanceUnitAdapter);
				} else {
					((DefaultMapScaleBar) scaleBar).setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.SINGLE);
				}
			}
			scaleBar.setDistanceUnitAdapter(primaryDistanceUnitAdapter);
		}
	}

}
