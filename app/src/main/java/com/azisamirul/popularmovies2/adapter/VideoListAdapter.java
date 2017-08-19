package com.azisamirul.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.azisamirul.popularmovies2.R;
import com.azisamirul.popularmovies2.config.Config;
import com.azisamirul.popularmovies2.data.VideoList;

import java.util.List;

/**
 * Created by azisamirul on 07/08/2017.
 */

public class VideoListAdapter extends
        RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private List<VideoList> videoLists;
    private Context ct;
    public VideoListOnClickHandler videoListOnClickHandler;

    public VideoListAdapter(VideoListOnClickHandler clickHandler, Context ct) {
        this.ct = ct;
        videoListOnClickHandler = clickHandler;
    }

    public interface VideoListOnClickHandler {
        void onClick(int pos);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoList vidList = videoLists.get(position);
        holder.tvTrailerName.setText(vidList.getName());
        holder.tvTrailerSite.setText(vidList.getSite());
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutVideoList = R.layout.video_lists;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachedImmediately = false;
        View view = inflater.inflate(layoutVideoList, parent, shouldAttachedImmediately);
        return new VideoViewHolder(view);

    }

    @Override
    public int getItemCount() {
        if (null == videoLists) return 0;
        return videoLists.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTrailerName, tvTrailerSite;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvTrailerSite = (TextView) itemView.findViewById(R.id.tv_trailer_site);
            tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            videoListOnClickHandler.onClick(adapterPosition);
            Context ctx = v.getContext();
            //reference from : https://gist.github.com/abhiin1947/4579810
            Uri uri = Uri.parse(Config.YOUTUBE_TRAILER + videoLists.get(adapterPosition).getKey());
            Intent youtube = new Intent(Intent.ACTION_VIEW, uri);
            Intent chooser = Intent.createChooser(youtube, "Open With");
            if (youtube.resolveActivity(ctx.getPackageManager()) != null) {
                ctx.startActivity(chooser);
            }

        }
    }

    public void addVideo(List<VideoList> videoLists) {
        this.videoLists = videoLists;
        notifyDataSetChanged();
    }

}
