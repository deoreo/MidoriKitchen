package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class MenuModel {

    private String id;
    private String menu;
    private String description;
    private String photo;
    private int stok;
    private String deliveryDate;

    private String promotion_code;

    private String ibuNama, ibuAlamat, ibuTelepon;
    private Double ibuLat, ibuLon;

    private int total_menu;
    private int price_menu;
    private int total_pay_menu;
    private int total_pay;
    private int delivery_price;

    public MenuModel() {
    }

    public MenuModel(String id, String menu, String description, String photo, int price, String deliveryDate) {
        this.id = id;
        this.menu = menu;
        this.description = description;
        this.photo = photo;
        this.price_menu = price;
        this.deliveryDate = deliveryDate;
    }

    public MenuModel(String id, String menu, String description, String photo, int price, int stok, String deliveryDate) {
        this.id = id;
        this.menu = menu;
        this.description = description;
        this.photo = photo;
        this.price_menu = price;
        this.stok = stok;
        this.deliveryDate = deliveryDate;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getIbuNama() {
        return ibuNama;
    }

    public void setIbuNama(String ibuNama) {
        this.ibuNama = ibuNama;
    }

    public String getIbuAlamat() {
        return ibuAlamat;
    }

    public void setIbuAlamat(String ibuAlamat) {
        this.ibuAlamat = ibuAlamat;
    }

    public String getIbuTelepon() {
        return ibuTelepon;
    }

    public void setIbuTelepon(String ibuTelepon) {
        this.ibuTelepon = ibuTelepon;
    }

    public Double getIbuLat() {
        return ibuLat;
    }

    public void setIbuLat(Double ibuLat) {
        this.ibuLat = ibuLat;
    }

    public Double getIbuLon() {
        return ibuLon;
    }

    public void setIbuLon(Double ibuLon) {
        this.ibuLon = ibuLon;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public int getTotal_menu() {
        return total_menu;
    }

    public void setTotal_menu(int total_menu) {
        this.total_menu = total_menu;
    }

    public int getPrice_menu() {
        return price_menu;
    }

    public void setPrice_menu(int price_menu) {
        this.price_menu = price_menu;
    }

    public int getTotal_pay_menu() {
        return total_pay_menu;
    }

    public void setTotal_pay_menu(int total_pay_menu) {
        this.total_pay_menu = total_pay_menu;
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
}
