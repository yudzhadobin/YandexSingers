package com.yudzhad.yandexthirdtry;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Entities.Singer;
import Services.GetJSONFromServer;
import Services.SaveDataTask;

/**
 * Created by yudzh_000 on 19.04.2016.
 */
public class SingersListFragment extends Fragment {

    boolean isFirstStart = true;

    RecyclerView recyclerView;
    ProgressView progress;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.singer_list, container, false);
        return view;
   }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initProgress(getView());
        initRecycler(getView());
        this.toolbar = (Toolbar)getView().findViewById(R.id.toolbar);
        ServerRequest serverRequest = new ServerRequest("http://download.cdn.yandex.net/mobilization-2016/artists.json");
        serverRequest.execute();
    }

    private void initRecycler(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.singers_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initProgress(View view) {
        progress = (ProgressView) view.findViewById(R.id.progress);
        progress.start();
    }


    public class ServerRequest extends AsyncTask<Void, List<Singer>, List<Singer>> {

        String url;
        RecyclerAdapter recyclerAdapter;
        SaveDataTask task;
        public ServerRequest(String url) {
            this.url = url;
        }

        @Override
        protected List<Singer> doInBackground(Void... params) {
            JSONArray result;
            List<Singer> singers = new ArrayList<>();
            List<Singer> resultFromBD = Singer.getAllFromDB(getActivity());
            Collections.sort(resultFromBD);
            publishProgress(resultFromBD);
            if(isFirstStart) {//Если первый заход на экран, то обращаемся к серверу за данными.
                try {
                    result = GetJSONFromServer.readJsonFromUrl(url);
                    singers = Singer.initFromJSON(result);
                    Collections.sort(singers);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                task = new SaveDataTask(singers, getActivity());

            }
            return singers;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.start();
            recyclerAdapter = (RecyclerAdapter) recyclerView.getAdapter();
        }

        @Override
        protected void onPostExecute(final List<Singer> singers) {
            super.onPostExecute(singers);
            if(isFirstStart) {
                task.execute();
                isFirstStart = false;
            }
            this.recyclerAdapter = (RecyclerAdapter)recyclerView.getAdapter();
            recyclerAdapter.addData(singers);
            progress.stop();
        }

        @Override
        protected void onProgressUpdate(final List<Singer>... values) {
            super.onProgressUpdate(values);
            if(recyclerAdapter == null) {
                View.OnClickListener ocl = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int itemPosition = recyclerView.getChildAdapterPosition(view);
                        Singer singer = values[0].get(itemPosition);

                        Fragment singerInfo = new SingerInfoFragment();

                        Slide slideTransition = new Slide(Gravity.RIGHT);
                        slideTransition.setDuration(1000);

                        singerInfo.setEnterTransition(slideTransition);
                        singerInfo.setAllowEnterTransitionOverlap(false);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("singer", singer);
                        singerInfo.setArguments(bundle);

                        FragmentManager fragmentManager = getActivity().getFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment, singerInfo)
                                .addToBackStack(null)
                                .commit();
                    }
                };
                recyclerView.setAdapter(new RecyclerAdapter(getActivity(), values[0], ocl));
            }

        }


    }
}
