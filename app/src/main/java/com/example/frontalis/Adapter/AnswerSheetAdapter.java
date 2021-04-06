package com.example.frontalis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontalis.Common.Common;
import com.example.frontalis.Model.CurrentQuestion;
import com.example.frontalis.R;

import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder> {

    Context context;
    List<CurrentQuestion> currentQuestionList;

    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet_item,
                 parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (currentQuestionList.get(position).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
            holder.question_item.setBackgroundResource(R.drawable.grid_question_no_answer);
        else
            holder.question_item.setBackgroundResource(R.drawable.grid_question_no_answer);
    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View question_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question_item = itemView.findViewById(R.id.question_item);

        }
    }
}
