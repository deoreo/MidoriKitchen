package midori.chef.content.model;

/**
 * Created by BimoV on 3/11/2017.
 */

public class AddMenuModel {

    private String id;
    private String menu;
    private String description;
    private String photo;
    private int price;
    private String deliveryDate;

    public AddMenuModel() {
    }

    public AddMenuModel(String id, String menu, String description, String photo, int price, String deliveryDate) {
        this.id = id;
        this.menu = menu;
        this.description = description;
        this.photo = photo;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

}
