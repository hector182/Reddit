package reddit.haristimuno.com.reddit.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reddit.haristimuno.com.reddit.model.ThemeEntity;
import reddit.haristimuno.com.reddit.model.Theme;

/**
 * Created by hector on 13-12-2017.
 */

public class Service {
    public static List<ThemeEntity> convert(JsonObject data) {
        List<ThemeEntity> themeList = new ArrayList<>();

        if (data != null) {
            JsonArray jsonArrayData = data.getAsJsonObject("data")
                    .getAsJsonArray("children");
            for (int i = 0; i < jsonArrayData.size(); i++) {
                JsonObject jsonObjectData = jsonArrayData.get(i).getAsJsonObject();
                JsonElement jsonTheme = jsonObjectData.get("data");
                themeList.add(new Gson().fromJson(jsonTheme, ThemeEntity.class));
            }
        } else {
            return Collections.emptyList();
        }

        return themeList;
    }

    public static List<Theme> getThemes(JsonObject data) {
        List<Theme> themeList = new ArrayList<>();
        Theme theme;

        if (data != null) {
            JsonArray jsonArrayData = data.getAsJsonObject("data")
                    .getAsJsonArray("children");
            for (int i = 0; i < jsonArrayData.size(); i++) {
                JsonObject jsonObjectData = jsonArrayData.get(i).getAsJsonObject();
                JsonElement jsonTheme = jsonObjectData.get("data");
                themeList.add(new Gson().fromJson(jsonTheme, Theme.class));
            }
        } else {
            return Collections.emptyList();
        }

        return themeList;
    }
}
