package app.eulerity;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public class Eulerity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }

}
