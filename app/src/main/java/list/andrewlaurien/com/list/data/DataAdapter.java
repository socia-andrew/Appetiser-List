package list.andrewlaurien.com.list.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

import list.andrewlaurien.com.list.ItemDetailActivity;
import list.andrewlaurien.com.list.ItemDetailFragment;
import list.andrewlaurien.com.list.R;
import list.andrewlaurien.com.list.api.model.Track;

/**
 * Created by Mayur on 6/25/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    Activity activity;
    Context context;
    private List<Track> trackList;
    private boolean mTwoPane;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout row;
        ImageView imgTrackArtwork;
        TextView txtTrackName, txtArtistNameNGenre, txtPrice;

        MyViewHolder(View view) {
            super(view);
            row = (LinearLayout) view.findViewById(R.id.item_row);
            imgTrackArtwork = (ImageView) view.findViewById(R.id.artwork);
            txtTrackName = (TextView) view.findViewById(R.id.track_name);
            txtArtistNameNGenre = (TextView) view.findViewById(R.id.artist_name_and_genre);
            txtPrice = (TextView) view.findViewById(R.id.price);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    Log.d("Position",""+pos);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID,pos);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, pos);

                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public DataAdapter(Activity activity, Context context, List<Track> trackList, boolean twoPane) {
        this.context = context;
        this.trackList = trackList;
        this.mTwoPane = twoPane;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Track track = trackList.get(position);

        final String artworkUrl = track.getArtworkUrl100();


        Glide.with(context).load(artworkUrl).apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground))
                .into(holder.imgTrackArtwork);

        holder.txtTrackName.setText(track.getTrackName());
        holder.txtArtistNameNGenre.setText(track.getArtistName() + " | " + track.getPrimaryGenreName());
        holder.txtPrice.setText(String.format("US $ %s", String.valueOf(track.getTrackPrice())));
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }
}