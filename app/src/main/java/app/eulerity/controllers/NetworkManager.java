package app.eulerity.controllers;

import android.app.Activity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import app.eulerity.utilities.Constants;
import app.eulerity.utilities.Utils;
import app.eulerity.views.activities.ImageEditor;
import app.eulerity.views.activities.MainActivity;

/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public class NetworkManager {

    private Activity mActivity;
    private DataInterface mDataInterface;
    private ImageListener mImageListener;

    public NetworkManager(Activity activity, DataInterface dataInterface) {
        mActivity = activity;
        mDataInterface = dataInterface;
    }

    public NetworkManager(Activity activity, ImageListener imageListener) {
        this.mActivity = activity;
        mImageListener = imageListener;

    }

    public void fetchDataFromServer() {
        String apiUrl = Constants.DATA_URL;

        AndroidNetworking.get(apiUrl)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mDataInterface.onDataListFetced(new JSONParser().parseMovieList(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        mDataInterface.onDataFetchFailed(anError.getErrorDetail());
                    }
                });
    }

    public void uploadImageToServer(String mRawLocalImageUrl, String mOriginalImageUrl, String uploadUrl) {

        AndroidNetworking.upload(uploadUrl)
                .addMultipartFile("file",new File(mRawLocalImageUrl))
                .addMultipartParameter("appid","am80262n@pace.edu")
                .addMultipartParameter("original",mOriginalImageUrl)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.closeProgress();
                       if(mImageListener!=null){
                           mImageListener.onImageUploadSuccess();
                       }
                    }
                    @Override
                    public void onError(ANError error) {
                        Utils.closeProgress();
                        if(mImageListener!=null){
                            mImageListener.onImageUploadFails(error);
                        }
                    }
                });
    }

    public void getUploadImageUrl() {
        String apiUrl = Constants.UPLOAD_URL;
        Utils.showProgress(mActivity);
        AndroidNetworking.get(apiUrl)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(mImageListener!=null){
                            try {
                                mImageListener.onImageURLReceived(response.getString("url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.closeProgress();
                        if(mImageListener!=null){
                            mImageListener.onImageUploadFails(error);
                        }
                    }
                });
    }
}
