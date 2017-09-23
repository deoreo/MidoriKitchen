package midori.kitchen.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class AppPrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MidoriKitchen";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_ID = "idaccount";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PHOTO = "photo";

    public AppPrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUser(String idaccount, String fullname, String email, String photo) {
        editor.putString(KEY_ID, idaccount);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHOTO, photo);
        editor.apply();
    }

    public void updateUser(String fullname, String email, String phone) {
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }
    public HashMap<String, String> getUser() {
        HashMap<String, String> dataUser = new HashMap<String, String>();
        dataUser.put(KEY_ID, pref.getString(KEY_ID, ""));
        dataUser.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, "User"));
        dataUser.put(KEY_EMAIL, pref.getString(KEY_EMAIL, "user@mail.com"));
        dataUser.put(KEY_PHOTO, pref.getString(KEY_PHOTO, ""));
        dataUser.put(KEY_PHONE, pref.getString(KEY_PHONE, "+62123456789"));
        return dataUser;
    }

    public void logout() {
        editor.remove(IS_LOGGED_IN);
        editor.remove(KEY_ID);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PHOTO);
        editor.remove(KEY_PHONE);
        editor.apply();
    }

}
