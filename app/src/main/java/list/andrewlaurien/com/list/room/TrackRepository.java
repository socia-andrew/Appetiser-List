package list.andrewlaurien.com.list.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import list.andrewlaurien.com.list.api.model.Track;

/**
 * Created by andrew on 15/04/2019.
 */

public class TrackRepository {
    private String DB_NAME = "db_tracks";
    private TrackDataBase trackDataBase;

    public TrackRepository(Context context) {
        trackDataBase = Room.databaseBuilder(context, TrackDataBase.class, DB_NAME).build();
    }

    public void insertTask(final Track track) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                trackDataBase.daoAccess().insertTask(track);
                return null;
            }
        }.execute();
    }

    public void insertAllTask(final List<Track> track) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                trackDataBase.daoAccess().insertAllTracks(track);
                return null;
            }
        }.execute();
    }

    public List<Track> getAllTasks() {
        return trackDataBase.daoAccess().fetchAllTracks();
    }

    public void deleteAll() {
         trackDataBase.daoAccess().deleteAllTracks();
    }

}
