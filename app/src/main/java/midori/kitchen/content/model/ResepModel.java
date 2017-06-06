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
}
