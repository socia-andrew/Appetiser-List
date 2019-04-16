package list.andrewlaurien.com.list.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import list.andrewlaurien.com.list.api.model.Track;

/**
 * Created by andrew on 15/04/2019.
 */

@Database(entities = {Track.class}, version = 1, exportSchema = false)
public abstract class TrackDataBase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
