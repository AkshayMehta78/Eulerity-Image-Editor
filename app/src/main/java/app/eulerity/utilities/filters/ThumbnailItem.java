package app.eulerity.utilities.filters;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;


public class ThumbnailItem {
    public Bitmap image;
    public Filter filter;
    public int status;
    public ThumbnailItem() {
        image = null;
        filter = new Filter();
        status = 0;
    }
}