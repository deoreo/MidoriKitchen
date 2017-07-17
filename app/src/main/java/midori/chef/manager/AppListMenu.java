package midori.chef.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import midori.chef.content.model.MenuModel;

/**
 * Created by BimoV on 3/12/2017.
 */

public class AppListMenu {
    private String SERIALIZED_ITEMS="ListMenu";
    private static final String PREF_NAME = "MenuChef";

    public AppListMenu() {
        super();
    }

    public void saveData(Context context, List<MenuModel> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(SERIALIZED_ITEMS, jsonFavorites);

        editor.commit();
    }

    public void addData(Context context, MenuModel product) {
        List<MenuModel> favorites = getData(context);
        if (favorites == null)
            favorites = new ArrayList<MenuModel>();
        favorites.add(product);
        saveData(context, favorites);
    }
    public ArrayList<MenuModel> getData(Context context) {
        SharedPreferences settings;
        List<MenuModel> favorites;

        settings = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(SERIALIZED_ITEMS)) {
            String jsonFavorites = settings.getString(SERIALIZED_ITEMS, null);
            Gson gson = new Gson();
            MenuModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    MenuModel[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<MenuModel>(favorites);
        } else
            return null;

        return (ArrayList<MenuModel>) favorites;
    }
}
