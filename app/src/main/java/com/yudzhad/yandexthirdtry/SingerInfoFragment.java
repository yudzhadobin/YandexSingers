package com.yudzhad.yandexthirdtry;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Entities.Singer;

public class SingerInfoFragment extends Fragment {
    Singer singer;
    Toolbar toolbar;
    ImageView imageView;
    TextView genresView;
    TextView albumsView;
    TextView descriptionView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_singer, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initActivity();
        loadData();
    }

    private void loadData() {
        singer = (Singer) this.getArguments().getSerializable("singer");
        toolbar.setTitle(singer.getName());
        Picasso.with(getActivity()).load(singer.getBigImageUrl()).fit().into(imageView);
        descriptionView.setText(singer.getDescription());
        genresView.setText(singer.getViewableGenres());
        albumsView.setText(singer.getViewableTracksAndAlbums());
    }


    private void initActivity() {
        imageView = (ImageView) getActivity().findViewById(R.id.big_singer_image);
        genresView = (TextView) getActivity().findViewById(R.id.genresView);
        albumsView = (TextView) getActivity().findViewById(R.id.albumsView);
        descriptionView = (TextView) getActivity().findViewById(R.id.descriptionView);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

    }


}
