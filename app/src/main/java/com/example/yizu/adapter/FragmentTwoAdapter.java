package com.example.yizu.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yizu.R;
import com.example.yizu.bean.Evaluation;

import java.util.List;

/**
 * Created by yikuai on 2017/7/25.
 */

public class FragmentTwoAdapter extends RecyclerView.Adapter<FragmentTwoAdapter.ViewHolder> {//评价卡片适配器
    private Context context;
    private List<Evaluation> mEvaluation;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView rentedPerson;
        TextView StarRating;
        TextView  TextEvaluation;
        TextView  evaluationTime;
        public ViewHolder(View view){
            super(view);
            layout=(LinearLayout) view;
            rentedPerson=(TextView)view.findViewById(R.id.rentedPerson);
            TextEvaluation=(TextView)view.findViewById(R.id.textEvaluation);
            StarRating=(TextView)view.findViewById(R.id.starRating2);
            evaluationTime=(TextView)view.findViewById(R.id.evaluationTime);
        }
    }
    public FragmentTwoAdapter(List<Evaluation> evaluationsList){
        mEvaluation=evaluationsList;
    }
    @Override
    public FragmentTwoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_two_card, parent, false);
        FragmentTwoAdapter.ViewHolder holder = new FragmentTwoAdapter.ViewHolder(view);
        return holder;
    }




    public void onBindViewHolder(FragmentTwoAdapter.ViewHolder holder, int position){

        Evaluation evaluation=mEvaluation.get(position);
        holder.rentedPerson.append(evaluation.getEval().getName());
        holder.StarRating.append(String.valueOf(evaluation.getStarRating()));
        holder.TextEvaluation.append(evaluation.getTextEvaluation());
        holder.evaluationTime.setText(evaluation.getCreatedAt());
    }
    @Override
    public int getItemCount(){
        return mEvaluation.size();
    }

}
