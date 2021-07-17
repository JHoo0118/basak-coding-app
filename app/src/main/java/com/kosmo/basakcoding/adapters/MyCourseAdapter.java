package com.kosmo.basakcoding.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kosmo.basakcoding.R;
import com.kosmo.basakcoding.activities.CourseDetailActivity;
import com.kosmo.basakcoding.activities.SignUpActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;


public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> items;

    public MyCourseAdapter(Context context, List<HashMap> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.my_course_item_container,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = items.get(position);
        holder.textTitle.setText(item.get("TITLE").toString());
        holder.textAdminName.setText(item.get("NAME").toString());
        holder.courseProgressText.setText(item.get("PROGRESS").toString()+"% 완료됨");
        holder.courseProgress.setProgress(Integer.parseInt(item.get("PROGRESS").toString()));

        // https://square.github.io/picasso/
        Picasso.get().load(KEY_BASE_URL + "/upload/course/" +
                item.get("COURSE_ID").toString() +"/thumbnail/" +
                item.get("THUMBNAIL").toString()).into(holder.thumbnail);

        holder.viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, items.get(position).get("TITLE").toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", items.get(position).get("COURSE_ID").toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView thumbnail;
        TextView textTitle;
        TextView textAdminName;
        ProgressBar courseProgress;
        TextView courseProgressText;
        View viewBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAdminName = itemView.findViewById(R.id.textAdminName);
            courseProgress = itemView.findViewById(R.id.courseProgress);
            courseProgressText = itemView.findViewById(R.id.courseProgressText);
        }
    }
}
