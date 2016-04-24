package com.yudzhad.yandexthirdtry;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public ImageView singerImage;
    public TextView genres;
    public TextView albums;

    public RecyclerItemViewHolder(final View parent) {
        super(parent);

        name = (TextView) parent.findViewById(R.id.singer_name);
        singerImage = (ImageView) parent.findViewById(R.id.singer_image);
        genres = (TextView) parent.findViewById(R.id.singer_genres);
        albums = (TextView) parent.findViewById(R.id.singer_albums);
    }


}