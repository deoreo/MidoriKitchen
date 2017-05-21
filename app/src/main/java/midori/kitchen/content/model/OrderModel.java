package midori.kitchen.content.model;

/**
 * Created by deoreo06 on 15/05/2017.
 */

public class OrderModel {
    private String id, payment, kupon, delivery, status, note, alamat, locationDetail;
    private Double lat, lon, jarak;

    public OrderModel(){

    }

    public OrderModel(String id, String alamat, String detailAlamat, Double lat, Double lon, Double jarak, String payment, String kupon, String delivery, String status, String note){
        this.id =id;
        this.alamat = alamat;
        this.locationDetail = detailAlamat;
        this.lat = lat;
        this.lon =lon;
        this.jarak = jarak;
        this.payment = payment;
        this.kupon = kupon;
        this.delivery = delivery;
        this.status = status;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getKupon() {
        return kupon;
    }

    public void setKupon(String kupon) {
        this.kupon = kupon;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getJarak() {
        return jarak;
    }

    public void setJarak(Double jarak) {
        this.jarak = jarak;
    }
}
