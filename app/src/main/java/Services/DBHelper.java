package Services;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yudzh_000 on 22.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    public static DBHelper getInstance(Activity activity) {
        if(instance == null) {
            instance = new DBHelper(activity);
        }
        return instance;
    }

    public static SQLiteDatabase getDB(Activity activity) {
        if(instance == null) {
            getInstance(activity);
        }
        return instance.getWritableDatabase();
    }

    final String createGenresTypes = "CREATE TABLE Genres("+
            " id integer PRIMARY KEY AUTOINCREMENT," +
            " name text)";
    final String createSingersTable = "CREATE TABLE Singers(" +
            "id integer PRIMARY KEY AUTOINCREMENT," +
            "name text," +
            "description text," +
            "small_image_url text," +
            "big_image_url text," +
            "tracks integer," +
            "albums integer)";
    final String CreateSingerGenresTable = "CREATE TABLE GenresSinger ("+
            "id_singer integer," +
            "id_genres integer)";

    private DBHelper(Context context) {
        super(context, "main_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSingersTable);
        db.execSQL(createGenresTypes);
        db.execSQL(CreateSingerGenresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
