package app.eulerity.utilities.filters;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import app.eulerity.R;


public class ThumbnailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "THUMBNAILS_ADAPTER";
    private static int lastPosition = -1;
    private ThumbnailCallback thumbnailCallback;
    private List<ThumbnailItem> dataSet;

    public ThumbnailsAdapter(List<ThumbnailItem> dataSet, ThumbnailCallback thumbnailCallback) {
        this.dataSet = dataSet;
        this.thumbnailCallback = thumbnailCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.layout_image_filter_row, viewGroup, false);
        return new ThumbnailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        final ThumbnailItem thumbnailItem = dataSet.get(i);
        ThumbnailsViewHolder thumbnailsViewHolder = (ThumbnailsViewHolder) holder;
        thumbnailsViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        thumbnailsViewHolder.thumbnail.setScaleType(ImageView.ScaleType.FIT_START);
        thumbnailsViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != i) {
                    thumbnailCallback.onThumbnailClick(thumbnailItem.filter);
                    lastPosition = i;
                    notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public ThumbnailsViewHolder(View v) {
            super(v);
            this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        }
    }
}