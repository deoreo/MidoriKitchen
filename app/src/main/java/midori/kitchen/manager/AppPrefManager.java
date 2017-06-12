package midori.kitchen.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import java.util.HashMap;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class AppPrefManager {
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";
    private static final String KEY_AREA = "area";
    private static final String KEY_DELIVERY_ADDRESS = "delivery_address";
    private static final String KEY_LOCATION_DETAIL = "location_detail" ;
    private static AppPrefManager sInstance = null;
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
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_POST_CODE = "kode_pos";
    public static final String USER_API_KEY = "user_api_key";

    public AppPrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static AppPrefManager getInstance(Context ctx) {
        if (sInstance == null)
            sInstance = new AppPrefManager(ctx);
        return sInstance;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUser(String idaccount, String fullname, String email, String telepon, String delivery_adress, String photo) {
        editor.putString(KEY_ID, idaccount);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHOTO, photo);
        editor.putString(KEY_PHONE, telepon);
        editor.putString(KEY_DELIVERY_ADDRESS, delivery_adress);
        editor.apply();
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
        dataUser.put(KEY_FULLNAME, pref.getString(KEY_FULLNAME, ""));
        dataUser.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        dataUser.put(KEY_PHOTO, pref.getString(KEY_PHOTO, ""));
        dataUser.put(KEY_PHONE, pref.getString(KEY_PHONE, ""));
        dataUser.put(KEY_ALAMAT, pref.getString(KEY_ALAMAT, ""));
        dataUser.put(KEY_AREA, pref.getString(KEY_AREA, ""));
        dataUser.put(KEY_CITY, pref.getString(KEY_CITY, ""));
        dataUser.put(KEY_PROVINCE, pref.getString(KEY_PROVINCE, ""));
        dataUser.put(KEY_LOCATION_DETAIL, pref.getString(KEY_LOCATION_DETAIL, ""));
        dataUser.put(KEY_DELIVERY_ADDRESS, pref.getString(KEY_DELIVERY_ADDRESS, ""));
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


    public void setUserApiKey(String api) {
        editor.putString(USER_API_KEY, api);
        editor.commit();
    }

    public String getUserApiKey(){
        return pref.getString(USER_API_KEY, "");
    }



    public void setAlamat(String alamat) {
        try {
            editor.putString(KEY_ALAMAT, alamat);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getAlamat() {
        return pref.getString(KEY_ALAMAT, null);
    }


    public void setProvince(String str) {
        try {
            editor.putString(KEY_PROVINCE, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getProvince() {
        return pref.getString(KEY_PROVINCE, null);
    }

    public void setCity(String str) {
        try {
            editor.putString(KEY_CITY, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getCity() {
        return pref.getString(KEY_CITY, null);
    }
    public void setArea(String str) {
        try {
            editor.putString(KEY_AREA, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getArea() {
        return pref.getString(KEY_AREA, null);
    }

    public void setLocationDetail(String str) {
        try {
            editor.putString(KEY_LOCATION_DETAIL, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getLocationDetail() {
        return pref.getString(KEY_LOCATION_DETAIL, null);
    }

    public void setDeliveryAddress(String str) {
        try {
            editor.putString(KEY_DELIVERY_ADDRESS, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getDeliveryAddress() {
        return pref.getString(KEY_DELIVERY_ADDRESS, null);
    }


    public void setPostCode(String str) {
        try {
            editor.putString(KEY_POST_CODE, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        editor.commit();
    }
    public String getPostCode() {
        return pref.getString(KEY_POST_CODE, null);
    }


    public void setGeocode(LatLng geocode){
        Gson gson = new Gson();
        String dataJson = gson.toJson(geocode);
        editor.putString("geocode", dataJson);
        editor.commit();

    }

    public LatLng getGeocode(){
        Gson gson = new Gson();
        String json = pref.getString("geocode", null);
        LatLng obj = gson.fromJson(json, LatLng.class);
        return obj;
    }

}
