package midori.kitchen.content.model;

/**
 * Created by deoreo06 on 06/06/2017.
 */

public class ResepModel {
    private String id;
    private String resep;
    private String cara_buat;
    private String image;
    private String url;
    private String bahan_id;
    private String bahan_nama;
    private String bahan_jumlah;

    public ResepModel(){

    }

    public ResepModel(String id, String resep, String cara_buat, String image, String url ){
        this.id = id;
        this.resep = resep;
        this.cara_buat = cara_buat;
        this.image = image;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResep() {
        return resep;
    }

    public void setResep(String resep) {
        this.resep = resep;
    }

    public String getCara_buat() {
        return cara_buat;
    }

    public void setCara_buat(String cara_buat) {
        this.cara_buat = cara_buat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBahan_jumlah() {
        return bahan_jumlah;
    }

    public void setBahan_jumlah(String bahan_jumlah) {
        this.bahan_jumlah = bahan_jumlah;
    }

    public String getBahan_id() {
        return bahan_id;
    }

    public void setBahan_id(String bahan_id) {
        this.bahan_id = bahan_id;
    }

    public String getBahan_nama() {
        return bahan_nama;
    }

    public void setBahan_nama(String bahan_nama) {
        this.bahan_nama = bahan_nama;
    }
}
