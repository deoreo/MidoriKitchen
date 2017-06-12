package midori.kitchen.manager;

import android.util.Base64;

public class ConfigManager {

    public static final String FLURRY_API_KEY = "4CHJP5MW7736BSBJ7Q75";
    public static final String version ="~1";
    //Wifi Smart Rumah
    public static final String SERVER = "http://midorikitchen.top/midori_api/v1";


    //public static final String SERVER = "https://masaku.id:2083/user"; // prod
//    public static final String SERVER = "https://masaku.id:2053/user"; // dev
    //public static final String SERVER = "https://umkkf6ee2ac1.masaku.koding.io:2053/user"; // temp
    public static final String LOGIN = SERVER+"/user_login";
    public static final String REGISTER = SERVER+"/user_register";
    public static final String ORDER = SERVER+"/post_order";
    public static final String ALL_MENU = SERVER+"/all_menus";
    public static final String MENU_DETAIL = SERVER+"/menu/";
    public static final String MIDORIKEY = "7f7cb3d190e243242834eff7aedc5272-6e10abda22870f81dcdb724a7231ea27.168b3f90f315be9aa222f898b8879f16";
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String API_KEY = "AIzaSyAHSIjLkUABvce1psAveGWwFOqSNJ6AT1A";
    public static final String URL_SUGGESTION = PLACES_API_BASE +"/autocomplete/json?components=country:id&key="+API_KEY+"&input=";
    public static final String ORDERDETAIL = SERVER+"/post_order_detail";
    public static final String ORDERS = SERVER+"/orders";

    public static final String BL_GET_TOKEN = "https://api.bukalapak.com/v2/authenticate.json";
    public static final String BL_GET_MYLAPAK = "https://api.bukalapak.com/v2/products/mylapak.json";
    public static final String BL_READ_PRODUK = "https://api.bukalapak.com/v2/products/";
    public static final String IBU_PROFILE = SERVER+"/ibu_profile";
    public static final String UPDATESTATUS = SERVER+"/update_order_status" ;
    public static final String BL_REGISTER = "https://api.bukalapak.com/v2/users.json";
    public static final String BL_USER_SUMMARY = "https://api.bukalapak.com/v2/users/account_summary.json";
    public static final String BL_CART = "https://api.bukalapak.com/v2/carts/add_product/";
    public static final String BL_INVOICES = "https://api.bukalapak.com/v2/invoices.json" ;
    public static final String BL_DOMPET_HISTORY = "https://api.bukalapak.com/v2/dompet/history.json";
    public static final String BL_PROVINCE = "https://api.bukalapak.com/v2/address/provinces.json" ;
    public static final String BL_CITY = "https://api.bukalapak.com/v2/address/cities.json?province=";
    public static final String BL_AREA = "https://api.bukalapak.com/v2/address.json";
    public static final String BL_SHIPPING_FEE = "https://api.bukalapak.com/v2/shipping_fee.json";
    public static final String BL_SEARCH_PRODUK = "https://api.bukalapak.com/v2/products.json?&category_id=139&keywords=";
    public static final String ACCOUNT_SID = "ACa5faef99cdd025b3d768891c97741996";
    public static final String AUTH_TOKEN = "bce099324373753b37e6bb8be9c68331";
    public static final String TWILLIO_SMS = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";

    public static final String TWILLIO_CREDENTIALS = Base64.encodeToString(
            (ConfigManager.ACCOUNT_SID + ":" + ConfigManager.AUTH_TOKEN).getBytes(),
            Base64.NO_WRAP);
    public static final String mailcode = "RlU0NEg3ZGN1SQ==";
    public static final String RESEP = SERVER+"/all_resep";
    public static final String BAHAN = SERVER+"/bahan/";
    public static final String UPDATE_PROFILE =SERVER+"/update_profile" ;
}
