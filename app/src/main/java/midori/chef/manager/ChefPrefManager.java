package midori.chef.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by BimoV on 3/11/2017.
 */

public class ChefPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MidoriChef";
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    public ChefPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /*
    * LOGIN
    */
    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUser(String fullname, String address, String phone, String key) {
        editor.putString("name", fullname);
        editor.putString("address", address);
        editor.putString("phone", phone);
        editor.putString("key", key);
        editor.apply();
    }

    public HashMap<String, String> getUser() {
        HashMap<String, String> dataUser = new HashMap<String, String>();
        dataUser.put("name", pref.getString("name", ""));
        dataUser.put("address", pref.getString("address", ""));
        dataUser.put("phone", pref.getString("phone", ""));
        dataUser.put("key", pref.getString("key", ""));
        return dataUser;
    }
    public void logout() {
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.remove("name");
        editor.remove("address");
        editor.remove("phone");
        editor.remove("key");
        editor.apply();
    }

}
