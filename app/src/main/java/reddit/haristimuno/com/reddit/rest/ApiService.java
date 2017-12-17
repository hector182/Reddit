package reddit.haristimuno.com.reddit.rest;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hector on 02-12-2017.
 */

public interface ApiService {
    @GET("reddits.json")
    Call<JsonObject> getThemes();
}
