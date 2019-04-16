package list.andrewlaurien.com.list.api;


import list.andrewlaurien.com.list.api.model.TrackModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIService {

    @GET("search")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<TrackModel> getTracks(@Query("term") String term,@Query("country") String countrycode,@Query("media") String media);
}