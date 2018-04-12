package app.eulerity.views.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

import app.eulerity.R;
import app.eulerity.controllers.ImageListener;
import app.eulerity.controllers.NetworkManager;
import app.eulerity.utilities.Constants;
import app.eulerity.utilities.ImageUtils;
import app.eulerity.utilities.StringUtils;
import app.eulerity.utilities.Utils;
import app.eulerity.utilities.filters.ThumbnailCallback;
import app.eulerity.utilities.filters.ThumbnailItem;
import app.eulerity.utilities.filters.ThumbnailsAdapter;
import app.eulerity.utilities.filters.ThumbnailsManager;

/**
 * Created by Akshay Mehta on 4/11/2018.
 */

public class ImageEditor extends AppCompatActivity implements View.OnClickListener , ThumbnailCallback,ImageListener {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final String TAG = ImageEditor.class.getSimpleName();
    private ImageView mImageView;
    private String mRawLocalImageUrl,mOriginalImageUrl;

    private TextView mFlipImageView, mOverlayTextView, mFilterTextView, mTaskTitleTextView;
    private ImageView mCancelImageView, mAcceptImageView;

    private EditText mAddTextEditText;
    private RelativeLayout mOverlayLayout;
    private RelativeLayout mFilterRelativeView;
    private RecyclerView mThumbnailRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getImagePath();
        getWidgetReferences();
        getWidgetEvents();
        setUpImage();

        initHorizontalList();
    }



    /**
     * Set views events
     */
    private void getWidgetEvents() {
        mFlipImageView.setOnClickListener(this);
        mOverlayTextView.setOnClickListener(this);
        mFilterTextView.setOnClickListener(this);
        mAcceptImageView.setOnClickListener(this);
        mCancelImageView.setOnClickListener(this);
    }

    /**
     * get image path from intent
     */
    private void getImagePath() {
        mOriginalImageUrl = getIntent().getStringExtra(Constants.ORIG_PATH);
        mRawLocalImageUrl = getIntent().getStringExtra(Constants.IMAGEPATH);
        mRawLocalImageUrl = ImageUtils.compressImage(mRawLocalImageUrl,this);
    }

    /**
     * Get all views reference
     */
    private void getWidgetReferences() {
        mImageView = findViewById(R.id.imageView);
        mFlipImageView = findViewById(R.id.flipImageView);
        mOverlayTextView = findViewById(R.id.overlayTextView);
        mFilterTextView = findViewById(R.id.filterTextView);
        mOverlayLayout = findViewById(R.id.overlayLayout);
        mAddTextEditText = findViewById(R.id.addTextEditTextView);
        mTaskTitleTextView = findViewById(R.id.taskTitleTextView);
        mCancelImageView = findViewById(R.id.cancelImageView);
        mAcceptImageView = findViewById(R.id.acceptImageView);
        mThumbnailRecyclerView = findViewById(R.id.thumbnails);
        mFilterRelativeView =findViewById(R.id.filterRelativeView);
    }

    /**
     * set default image into ImageView
     */
    private void setUpImage() {
        if (StringUtils.isNotEmpty(mRawLocalImageUrl)) {
            mImageView.setImageDrawable(Drawable.createFromPath(mRawLocalImageUrl));
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mFlipImageView) {
            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview
            bitmap = ImageUtils.flip(bitmap, ImageUtils.FLIP_HORIZONTAL);
            mImageView.setImageBitmap(bitmap);
            mRawLocalImageUrl = ImageUtils.saveImage(bitmap,System.currentTimeMillis());
        } else if (view == mOverlayTextView) {
            enableOverlayView();
        } else if (view == mFilterTextView) {
            enableFilterView();
        } else if (view == mAcceptImageView) {
            if(mOverlayLayout.getVisibility()==View.VISIBLE){
                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview
                bitmap = ImageUtils.processingBitmap(bitmap, mAddTextEditText.getText().toString().trim());
                mImageView.setImageBitmap(bitmap);
                mRawLocalImageUrl = ImageUtils.saveImage(bitmap,System.currentTimeMillis());
            }else{
                Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview
                mRawLocalImageUrl = ImageUtils.saveImage(bitmap,System.currentTimeMillis());
            }
            disableOverlayImageView();

        } else if (view == mCancelImageView) {
            if(mFilterRelativeView.getVisibility()==View.VISIBLE){
                mImageView.setImageDrawable(Drawable.createFromPath(mRawLocalImageUrl));
            }
            disableOverlayImageView();
        }
    }




    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        mThumbnailRecyclerView.setLayoutManager(layoutManager);
        mThumbnailRecyclerView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, ImageEditor.this);
                mThumbnailRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }


    private void enableFilterView() {
        mOverlayLayout.setVisibility(View.GONE);
        mFilterRelativeView.setVisibility(View.VISIBLE);
        mTaskTitleTextView.setText(getString(R.string.filters));
        mAcceptImageView.setVisibility(View.VISIBLE);
        mCancelImageView.setVisibility(View.VISIBLE);
    }



    /**
     * Disable Overlayview when clicked on Overlay
     */
    private void disableOverlayImageView() {
        mAcceptImageView.setVisibility(View.GONE);
        mCancelImageView.setVisibility(View.GONE);
        mAddTextEditText.setText("");
        mOverlayLayout.setVisibility(View.GONE);
        mFilterRelativeView.setVisibility(View.GONE);
        mTaskTitleTextView.setText(getString(R.string.editor));
    }

    /**
     * Enable Overlayview when clicked on Overlay
     */
    private void enableOverlayView() {
        mOverlayLayout.setVisibility(View.VISIBLE);
        mTaskTitleTextView.setText(getString(R.string.add_text));
        mAcceptImageView.setVisibility(View.VISIBLE);
        mCancelImageView.setVisibility(View.VISIBLE);
        mAddTextEditText.setText("");
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        mImageView.setImageDrawable(Drawable.createFromPath(mRawLocalImageUrl));
        BitmapFactory.Options opts = new BitmapFactory.Options(); opts.inMutable = true;
        Bitmap thumbImage = ((BitmapDrawable) mImageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview
        Bitmap newBitmap = thumbImage.copy(Bitmap.Config.ARGB_4444,true);
        newBitmap = filter.processFilter(newBitmap);
        mImageView.setImageBitmap(newBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.upload:
                new NetworkManager(ImageEditor.this,this).getUploadImageUrl();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onImageUploadSuccess() {
        Toast.makeText(this,getString(R.string.success_upload),Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onImageUploadFails(ANError error) {
        Utils.showSnackbar(findViewById(R.id.mainLayout),error.getErrorDetail());
    }

    @Override
    public void onImageURLReceived(String url) {
        new NetworkManager(ImageEditor.this,this).uploadImageToServer(mRawLocalImageUrl,mOriginalImageUrl,url);
    }
}
