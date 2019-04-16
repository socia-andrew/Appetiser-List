package list.andrewlaurien.com.list.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import list.andrewlaurien.com.list.api.model.Track;
import list.andrewlaurien.com.list.api.model.TrackModel;

/**
 * Created by andrew on 15/04/2019.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertTask(Track track);

    @Insert
    void insertAllTracks(List<Track> tracks);

    @Query("SELECT * FROM Track ORDER BY trackId desc")
    List<Track> fetchAllTracks();

    @Query("DELETE FROM Track")
    public void deleteAllTracks();

}
