package Entities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Services.DBHelper;

/**
 * Created by yudzh_000 on 07.04.2016.
 */
public class Singer implements Serializable, Comparable<Singer>{
    String name;
    List<String> genres = new ArrayList<>();
    int tracks;
    int albums;
    //String link;
    String description;
    String smallImageUrl;
    String bigImageUrl;

    public Singer() {
    }

    public static Singer initFromJSON(JSONObject json) throws JSONException {
        Singer singer = new Singer();
        singer.name = json.getString("name");
        for (int i = 0; i < json.getJSONArray("genres").length(); i++) {
            singer.genres.add(json.getJSONArray("genres").getString(i));
        }
        singer.tracks = json.getInt("tracks");
        singer.albums = json.getInt("albums");
//        singer.link = json.getString("link");
        singer.description = json.getString("description");
        singer.description = singer.description.substring(0,1).toUpperCase() +
                singer.description.substring(1);
        singer.smallImageUrl = json.getJSONObject("cover").getString("small");
        singer.bigImageUrl = json.getJSONObject("cover").getString("big");

        return singer;
    }

    public static List<Singer> initFromJSON(JSONArray jsonArray) throws JSONException {
        List<Singer> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(initFromJSON(jsonArray.getJSONObject(i)));
        }
        return result;
    }

    public static Singer initFromCursor(Cursor cursor) {
        Singer singer = new Singer();
        singer.name = cursor.getString(1);
        singer.description = cursor.getString(2);
        singer.description = cursor.getString(2);
        singer.smallImageUrl = cursor.getString(3);
        singer.bigImageUrl = cursor.getString(4);
        singer.tracks = cursor.getInt(5);
        singer.albums = cursor.getInt(6);
        return singer;
    }

    public static List<Singer> getAllFromDB(Activity activity) {

        SQLiteDatabase db = DBHelper.getDB(activity);

        Cursor cursor = db.rawQuery("Select * from Singers", null);
        List<Singer> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Singer singer = initFromCursor(cursor);
            Cursor genresCursor = db.rawQuery("Select id_genres  from GenresSinger where id_singer = ?"
                    ,new String[]{cursor.getString(0)});
            while (genresCursor.moveToNext()) {
                Cursor genre = db.rawQuery("Select name from Genres where id = ?"
                    ,new String[]{genresCursor.getString(0)});
                if(genre.moveToFirst())
                singer.genres.add(genre.getString(0));
                genre.close();
            }
            genresCursor.close();
            result.add(singer);
        }
        cursor.close();
        return result;
    }

    public void putIntoDB(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("tracks", tracks);
        contentValues.put("albums", albums);
        contentValues.put("description", description);
        contentValues.put("small_image_url", smallImageUrl);
        contentValues.put("big_image_url", bigImageUrl);
        long singerID = db.insert("Singers", null, contentValues);
        for (String genre : genres) {
            Cursor cursor = db.rawQuery("Select id from Genres where name = ?", new String[]{genre});
            if (!cursor.moveToFirst()) {
                ContentValues putGenres = new ContentValues();
                putGenres.put("name", genre);
                db.insert("Genres", null, putGenres);
                cursor.close();
                cursor = db.rawQuery("Select id from Genres where name = ?", new String[]{genre});
                cursor.moveToFirst();
            }

            int genreID = cursor.getInt(0);
            cursor.close();
            ContentValues genres = new ContentValues();
            genres.put("id_singer", singerID);
            genres.put("id_genres", genreID);
            db.insert("GenresSinger", null, genres);
        }

    }

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }


    public String getViewableTracksAndAlbums() {
       StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append(this.albums);
       stringBuilder.append(" альбомов, ");
       stringBuilder.append(this.tracks);
       stringBuilder.append(" песен");

       return stringBuilder.toString();
    }

    public String getViewableGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : genres) {
            stringBuilder.append(s);
            stringBuilder.append(", ");
        }
        if(stringBuilder.length()>1) {
            return stringBuilder.substring(0, stringBuilder.length() - 2);
        }
        else {
            return stringBuilder.toString();
        }
    }

    public String getDescription() {
        return description;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Singer singer = (Singer) o;

        if (tracks != singer.tracks) return false;
        if (albums != singer.albums) return false;
        if (name != null ? !name.equals(singer.name) : singer.name != null) return false;
        if (genres != null ? !genres.equals(singer.genres) : singer.genres != null) return false;
        if (description != null ? !description.equals(singer.description) : singer.description != null)
            return false;
        if (smallImageUrl != null ? !smallImageUrl.equals(singer.smallImageUrl) : singer.smallImageUrl != null)
            return false;
        return !(bigImageUrl != null ? !bigImageUrl.equals(singer.bigImageUrl) : singer.bigImageUrl != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + tracks;
        result = 31 * result + albums;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (smallImageUrl != null ? smallImageUrl.hashCode() : 0);
        result = 31 * result + (bigImageUrl != null ? bigImageUrl.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Singer another) {
        return this.name.compareTo(another.name);
    }
}
