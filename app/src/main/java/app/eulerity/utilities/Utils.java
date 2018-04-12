package app.eulerity.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;

import java.io.File;

import app.eulerity.R;

/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public class Utils {
    private static Dialog progressDialogInstance;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showSnackbar(View view, String message) {
        if(view!=null){
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }


    public static void showProgress(Context context) {
        try {
            if (context != null) {
                progressDialogInstance = new Dialog(context);
                progressDialogInstance.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialogInstance.setContentView(R.layout.layout_progress);
                progressDialogInstance.setCanceledOnTouchOutside(false);
                progressDialogInstance.setCancelable(false);
                progressDialogInstance.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeProgress() {
        try {
            if (progressDialogInstance != null && progressDialogInstance.isShowing()) {
                progressDialogInstance.dismiss();
                progressDialogInstance = null;
            }
        } catch (Exception e) {
        }
    }


    /**
     * Get Default Share Directory
     * @return default share directory
     */
    public static File getDefaultShareDirectory() {
        String root = Environment.getExternalStorageDirectory().toString() + File.separator;
        File trellShareDir = new File(root + Constants.DEFAULT_SHARE_FOLDER);
        trellShareDir.mkdirs();
        return trellShareDir;
    }



    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
