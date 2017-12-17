package reddit.haristimuno.com.reddit;

import com.google.gson.JsonObject;
import org.junit.Test;
import reddit.haristimuno.com.reddit.rest.ApiClient;
import reddit.haristimuno.com.reddit.rest.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;


public class ThemesTest {

    private ApiService apiService;
    private JsonObject themes;

    @Test
    public void themes_isCorrect() throws Exception {

        apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<JsonObject> call = apiService.getThemes();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                themes = response.body();
                assertEquals(25, themes.size());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                assertEquals(call, themes.size());
            }
        });
    }
}