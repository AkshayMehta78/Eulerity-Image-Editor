package app.eulerity.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.eulerity.utilities.Constants;


/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public class JSONParser implements Constants.JSON {

    public ArrayList<String> parseMovieList(JSONArray jsonArray) {
        ArrayList<String> imageArrayList = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                imageArrayList.add(jsonObject.isNull(URL) ? "" : jsonObject.getString(URL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageArrayList;
    }
}
