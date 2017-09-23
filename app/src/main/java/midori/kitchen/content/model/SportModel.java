package midori.kitchen.content.model;

/**
 * Created by Januar on 9/24/2017.
 */

public class SportModel {
    private String id;
    private String name;
    private String address;
    private String chalenge;
    private String photo;
    private int price;

    public SportModel(){}

    public SportModel(String id, String name, int price, String chalenge, String address,String photo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.chalenge = chalenge;
        this.photo = photo;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChalenge() {
        return chalenge;
    }

    public void setChalenge(String chalenge) {
        this.chalenge = chalenge;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



}
