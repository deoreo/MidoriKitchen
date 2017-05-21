package midori.kitchen.manager;

import android.app.Activity;
import android.util.Log;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import midori.kitchen.manager.ConfigManager;


public class JSONControl {
    private JSONResponse _JSONResponse;


    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }

    public JSONArray listPlace(String addressInput) {
        JSONArray json = null;
        JSONObject jsonObj = null;
        try {
            String url = ConfigManager.URL_SUGGESTION +
                    URLEncoder.encode(addressInput, "utf8");
            Log.d("url", url);


            jsonObj = _JSONResponse.GETResponse(url);
            json = jsonObj.getJSONArray("predictions");
        } catch (ConnectException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (JSONException e) {
        }
        return json;

    }

    public JSONObject postLogin(String email, String password) {

        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.LOGIN, ConfigManager.MIDORIKEY, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject postRegister(String name, String email, String password,String phone, String address) {

        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("address", address));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.REGISTER, ConfigManager.MIDORIKEY, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getAllMenus(Activity activity) {
        AppPrefManager appPrefManager = new AppPrefManager(activity);
        JSONObject jsonObj = new JSONObject();

        try {
            Log.d("API_KEY", appPrefManager.getUserApiKey());
            jsonObj = _JSONResponse.GETResponseToken(ConfigManager.ALL_MENU, appPrefManager.getUserApiKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getMenuDetail(Activity activity, String idMenu) {
        AppPrefManager appPrefManager = new AppPrefManager(activity);
        JSONObject jsonObj = new JSONObject();

        try {
            Log.d("API_KEY", appPrefManager.getUserApiKey());
            jsonObj = _JSONResponse.GETResponseToken(ConfigManager.MENU_DETAIL+idMenu, appPrefManager.getUserApiKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

}
