package com.kosmo.basakcoding.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.kosmo.basakcoding.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> items;
    private SimpleExoPlayer player;
//    List<ConstraintLayout> constraintLayoutList = new ArrayList<>();

    public VideoListAdapter(Context context, List<HashMap> items, SimpleExoPlayer player) {
        this.context = context;
        this.items = items;
        this.player = player;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.video_list_item_container, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = items.get(position);
//        constraintLayoutList.add(holder.viewVideo);

        holder.textVideoIndex.setText(item.get("index").toString());
        holder.textVideoTitle.setText(item.get("videoTitle").toString());
        holder.textVideoTime.setText("동영상 ~ " + item.get("videoLength").toString());
//        holder.viewVideo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        if (item.get("seen").toString().equals("Y")) {
            holder.videoSeenCheck.setVisibility(View.VISIBLE);
        }

        holder.viewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("basakcoding", item.get("videoId").toString() + " " + item.get("courseId").toString());

                MediaItem mediaItem = MediaItem.fromUri(
                        KEY_BASE_URL + "/upload/course/"+item.get("courseId").toString()+"/video/" + item.get("videoUri").toString());
//                holder.viewVideo.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, null));

                player.setMediaItem(mediaItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textVideoIndex, textVideoTitle, textVideoTime;
        ImageView videoSeenCheck;
        ConstraintLayout viewVideo;
        PlayerView playerView;

        public ViewHolder(View itemView) {
            super(itemView);
            videoSeenCheck = itemView.findViewById(R.id.videoSeenCheck);
            textVideoIndex = itemView.findViewById(R.id.textVideoIndex);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoTime = itemView.findViewById(R.id.textVideoTime);
            viewVideo = itemView.findViewById(R.id.viewVideo);
            playerView = itemView.findViewById(R.id.playerView);
        }
    }
}
