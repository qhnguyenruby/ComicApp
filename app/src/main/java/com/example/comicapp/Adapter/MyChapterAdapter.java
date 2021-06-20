package com.example.comicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Interface.IRecyclerItemClickListener;
import com.example.comicapp.Model.Chapter;
import com.example.comicapp.ViewComicActivity;

import java.util.List;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.ViewHolder> {

    Context context;
    List<Chapter> chapterList;
    LayoutInflater inflater;

    public MyChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.chapter_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.txt_chapter_numb.setText(chapterList.get(i).Name);
        viewHolder.setiRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.chapterSelected = chapterList.get(position);
                Common.chapterIndex = position;
                context.startActivity(new Intent(context, ViewComicActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_chapter_numb;
        IRecyclerItemClickListener iRecyclerItemClickListener;

        public void setiRecyclerItemClickListener(IRecyclerItemClickListener iRecyclerItemClickListener) {
            this.iRecyclerItemClickListener = iRecyclerItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_chapter_numb = itemView.findViewById(R.id.txt_chapter_numb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemClickListener.onClick(view, getAdapterPosition());
        }
    }

}
