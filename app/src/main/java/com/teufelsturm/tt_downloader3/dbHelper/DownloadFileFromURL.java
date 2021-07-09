package com.teufelsturm.tt_downloader3.dbHelper;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    boolean last;
    public DownloadFileFromURL(boolean b) {
        this.last = b;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        Log.d("doInBackground", String.valueOf(last));
        int count;
        String uniquename = String.valueOf(System.currentTimeMillis());
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()+"/Download/"+ uniquename);
            Log.d("OutputFile",""+Environment.getExternalStorageDirectory()+"/Download/"+ uniquename);


            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("catchError: ", e.getMessage());
            Log.d("catchOutputFile","no output file");
        }

        return f_url[0];
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        //the above calulated file progress needs to be updated here
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        Log.d("onPostExecuteURL",file_url);
    }

}