package Services;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import Entities.Singer;

/**
 * Created by yudzh_000 on 22.04.2016.
 */
public class SaveDataTask extends AsyncTask<Void, Void, Void>{

    List<Singer> target;
    Activity activity;
    public SaveDataTask(List<Singer> target, Activity activity) {
        this.target = target;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (Singer singer : target) {
            singer.putIntoDB(DBHelper.getDB(activity));
        }
        return null;
    }
}
