package app.eulerity.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import app.eulerity.R;
import app.eulerity.controllers.DataInterface;
import app.eulerity.controllers.NetworkManager;
import app.eulerity.model.ImageModel;
import app.eulerity.utilities.Constants;
import app.eulerity.utilities.DownloadFileFromURL;
import app.eulerity.utilities.GridDividerDecoration;
import app.eulerity.utilities.Utils;
import app.eulerity.views.adapters.ImageRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements DataInterface,DownloadFileFromURL.ServerUrlDownloadListener  {

    private RecyclerView mRecyclerview;
    private ArrayList<String> mImageModelArrayList;
    private NetworkManager mNetworkManager;
    private ImageRecyclerViewAdapter mImageRecyclerViewAdapter;
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgetReferences();
        initialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        if (Utils.isNetworkAvailable(this)) {
            mNetworkManager.fetchDataFromServer();
        } else {
            Utils.showSnackbar(findViewById(R.id.relativeLayout), getString(R.string.no_internet_connection));
        }
    }

    private void initialization() {
        mImageModelArrayList = new ArrayList<String>();
        mNetworkManager = new NetworkManager(this, this);
    }


    private void getWidgetReferences() {
        mRecyclerview = findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerview.addItemDecoration(new GridDividerDecoration(this));
    }

    @Override
    public void onDataListFetced(ArrayList<String> imageModelArrayList) {
        mImageModelArrayList = imageModelArrayList;
        if (mImageRecyclerViewAdapter == null) {
            if (imageModelArrayList.size() > 0) {
                mImageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this, mImageModelArrayList);
                mRecyclerview.setAdapter(mImageRecyclerViewAdapter);
            } else {
                Utils.showSnackbar(findViewById(R.id.relativeLayout), getString(R.string.no_data_found));
            }
        } else {
            mImageRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDataFetchFailed(String message) {
        Utils.showSnackbar(findViewById(R.id.relativeLayout), message);
    }

    public void onImageClicked(String imageUrl) {
        mImageUrl = imageUrl;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            new DownloadFileFromURL(this, this).execute(imageUrl);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new DownloadFileFromURL(this, this).execute(mImageUrl);
                } else {
                    Utils.showSnackbar(findViewById(R.id.mainLayout),getString(R.string.no_write_permission));
                }
                return;
            }
        }
    }


    @Override
    public void getFilePath(String filePath, String originalImageUrl) {
        Intent intent = new Intent(this, ImageEditor.class);
        intent.putExtra(Constants.IMAGEPATH, filePath);
        intent.putExtra(Constants.ORIG_PATH, originalImageUrl);
        startActivity(intent);
    }
}
