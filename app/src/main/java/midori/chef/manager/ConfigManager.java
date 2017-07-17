package midori.chef.manager;

/**
 * Created by BimoV on 5/23/2017.
 */

public class ConfigManager {
    public static String URL_MIDORI = "http://midorikitchen.top/midori_api/v1/";
    public static String URL_BUKALAPAK = "https://api.bukalapak.com/v2/";
    public static String KEYWORD = "keywords=";

    public static String GET_MY_LAPAK = URL_BUKALAPAK + "products/mylapak.json?" + KEYWORD;
    public static String CREATE_PRODUCT_IMAGE = URL_BUKALAPAK + "images.json";
    public static String CREATE_PRODUCT = URL_BUKALAPAK + "products.json";
    public static String LOGIN = URL_MIDORI + "ibu_login";
    public static String PRODUCT = "products/";
    public static String READ_PRODUCT = URL_BUKALAPAK + PRODUCT;
    public static String TRANSAKSI = URL_MIDORI + "ibu_orders/";
    public static String REGISTER = URL_MIDORI + "ibu_register";

    //https://api.bukalapak.com/v2/products/8aws21.json
}
