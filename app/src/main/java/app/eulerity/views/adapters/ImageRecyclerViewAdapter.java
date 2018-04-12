package app.eulerity.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.eulerity.R;
import app.eulerity.utilities.Constants;
import app.eulerity.utilities.DownloadFileFromURL;
import app.eulerity.views.activities.ImageEditor;
import app.eulerity.views.activities.MainActivity;


/**
 * Created by Akshay Mehta
 */

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final String TAG = ImageRecyclerViewAdapter.class.getSimpleName();
    Activity mActivity;
    ArrayList<String> mImageArrayList;

    public ImageRecyclerViewAdapter(Activity activity, ArrayList<String> mImageArrayList) {
        mActivity = activity;
        this.mImageArrayList = mImageArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.imageUrl = mImageArrayList.get(position);
        Picasso.get().load(mImageArrayList.get(position)).resize(600, 400).into(viewHolder.imageView);
    }


    @Override
    public int getItemCount() {
        return mImageArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        String imageUrl;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if(mActivity instanceof MainActivity){
                ((MainActivity)mActivity).onImageClicked(imageUrl);
            }
        }
    }





}