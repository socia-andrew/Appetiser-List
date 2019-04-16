package list.andrewlaurien.com.list.data;


import java.util.List;

import list.andrewlaurien.com.list.api.model.Track;


public class DataListContract {

    public interface View {
        void displayMessage(String message);

        void setLoadingIndicator(boolean isLoading);

        void displayTracks(List<Track> dataTracks);
    }

    public interface Presenter {
        void getTracks(String term);
    }
}