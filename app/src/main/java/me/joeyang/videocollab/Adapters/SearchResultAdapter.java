package me.joeyang.videocollab.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.joeyang.videocollab.Models.Video;
import me.joeyang.videocollab.R;

/**
 * Created by joe on 16-01-26.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Video> videos;
    private Context mContext;

    public SearchResultAdapter(List<Video> videos, Context context){
        this.videos = videos;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mThumbnailImage;
        public ViewHolder(View v){
            super(v);
            mThumbnailImage = (ImageView) v.findViewById(R.id.videoThumbnailImage);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(videos.get(position).thumbnailUrl).into(holder.mThumbnailImage);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

}
