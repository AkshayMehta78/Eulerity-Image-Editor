/*
 * Copyright (c) 2018 Trell Experiences Private Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.eulerity.utilities;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Background Async Task to download file
 */

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private static final String TAG = DownloadFileFromURL.class.getSimpleName();
    private final ServerUrlDownloadListener serverUrlDownloadListener;
    private Activity mActivity;
    private String imageUrl;

    public DownloadFileFromURL(Activity activity, ServerUrlDownloadListener serverUrlDownloadListener) {
        mActivity = activity;
        this.serverUrlDownloadListener = serverUrlDownloadListener;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.showProgress(mActivity);
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        imageUrl = f_url[0];
        File shareDirectory = Utils.getDefaultShareDirectory();

        int count;
        String fName = "Akshay.jpg";
        File file = new File(shareDirectory, fName);
        try {
            URL url = new URL(f_url[0]);

            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lenghtOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 1024 * 5);

            // Output stream to write file
            OutputStream output = new FileOutputStream(file.getAbsoluteFile());

            byte data[] = new byte[1024 * 5];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            if (file.exists()) {
                file.delete();
            }
        }

        return file.getAbsolutePath();
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
 /*       mCircularProgressbar.get().setProgress(Integer.parseInt(progress[0]));
        mTextIndicator.get().setText(String.valueOf(progress[0] + "%"));*/
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        Utils.closeProgress();
        // dismiss the dialog after the file was downloaded
        if (StringUtils.isNotEmpty(file_url)) {
            serverUrlDownloadListener.getFilePath(file_url,imageUrl);
        }
    }

    public interface ServerUrlDownloadListener {
        void getFilePath(String filePath, String imageUrl);
    }
}