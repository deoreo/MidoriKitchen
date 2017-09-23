package midori.kitchen.manager;

import java.util.HashMap;

import midori.kitchen.content.model.BuyModel;
import midori.kitchen.content.model.CartModel;

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
    public static String wallet_tag = "history_tag";

    public static HashMap<String, CartModel> cart = new HashMap<>();

    public static BuyModel buyModel = new BuyModel();
}
