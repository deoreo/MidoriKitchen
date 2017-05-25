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
}
