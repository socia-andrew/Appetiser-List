package list.andrewlaurien.com.list.data;



import android.util.Log;

import list.andrewlaurien.com.list.api.APIService;
import list.andrewlaurien.com.list.api.ServiceFactory;
import list.andrewlaurien.com.list.api.model.TrackModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataPresenter implements DataListContract.Presenter {

    private DataListContract.View listView;

    public DataPresenter(DataListContract.View lView) {
        this.listView = lView;
    }

    @Override
    public void getTracks(String term) {
        APIService service = ServiceFactory.getInstance();
        Call<TrackModel> trackModelCall = service.getTracks(term,"au","movie");
        trackModelCall.enqueue(new Callback<TrackModel>() {
            @Override
            public void onResponse(Call<TrackModel> call, Response<TrackModel> response) {
                Log.d("Response",""+ response.body());
                TrackModel trackModel = response.body();
                if (trackModel.getResultCount() > 0) {
                    listView.displayTracks(trackModel.getTracks());
                } else {
                    listView.displayMessage("No data found, Try again.");
                }
            }

            @Override
            public void onFailure(Call<TrackModel> call, Throwable t) {
                listView.displayMessage("No data found, Try again.");
            }
        });
    }
}