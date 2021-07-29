package com.kosmo.basakcoding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.kosmo.basakcoding.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static com.kosmo.basakcoding.utilities.Constants.KEY_BASE_URL;


public class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> items;

    public BestAdapter(Context context, List<HashMap> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.courses_best_container,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap item = items.get(position);
        holder.textTitle.setText(item.get("TITLE").toString());

        // https://square.github.io/picasso/
        Picasso.get().load(KEY_BASE_URL + "/upload/course/" +
                item.get("COURSE_ID").toString() +"/thumbnail/" +
                item.get("THUMBNAIL").toString()).into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView thumbnail;
        TextView textTitle;
        View viewBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}
