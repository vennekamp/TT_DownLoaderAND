package com.teufelsturm.tt_downloaderand_kotlin.dbHelper;

import com.teufelsturm.tt_downloader3.SteinFibelApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DB_BackUp2SDCard {

	public static String exportDB(Context aContext) {
		return readWriteFile(aContext, true);
	}

	public static String importDB(Context aContext) {
		return readWriteFile(aContext, false);
	}

	private static String readWriteFile(Context aContext, Boolean isExport) {
		Log.v("DB_BackUp2SDCard", "********************************"
				+ "\r\nState: "
				+ Environment.getExternalStorageState());
//		DataBaseHelper dbHelper = new DataBaseHelper(aContext);

		String dirDownLoad = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getPath();

		try {
			// appDBFile --> the database file used in this application 
			File appDBFile = new File(SteinFibelApplication.getDataBaseHelper().getDB_PATH_NAME());
			Log.v("DB_BackUp2SDCard", "File srcDBFile = " + appDBFile);
			if (appDBFile.exists()) {
				Log.v("DB_BackUp2SDCard", "********************************");
				// downloadDBFile --> the database file on the Download Folder 
				File downloadDBFile = new File(dirDownLoad,
						"TT_DownLoaderAND_Database.sqlite");
				Log.e("DB_BackUp2SDCard", "File dstDBFile = " + downloadDBFile);
				downloadDBFile.createNewFile();
				Log.v("DB_BackUp2SDCard", "********************************");
				InputStream in;
				OutputStream out;
				if (isExport) {
					in = new FileInputStream(appDBFile);
					out = new FileOutputStream(downloadDBFile);
				} else {
					in = new FileInputStream(downloadDBFile);
					out = new FileOutputStream(appDBFile);
				}
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
					/* Log.v("DB_BackUp2SDCard", "wrote: " + len + " bytes"); */
				}
				in.close();
				out.close();
				if (isExport) {
					new SingleMediaScanner(aContext, downloadDBFile);
					return "Daten wurden exportiert nach:\r\n" + downloadDBFile;
				} else {
					return "Daten wurden importiert aus:\r\n" + downloadDBFile;
				}
			}
		} catch (FileNotFoundException ex) {
			return ex.getMessage() + " im angegebenen Verzeichnis.";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
		return "?!?!?";
	}

}
