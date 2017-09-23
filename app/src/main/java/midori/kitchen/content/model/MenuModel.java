package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class MenuModel {

    private String id;
    private String menu;
    private String description;
    private String photo;
    private int price;
    private String owner;
    private int calories;

    public MenuModel() {
    }

    public MenuModel(String id, String menu, String description, String photo, int price, String owner, int calories) {
        this.id = id;
        this.menu = menu;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.owner = owner;
        this.calories = calories;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
