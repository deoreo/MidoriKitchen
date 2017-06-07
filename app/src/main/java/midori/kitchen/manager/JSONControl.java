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

import static midori.kitchen.manager.AppData.address;


public class JSONControl {
    private JSONResponse _JSONResponse;


    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }


    //API Bukalapak//

    public JSONObject bukalapakGetToken(String username, String password) {
        JSONObject jsonObj = new JSONObject();
        String base64EncodedCredentials = Base64.encodeToString(
                (username+":"+password).getBytes(),
                Base64.NO_WRAP);
        Log.v("Bukalapak", "bukalapakgetToken");
        try {
            jsonObj = _JSONResponse.POSTResponse(ConfigManager.BL_GET_TOKEN, base64EncodedCredentials);
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakUserSummary(String Base64UserKey) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakUserSummary");
        try {
            jsonObj = _JSONResponse.GETResponseBukalapak(ConfigManager.BL_USER_SUMMARY, Base64UserKey);
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakRegister(String email,String username, String name, String password) {

        JSONObject jsonObj = null;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("name", name);
            jsonObject.put("password", password);
            jsonObject.put("password_confirmation", password);
            jsonObject.put("policy", "1");
            jsonObject.put("referer", "midorikitchen");
            jsonObj = _JSONResponse.POSTJsonResponse(ConfigManager.BL_REGISTER, jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject bukalapakMidoriLapak(String Base64Key) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakMidoriLapak");
        try {
            jsonObj = _JSONResponse.GETResponseBukalapak(ConfigManager.BL_GET_MYLAPAK,Base64Key);
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakAddCart(String idProduk, int jumlah) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakAddCart");
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("quantity", ""+jumlah));
            jsonObj = _JSONResponse.POSTBukalapakResponse(ConfigManager.BL_CART+idProduk+".json", AppData.Base64KeyUser, params);
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakCreateInvoice(int item_id, String nama_penerima, String phone,
                                             String province, String city,String area, String address, String post_code,
                                             int seller_id,String buyer_notes, String password_bukadompet,
                                             int cart_id
                                             ) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakCreateInvoice");
        try {
            JSONObject createInvoice = new JSONObject();
            JSONObject payment_invoice = new JSONObject();
            payment_invoice.put("shipping_name", nama_penerima);
            payment_invoice.put("phone", phone);
            JSONObject address_attributes = new JSONObject();
            address_attributes.put("province", province);
            address_attributes.put("city", city);
            address_attributes.put("area", area);
            address_attributes.put("address", address);
            address_attributes.put("post_code", post_code);
            payment_invoice.put("address", address_attributes);
            JSONArray parent_transactions_attributes = new JSONArray();
            JSONObject transactions_attributes = new JSONObject();
            transactions_attributes.put("seller_id", seller_id);
            transactions_attributes.put("courier", "gojek");
            transactions_attributes.put("buyer_notes", buyer_notes);
            parent_transactions_attributes.put(transactions_attributes);
            JSONArray item_ids = new JSONArray();
            item_ids.put(item_id);
            transactions_attributes.put("item_ids", item_ids);
            payment_invoice.put("transactions_attributes", parent_transactions_attributes);
            createInvoice.put("payment_invoice", payment_invoice);
            createInvoice.put("payment_method", "deposit");
            createInvoice.put("deposit_password", password_bukadompet);
            createInvoice.put("cart_id", cart_id);
            jsonObj = _JSONResponse.POSTBukalapakJSONResponse(ConfigManager.BL_INVOICES, AppData.Base64KeyUser, createInvoice);
            Log.v("Bukalapak createInvoice",createInvoice.toString());
            Log.v("Bukalapak BResponse",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakReadProduct(String idProduk) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakMidoriLapak");
        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.BL_READ_PRODUK+idProduk+".json");
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakSearchProduct(String name) {
        JSONObject jsonObj = new JSONObject();
        try {
//            jsonObj = _JSONResponse.GETResponse(URLEncoder.encode(ConfigManager.BL_SEARCH_PRODUK+name, "UTF-8"));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            jsonObj = _JSONResponse.GETResponse(ConfigManager.BL_SEARCH_PRODUK+URLEncoder.encode(name, "UTF-8"));
            Log.v("Bukalapak",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public JSONObject bukalapakGetProvince() {
        JSONObject jsonObj = new JSONObject();
        Log.v("Bukalapak", "bukalapakMidoriLapak");
        try {
            jsonObj = _JSONResponse.GETResponse(ConfigManager.BL_PROVINCE);
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

    public JSONObject getBahan(String idBahan) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj = _JSONResponse.GETResponseToken(ConfigManager.BAHAN+idBahan, ConfigManager.MIDORIKEY);
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


    public JSONObject getResep() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj = _JSONResponse.GETResponseToken(ConfigManager.RESEP, ConfigManager.MIDORIKEY);
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
                params.add(new BasicNameValuePair("ibu_id", menuModels.get(i).getIbuId()));
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

    public JSONObject BukalapakGetCity(String province) {
        JSONObject jsonObj = null;
        try {
            String url = ConfigManager.BL_CITY +
                    URLEncoder.encode(province, "utf8");
            Log.d("url", url);
            jsonObj = _JSONResponse.GETResponse(url);
        } catch (Exception e) {

        }

        return jsonObj;

    }

    public JSONObject BukalapakGetProvinces() {
        JSONObject jsonObj = null;
        try {
            String url = ConfigManager.BL_PROVINCE;
            Log.d("url", url);
            jsonObj = _JSONResponse.GETResponse(url);
        } catch (Exception e) {

        }

        return jsonObj;
    }

    public JSONObject BukalapakGetArea() {
        JSONObject jsonObj = null;
        try {
            String url = ConfigManager.BL_AREA;
            Log.d("url", url);
            jsonObj = _JSONResponse.GETResponse(url);
        } catch (Exception e) {

        }

        return jsonObj;
    }

    public JSONObject sendSMS(String base64Auth, String from, String to, String body) {
        JSONObject jsonObj = new JSONObject();
        Log.v("Twillio", "SMS Twillio");
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("From", ""+from));
            params.add(new BasicNameValuePair("To", ""+to));
            params.add(new BasicNameValuePair("Body", ""+to));
            jsonObj = _JSONResponse.POSTBukalapakResponse(ConfigManager.TWILLIO_SMS, base64Auth, params);
            Log.v("Twillio",jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }
}
