package com.kosmo.basakcoding.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.activities.CourseVideoActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> items;
    private SimpleExoPlayer player;
    private int lastTappedPosition = -1;
    private String activeId;
    RecyclerView currRecyclerView;
    RecyclerView videoRecyclerView;

    public VideoListAdapter(Context context, List<HashMap> items, SimpleExoPlayer player, String activeId, RecyclerView currRecyclerView) {
        this.context = context;
        this.items = items;
        this.player = player;
        this.activeId = activeId;
        this.currRecyclerView = currRecyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.videoRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.video_list_item_container, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = items.get(position);
        if (activeId.equals(item.get("videoId").toString()) && lastTappedPosition == -1) {
            // 현재 보는 동영상 UI 표시
            holder.viewVideo.setBackgroundColor(context.getResources().getColor(R.color.colorPurpleLight, null));
        }

        holder.textVideoIndex.setText(item.get("index").toString());
        holder.textVideoTitle.setText(item.get("videoTitle").toString());
        holder.textVideoTime.setText("동영상 ~ " + item.get("videoLength").toString());

        if (item.get("seen").toString().equals("Y")) {
            holder.videoSeenCheck.setVisibility(View.VISIBLE);
        }

        holder.viewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 동영상 스위칭
                MediaItem mediaItem = MediaItem.fromUri(
                        KEY_BASE_URL + "/upload/course/" + item.get("courseId").toString() + "/video/" + item.get("videoUri").toString());
                player.setMediaItem(mediaItem);

                // 현재 보는 동영상 UI 표시
                holder.viewVideo.setBackgroundColor(context.getResources().getColor(R.color.colorPurpleLight, null));

                CourseVideoActivity.currPlayingVideoId = item.get("videoId").toString();
                CourseVideoActivity.lastTappedPosition = position;
                CourseVideoActivity.tappedParentView = videoRecyclerView.getParent();
                currRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        boolean check = true;
        if (CourseVideoActivity.lastTappedPosition == position && CourseVideoActivity.tappedParentView == videoRecyclerView.getParent()) {
            holder.viewVideo.setBackgroundColor(context.getResources().getColor(R.color.colorPurpleLight, null));
        } else if (CourseVideoActivity.lastTappedPosition != -1) {
            holder.viewVideo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        }
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
