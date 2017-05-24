package midori.kitchen.manager;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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

import midori.kitchen.content.model.MenuModel;
import midori.kitchen.manager.ConfigManager;


public class JSONControl {
    private JSONResponse _JSONResponse;


    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }


    //API Bukalapak//
    public JSONObject getMyLapak() {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "getMyLapak");
        try {
            jsonObj = _JSONResponse.GETResponseMyLapak();
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject readProdukBukalapak(String idProduk) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "getMyLapak");
        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.BL_READ_PRODUK+idProduk+".json");
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }


    //API MidoriKitchen
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

    public JSONObject postIbuProfile(String name) {

        JSONObject jsonObj = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.IBU_PROFILE, ConfigManager.MIDORIKEY, params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
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

    public JSONObject getOrderUser(Activity activity) {
        AppPrefManager appPrefManager = new AppPrefManager(activity);
        JSONObject jsonObj = new JSONObject();

        try {
            Log.d("API_KEY", appPrefManager.getUserApiKey());
            jsonObj = _JSONResponse.GETResponseToken(ConfigManager.ORDERS, appPrefManager.getUserApiKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject postOrder(Activity activity, String payment_id, String kupon_id, String delivery_id,
                                String order_lat,String order_lon,
                                String order_jarak, String status_order, String order_note, String detail_address, String order_total_harga) {

        JSONObject jsonObj = null;
        AppPrefManager appPrefManager = new AppPrefManager(activity);
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("payment_id", payment_id));
            params.add(new BasicNameValuePair("kupon_id", kupon_id));
            params.add(new BasicNameValuePair("delivery_id", delivery_id));
            params.add(new BasicNameValuePair("order_lat", order_lat));
            params.add(new BasicNameValuePair("order_lon", order_lon));
            params.add(new BasicNameValuePair("order_jarak", ""+order_jarak));
            params.add(new BasicNameValuePair("status_order_id", status_order));
            params.add(new BasicNameValuePair("order_note", order_note));
            params.add(new BasicNameValuePair("order_detail_address", detail_address));
            params.add(new BasicNameValuePair("order_total_harga", order_total_harga));
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.ORDER, appPrefManager.getUserApiKey(), params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /*
    public JSONObject postOrderDetail(String order_id, ArrayList<MenuModel> menuModels) {
        JSONObject jsonObj = null;
        for(int i=0;i<menuModels.size();i++) {

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("order_id", order_id));
                params.add(new BasicNameValuePair("menu_id", menuModels.get(i).getId()));
                params.add(new BasicNameValuePair("order_detail_jumlah", "" + menuModels.get(i).getTotal_menu()));
                jsonObj = _JSONResponse.POSTResponse(ConfigManager.ORDERDETAIL, params);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObj;
    }*/

    public void postOrderDetail(String order_id, ArrayList<MenuModel> menuModels) {

        for(int i=0;i<menuModels.size();i++) {

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("order_id", order_id));
                params.add(new BasicNameValuePair("menu_id", menuModels.get(i).getId()));
                params.add(new BasicNameValuePair("order_detail_jumlah", "" + menuModels.get(i).getTotal_menu()));
                _JSONResponse.POSTResponse(ConfigManager.ORDERDETAIL, params);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject updateStatus(Activity activity, String order_id, String status_id) {

        JSONObject jsonObject = null;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("order_id", order_id));
            params.add(new BasicNameValuePair("status_id", status_id));
            jsonObject = _JSONResponse.POSTResponse(ConfigManager.UPDATESTATUS, AppPrefManager.getInstance(activity).getUserApiKey(), params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
