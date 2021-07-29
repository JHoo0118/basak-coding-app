package com.kosmo.basakcoding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.kosmo.basakcoding.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;


public class CurriculumListAdapter extends RecyclerView.Adapter<CurriculumListAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> items;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SimpleExoPlayer player;
    private String activeId;
    private RecyclerView currRecyclerView;

    public CurriculumListAdapter(Context context, List<HashMap> items, SimpleExoPlayer player, String videoId) {
        this.context = context;
        this.items = items;
        this.player = player;
        this.activeId = videoId;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.currRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.curriculum_list_item_container,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = items.get(position);
        holder.textChapterTitle.setText("Chapter " + item.get("cIndex") +" - " + item.get("name"));

        // 자식 RecyclerView 설정 시작
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.size());

        VideoListAdapter videoListAdapter = new VideoListAdapter(context, (List<HashMap>) item.get("videos"), player, activeId, currRecyclerView);
        holder.videoRV.setLayoutManager(layoutManager);
        holder.videoRV.setAdapter(videoListAdapter);
        holder.videoRV.setRecycledViewPool(viewPool);
        // 자식 RecyclerView 설정 끝

        holder.viewCurriculum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "클릭", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textChapterTitle;
        ConstraintLayout viewCurriculum;
        RecyclerView videoRV;

        public ViewHolder(View itemView) {
            super(itemView);
            videoRV = itemView.findViewById(R.id.videoRV);
            textChapterTitle = itemView.findViewById(R.id.textChapterTitle);
            viewCurriculum = itemView.findViewById(R.id.viewCurriculum);
        }
    }
}
