package com.mx.designateddrive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mx.designateddrive.R;
import com.mx.designateddrive.vo.ReviewVO;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ReviewVO> dataList;

    public ReviewAdapter(Context context, ArrayList<ReviewVO> list){
        inflater = LayoutInflater.from(context);
        this.dataList = list;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.star.setRating(dataList.get(position).rating);
        holder.user_name.setText(dataList.get(position).user_name);
        holder.review_txt.setText(dataList.get(position).review_txt);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private RatingBar star;
        private TextView review_txt, user_name;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            star = itemView.findViewById(R.id.review_star);
            review_txt = itemView.findViewById(R.id.review_txt);
            user_name = itemView.findViewById(R.id.user_id);
        }
    }
}
