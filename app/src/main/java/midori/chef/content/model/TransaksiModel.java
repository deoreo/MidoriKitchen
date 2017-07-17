package midori.chef.content.model;

/**
 * Created by BimoV on 3/10/2017.
 */

public class TransaksiModel {
    private String id;
    private String menu;
    private String jumlah_pesan;
    private String harga;
    private String status;
    private String avatar;
    private String note;


    public TransaksiModel() {

    }

    public TransaksiModel(String id,String menu, String jumlah_pesan, String harga, String status, String avatar) {
        this.id = id;
        this.menu = menu;
        this.jumlah_pesan = jumlah_pesan;
        this.harga = harga;
        this.status = status;
        this.avatar = avatar;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getJumlah_pesan() {
        return jumlah_pesan;
    }

    public void setJumlah_pesan(String jumlah_pesan) {
        this.jumlah_pesan = jumlah_pesan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
