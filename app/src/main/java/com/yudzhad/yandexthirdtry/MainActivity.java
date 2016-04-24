package com.yudzhad.yandexthirdtry;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase("main_db");// При запуске очищаем бд.

        Fragment singersList = new SingersListFragment();
        FragmentManager fragmentManager = this.getFragmentManager();
        Slide slide = new Slide(Gravity.LEFT);

        slide.setDuration(1000);
        singersList.setAllowEnterTransitionOverlap(false);
        singersList.setExitTransition(slide);
        singersList.setReenterTransition(slide);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment, singersList)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
