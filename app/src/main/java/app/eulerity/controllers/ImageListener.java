package app.eulerity.controllers;

import com.androidnetworking.error.ANError;

import org.json.JSONObject;

/**
 * Created by Akshay Mehta on 4/12/2018.
 */

public interface ImageListener {
    void onImageUploadSuccess();
    void onImageUploadFails(ANError error);
    void onImageURLReceived(String url);
}
