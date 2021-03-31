package com.example.edumantradoubts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Announcement;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>{

    Context context;
    List<Announcement> announcement;

    public AnnouncementAdapter(Context context, List<Announcement> announcement) {
        this.context = context;
        this.announcement = announcement;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, int position) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        holder.body.setLayoutParams(lp);
        final int[] pushed = {0};

        holder.cardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushed[0]++;
                LinearLayout.LayoutParams lp;
                if(pushed[0]%2==1) {
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                else {
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                }
                holder.body.setLayoutParams(lp);
            }
        });
        holder.date.setText(announcement.get(position).getDate());
        holder.title.setText(announcement.get(position).getTitle());
        holder.body.setText(announcement.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return announcement.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, title, body;
        LinearLayout cardLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.announcement_date);
            title = itemView.findViewById(R.id.announcement_title);
            body = itemView.findViewById(R.id.announcement_body);
            cardLL = itemView.findViewById(R.id.announcement_card_ll);
        }

    }
}
