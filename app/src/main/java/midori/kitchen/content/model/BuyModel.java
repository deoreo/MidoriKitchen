package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class BuyModel {

    private String menu, notes, address, location_detail, promotion_code;
    private int total_menu;
    private int price_menu;
    private int total_pay_menu;
    private int total_pay;
    private int delivery_price;

    public BuyModel() {

    }

    public BuyModel(String menu, int total_menu, String notes, String address, String location_detail, String promotion_code) {
        this.menu = menu;
        this.total_menu = total_menu;
        this.notes = notes;
        this.address = address;
        this.location_detail = location_detail;
        this.promotion_code = promotion_code;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getTotal_menu() {
        return total_menu;
    }

    public void setTotal_menu(int total_menu) {
        this.total_menu = total_menu;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation_detail() {
        return location_detail;
    }

    public void setLocation_detail(String location_detail) {
        this.location_detail = location_detail;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public int getPrice_menu() {
        return price_menu;
    }

    public void setPrice_menu(int price_menu) {
        this.price_menu = price_menu;
    }

    public int getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(int total_pay) {
        this.total_pay = total_pay;
    }

    public int getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(int delivery_price) {
        this.delivery_price = delivery_price;
    }

    public int getTotal_pay_menu() {
        return total_pay_menu;
    }

    public void setTotal_pay_menu(int total_pay_menu) {
        this.total_pay_menu = total_pay_menu;
    }
}
