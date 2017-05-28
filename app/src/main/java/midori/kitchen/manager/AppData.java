package midori.kitchen.manager;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import midori.kitchen.content.model.BuyModel;
import midori.kitchen.content.model.CartModel;
import midori.kitchen.content.model.InvoiceModel;
import midori.kitchen.content.model.MenuModel;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class AppData {

    //tag fragment
    public static String backstack_tag = "";
    public static String menu_tag = "menu";
    public static String history_tag = "history_tag";
    public static String detail_menu_tag = "detail_menu";
    public static String checkout_review_tag = "checkout_review";
    public static String checkout_delivery_tag = "checkout_delivery";
    public static String checkout_voucher_tag = "checkout_voucher";
    public static String checkout_payment_tag = "checkout_payment";
    public static String backstack_buy_tag = "";
    public static String buy_review_tag = "buy_review";
    public static String buy_payment_tag = "buy_payment";

    public static HashMap<String, CartModel> cart = new HashMap<>();

    //public static BuyModel buyModel = new BuyModel();
    public static MenuModel menuModel = new MenuModel(

    );
    public static String id="0";
    public static String order_id = "INV-MK";
    public static String payment_id="0";
    public static String kupon_id="0";
    public static String delivery="0";
    public static String status="0";
    public static String note="0";
    public static String address="0";
    public static String locationDetail="0";
    public static double distance=0.0;
    public static LatLng latLngDelivery = new LatLng(0.0, 0.0);
    public static int priceDelivery = 8000;
    public static String delivery_id;
    public static String detail_address;
    public static String total_harga;
    public static ArrayList<MenuModel> menus = new ArrayList<>();
    public static String TAG = "MidoriKitchen Server";
    public static String Base64KeyMidoriKitchen = "MzMxNzIxNTY6VDNHVVhja3A4SzhtQzV6TXNlR0k=";
    public static String Base64KeyUser = "";
    public static String email_bukalapak = "";
    public static String password_bukalapak ="";
    public static String BukaDompetBalance = "1000";
    public static InvoiceModel invoiceModel = new InvoiceModel();
    public static String invoice_message = "Gagal menggunakan saldo";
    public static boolean isBukaDompet = true;
    public static String rekening_user = "11111111";
    public static String order_id_history;

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
