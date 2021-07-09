package com.teufelsturm.tt_downloader3;

import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.teufelsturm.tt_downloader3.searches.MyPagerFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

/**
 * Background Async Task to download file
 */
public class DownloadFileFromURL extends AsyncTask<String, String, File> {
    public static final String TAG = DownloadFileFromURL.class.getSimpleName();
    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<FragmentActivity> weakActivity;

    public DownloadFileFromURL(FragmentActivity myActivity) {
        this.weakActivity = new WeakReference<>(myActivity);
    }
    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected File doInBackground(String... f_url) {
        if (f_url == null || f_url[0] == null) return null;
        int count;
        File file = null;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();
            String fileExtension = f_url[0].substring(f_url[0].lastIndexOf('.') );
            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);


            file = new File(weakActivity.get().getApplicationContext().getFilesDir()
                    + "/user_profile_photo" + fileExtension);

            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();

            // Output stream
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                Log.v(TAG, "DownLoad Progress: " + (int) ((total * 100) / lenghtOfFile));
                // writing data to file
                output.write(data, 0, count);
            }
            Log.v(TAG, "Das Profilphoto wurde erfolgreich geladen nach: " + file.getCanonicalPath());
            // flushing output
            output.flush();
            // closing streams
            output.close();
            input.close();
            return file;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage(), e);
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(File file) {
        //        String msg = "Das Profilphoto wurde erfolgreich geladen nach: " + file.toString();
        //        Toast.makeText(weakActivity.get(), msg, Toast.LENGTH_LONG).show();
        if ( file != null ) {
            ((MyPagerFragment)weakActivity.get().getSupportFragmentManager()
                    .findFragmentByTag(MyPagerFragment.ID)).updateUserPhoto(file);
        }
    }
}