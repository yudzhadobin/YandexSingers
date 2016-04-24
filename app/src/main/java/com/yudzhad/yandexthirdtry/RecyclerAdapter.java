package com.yudzhad.yandexthirdtry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import Entities.Singer;

/**
 * Created by vadim on 18.01.16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Singer> singers;
    Context context;
    View.OnClickListener item_listener;

    public RecyclerAdapter(Activity _activity, List<Singer> singers, View.OnClickListener ocl) {
        this.singers = singers;
        context = _activity;
        item_listener = ocl;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singer_list_item, parent, false);
        v.setOnClickListener(item_listener);
        return new RecyclerItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        Singer singer = singers.get(pos);
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        holder.name.setText(singer.getName());

        holder.genres.setText(singer.getViewableGenres());

        holder.albums.setText(singer.getViewableTracksAndAlbums());
        Picasso.with(context).load(singer.getSmallImageUrl()).fit().into(holder.singerImage);

    }

    @Override
    public int getItemCount() {
        return singers.size();
    }

    public void addData(List<Singer> data) {
        this.singers.addAll(data);
        notifyDataSetChanged();
    }
}
