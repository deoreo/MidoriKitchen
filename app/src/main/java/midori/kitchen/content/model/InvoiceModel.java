package midori.kitchen.content.model;

/**
 * Created by deoreo06 on 27/05/2017.
 */

public class InvoiceModel {

    String item_id, nama_penerima,  phone,
            province,  city, area,  address,  post_code,
            seller_id, buyer_notes,  password_bukadompet,  cart_id;

    public InvoiceModel(){

    }

    public InvoiceModel(String item_id, String nama_penerima, String phone,
                        String province, String city,String area, String address, String post_code,
                        String seller_id,String buyer_notes, String password_bukadompet, String cart_id){
        this.item_id = item_id;
        this.nama_penerima = nama_penerima;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.post_code = post_code;
        this.seller_id = seller_id;
        this.buyer_notes = buyer_notes;
        this.password_bukadompet = password_bukadompet;
        this.cart_id = cart_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getNama_penerima() {
        return nama_penerima;
    }

    public void setNama_penerima(String nama_penerima) {
        this.nama_penerima = nama_penerima;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getBuyer_notes() {
        return buyer_notes;
    }

    public void setBuyer_notes(String buyer_notes) {
        this.buyer_notes = buyer_notes;
    }

    public String getPassword_bukadompet() {
        return password_bukadompet;
    }

    public void setPassword_bukadompet(String password_bukadompet) {
        this.password_bukadompet = password_bukadompet;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
}
