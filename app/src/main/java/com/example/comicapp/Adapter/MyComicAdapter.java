package com.example.comicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.ChaptersActivity;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Interface.IRecyclerItemClickListener;
import com.example.comicapp.Model.Comic;
import com.example.comicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;

    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.comic_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Picasso.get().load(comicList.get(i).Image).into(myViewHolder.image_comic);
        myViewHolder.txt_comic.setText(comicList.get(i).Name);

        myViewHolder.setiRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //save comic select
                Common.comicSelected = comicList.get(position);
                Intent intent = new Intent(
                        context, ChaptersActivity.class
                );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image_comic;
        TextView txt_comic;

        IRecyclerItemClickListener iRecyclerItemClickListener;

        public void setiRecyclerItemClickListener(IRecyclerItemClickListener iRecyclerItemClickListener) {
            this.iRecyclerItemClickListener = iRecyclerItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_comic = itemView.findViewById(R.id.image_comic);
            txt_comic = itemView.findViewById(R.id.comic_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
