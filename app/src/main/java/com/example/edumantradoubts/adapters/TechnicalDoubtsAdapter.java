package com.example.edumantradoubts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Technical;
import java.util.List;

public class TechnicalDoubtsAdapter extends RecyclerView.Adapter<TechnicalDoubtsAdapter.ViewHolder> {

    List<Technical> technical;

    public TechnicalDoubtsAdapter(List<Technical> technical, Context context) {
        this.technical = technical;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public TechnicalDoubtsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_technical_doubts, parent, false);
        return new TechnicalDoubtsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicalDoubtsAdapter.ViewHolder holder, int position) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        holder.answer.setLayoutParams(lp);
        final int[] pushed = {0};

        holder.cardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushed[0]++;
                if(pushed[0]%2==1) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    holder.answer.setLayoutParams(lp);
                }
                else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    holder.answer.setLayoutParams(lp);
                }
            }


        });
        holder.question.setText(technical.get(position).getQuestion());
        holder.answer.setText(technical.get(position).getAnswer());

    }

    @Override
    public int getItemCount() {
        return technical.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cardLL;
        TextView question, answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLL = itemView.findViewById(R.id.card_ll);
            question = itemView.findViewById(R.id.card_technical_question);
            answer = itemView.findViewById(R.id.card_technical_answer);
        }
    }
}
