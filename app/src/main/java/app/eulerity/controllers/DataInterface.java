package app.eulerity.controllers;

import java.util.ArrayList;

/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public interface DataInterface {
    void onDataListFetced(ArrayList<String> imageModelArrayList);
    void onDataFetchFailed(String message);
}
