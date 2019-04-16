package list.andrewlaurien.com.list;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import list.andrewlaurien.com.list.api.model.Track;
import list.andrewlaurien.com.list.data.DataAdapter;
import list.andrewlaurien.com.list.data.DataListContract;
import list.andrewlaurien.com.list.data.DataPresenter;
import list.andrewlaurien.com.list.room.TrackRepository;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements DataListContract.View {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    //private static final String url = "https://itunes.apple.com/search?term=star&amp;country=au&amp;media=movie";
    public static List<Track> dataTracks = new ArrayList<>();
    ShimmerRecyclerView item_list;

    DataPresenter presenter;
    DataAdapter adapter;
    Context context;
    FrameLayout frameLayout;

    public static TrackRepository trackRepository;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tvgreetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        context = this;

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        presenter = new DataPresenter(this);
        item_list = findViewById(R.id.item_list);

        adapter = new DataAdapter(this, context, dataTracks, mTwoPane);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        item_list.setLayoutManager(mLayoutManager);
        item_list.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        item_list.setItemAnimator(new DefaultItemAnimator());
        item_list.setAdapter(adapter);

        frameLayout = findViewById(R.id.frameLayout);

        trackRepository = new TrackRepository(getApplicationContext());

        sharedPreferences = getSharedPreferences("Last_Visit", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Calendar calendar = Calendar.getInstance();
        editor.putLong("time", calendar.getTimeInMillis()).apply();

        tvgreetings = findViewById(R.id.tvgreetings);

        getPreviousTracks();
        getLastVisit();


    }

    //get Last visit date of user
    //used sharedpreferences for this one because it is only lightweight data.
    public void getLastVisit() {

        if (sharedPreferences.contains("time")) {
            Long last_visit = sharedPreferences.getLong("time", 0);
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(last_visit));
            tvgreetings.setVisibility(View.VISIBLE);
            tvgreetings.setText(" Welcome Back " + dateString);
        } else {
            tvgreetings.setVisibility(View.GONE);
        }

    }

    //Fetch previous data to display
    public void getPreviousTracks() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                List<Track> mytracks = trackRepository.getAllTasks();

                if (mytracks.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayTracks(mytracks);
                        }
                    });
                }

            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void search(final String strTerm) {
        dataTracks.clear();
        adapter.notifyDataSetChanged();
        setLoadingIndicator(true);
        item_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getTracks(strTerm);
            }
        }, 2000);
    }


    @Override
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            item_list.showShimmerAdapter();
        } else {
            item_list.hideShimmerAdapter();
        }
    }


    @Override
    public void displayTracks(List<Track> dataTracks) {
        setLoadingIndicator(false);


        this.dataTracks.clear();
        this.dataTracks.addAll(dataTracks);
        adapter.notifyDataSetChanged();


        //Used room library to save search data
        //I select room because it is easy to use.
        //Save new result to room database
        //and delete the previous data.
        new Thread(new Runnable() {

            @Override
            public void run() {
                trackRepository.deleteAll();
                trackRepository.insertAllTask(dataTracks);
            }
        }).start();

    }

    @Override
    public void displayMessage(String message) {
        setLoadingIndicator(false);
        Snackbar.make(frameLayout, message, Snackbar.LENGTH_LONG).show();
    }

}

