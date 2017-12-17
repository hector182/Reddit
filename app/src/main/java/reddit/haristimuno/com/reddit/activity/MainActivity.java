package reddit.haristimuno.com.reddit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import reddit.haristimuno.com.reddit.cache.CacheStorage;
import reddit.haristimuno.com.reddit.R;
import reddit.haristimuno.com.reddit.rest.Service;
import reddit.haristimuno.com.reddit.adapter.ThemeAdapter;
import reddit.haristimuno.com.reddit.model.Theme;
import reddit.haristimuno.com.reddit.rest.ApiClient;
import reddit.haristimuno.com.reddit.rest.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String THEMES_CACHE = "THEMES";
    private RecyclerView recyclerView;
    private ThemeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Theme> themes;
    private ProgressBar progressBar;
    private TextView generalError;
    private ApiService apiService;
    private SwipeRefreshLayout swipeContainer;
    private boolean isOnfreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiService =
                ApiClient.getClient().create(ApiService.class);

        initComponents();

        if(isOnline()) {
            loadServiceData();
        } else {
            loadlocalData();
        }
    }

    public void initComponents() {
        generalError = (TextView) findViewById(R.id.generalError);
        progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);

        generalError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        onRefreshContainer(swipeContainer);

        recyclerView = (RecyclerView) findViewById(R.id.themes_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        themes = new ArrayList<>();
    }

    public void onRefreshContainer(SwipeRefreshLayout swipeContainer){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isOnfreshing) {
                    if(isOnline()) {
                        loadServiceData();
                    } else {
                        loadlocalData();
                    }
                }
            }
        });
    }

    /**
     * method to load the data from cache
     * @return if the application has intenet connection
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void loadServiceData() {
        Call<JsonObject> call = apiService.getThemes();
        isOnfreshing = true;

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                themes = Service.getThemes(response.body());
                adapter = new ThemeAdapter(themes, R.layout.adapter_movies, getApplicationContext());
                recyclerView.setAdapter(adapter);

                CacheStorage c = new CacheStorage(getApplicationContext());
                String json = new Gson().toJson(themes);
                c.save(THEMES_CACHE, json);

                addListListener();
                progressBar.setVisibility(View.GONE);
                generalError.setVisibility(View.GONE);
                isOnfreshing = false;
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                generalError.setVisibility(View.VISIBLE);
                isOnfreshing = false;
            }
        });
    }

    /**
     * method to load the data from cache
     */
    public void loadlocalData() {
        isOnfreshing = true;
        progressBar.setVisibility(View.VISIBLE);
        generalError.setVisibility(View.GONE);
        CacheStorage c = new CacheStorage(getApplicationContext());
        themes = new Gson().fromJson(c.get(THEMES_CACHE), new TypeToken<List<Theme>>(){}.getType());

        if(themes==null || themes.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            generalError.setVisibility(View.VISIBLE);
            isOnfreshing = false;
            swipeContainer.setRefreshing(false);
            adapter.setList(themes);
            adapter.notifyDataSetChanged();
            return;
        }

        adapter = new ThemeAdapter(themes, R.layout.adapter_movies, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addListListener();

        progressBar.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
        isOnfreshing = false;
    }

    public void addListListener() {
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme theme = adapter.get(recyclerView.getChildAdapterPosition(v));
                Intent contentIntent = new Intent(getApplicationContext(),ContentActivity.class);
                contentIntent.putExtra("theme", theme);
                startActivity(contentIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.clear_cache) {
            deleteCache();
        }
        return false;
    }

    /**
     * delete local themes categories
     */
    public void deleteCache() {
        CacheStorage cache = new CacheStorage(getApplicationContext());
        cache.clear(THEMES_CACHE);
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);

        Snackbar.make(view, "Cache has been deleted", Snackbar.LENGTH_LONG)
                .show();

    }
}
