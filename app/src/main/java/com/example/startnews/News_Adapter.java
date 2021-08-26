package com.example.startnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.viewHolder> {

    List<News> news;
    Context context;
    private final OnClickedListener mOnClickedListener;

    News_Adapter(List<News> news, Context context, OnClickedListener onClickedListener) {
        this.news = news;
        this.context = context;
        this.mOnClickedListener = onClickedListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);

        return new viewHolder(view, mOnClickedListener);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        String img_res = news.get(position).getImage();
        String author = news.get(position).getAuthor();
        String desc = news.get(position).getDescription();
        String title = news.get(position).getTitle();

        Glide.with(context).load(img_res).into(holder.NewsImg);
        if (author.equals("null")) {
            author = "Source:Unknown";
        }

        holder.NewsTitle.setText(title);
        holder.NewsDesc.setText(desc);
        holder.Author.setText(author);


    }

    public void updateNews(List<News> updatedNews) {
        news.clear();
        news.addAll(updatedNews);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView NewsImg;
        TextView NewsDesc;
        TextView NewsTitle;
        TextView Author;
        OnClickedListener onClickedListener;

        public viewHolder(@NonNull View itemView, OnClickedListener onClickedListener) {
            super(itemView);
            NewsImg = itemView.findViewById(R.id.NewsImg);
            NewsDesc = itemView.findViewById(R.id.NewsDesc);
            NewsTitle = itemView.findViewById(R.id.NewsTitle);
            Author = itemView.findViewById(R.id.Author);
            this.onClickedListener = onClickedListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onClickedListener.onNoteClicked(getAdapterPosition());

        }


    }

    public interface OnClickedListener {
        void onNoteClicked(int position);

    }
}
