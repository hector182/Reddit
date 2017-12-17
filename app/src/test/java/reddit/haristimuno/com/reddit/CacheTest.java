package reddit.haristimuno.com.reddit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;
import reddit.haristimuno.com.reddit.cache.CacheStorage;
import reddit.haristimuno.com.reddit.rest.ApiClient;
import reddit.haristimuno.com.reddit.rest.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;


public class CacheTest {

    private ApiService apiService;
    private JsonObject themes;
    private CacheStorage cache;
    private Context ctx;
    private String file="themes";

    @Test
    public void cache_isCorrect() throws Exception {

        apiService =
                ApiClient.getClient().create(ApiService.class);

        cache = new CacheStorage(ctx);

        Call<JsonObject> call = apiService.getThemes();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                themes = response.body();
                String json = new Gson().toJson(themes);
                cache.save(file, json);
                String localMoviesJson = cache.get(file);
                assertEquals(json, localMoviesJson);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                assertEquals(call, themes.size());
            }
        });
    }
}