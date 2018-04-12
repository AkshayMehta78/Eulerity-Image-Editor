
package app.eulerity.utilities;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Created by Akshay Mehta
 */

public class StringUtils {

    /**
     * Check whether a string is not NULL, empty or "NULL", "null", "Null"
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        boolean flag = true;
        if (str != null) {
            str = str.trim();
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

}