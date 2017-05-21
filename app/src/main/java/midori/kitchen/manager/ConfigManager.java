package midori.kitchen.manager;

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
    public static final String INIT = SERVER+"/init";
    public static final String FORGOT_PASSWORD = SERVER+"/forgot";
    public static final String RESEND_RESET_PASSWORD = SERVER+"/forgot/resend-token";
    public static final String CHECK_RESET_PASSWORD = SERVER+"/forgot/check";
    public static final String RESET_PASSWORD = SERVER+"/forgot/reset";
    public static final String MENU_SPEED = SERVER+"/menu/speed/";
    public static final String MENU_PREORDER = SERVER+"/menu/preorder/";
    public static final String ALL_MENU = SERVER+"/all_menus";
    public static final String MENU_DETAIL = SERVER+"/menu/";
    public static final String CATEGORY = SERVER+"/menu/categories";
    public static final String LIKE = SERVER+"/menu/like";
    public static final String DISLIKE = SERVER+"/menu/dislike";
    public static final String MENU_NEXT = SERVER+"/menu/next/";
    public static final String WISHLIST = SERVER+"/like";
    public static final String CALCULATE_PRICE = SERVER+"/calculate-price";
    public static final String CHECKOUT = SERVER+"/checkout";
    public static final String LOGOUT_ALL = SERVER+"/logout-all";
    public static final String REFRESH_TOKEN = SERVER+"/refresh-token";
    public static final String VERIFY_PHONE_NUMBER = SERVER+"/verify";
    public static final String RESEND_VERIFY_CODE = SERVER+"/resend-code";
    public static final String UPDATE_PROFILE = SERVER+"/profile";
    public static final String TRANSACTIONS = SERVER+"/transactions/";
    public static final String TRANSACTION = SERVER+"/transaction/";
    public static final String NOTIFICATION = SERVER+"/notif/";
    public static final String DETAIL_TRANSACTION = SERVER+"/transaction/";
    public static final String POST_DEVICE_TOKEN = SERVER+"/device-token";
    public static final String MIDORIKEY = "7f7cb3d190e243242834eff7aedc5272-6e10abda22870f81dcdb724a7231ea27.168b3f90f315be9aa222f898b8879f16";
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String GEOCODE_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyAHSIjLkUABvce1psAveGWwFOqSNJ6AT1A";
    public static final String URL_SUGGESTION = PLACES_API_BASE +"/autocomplete/json?components=country:id&key="+API_KEY+"&input=";

}
