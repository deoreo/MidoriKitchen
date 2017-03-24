package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class CartModel {

    private String id ,menu;
    private int total, price;

    public CartModel() {
    }

    public CartModel(String id, String menu, int total, int price) {
        this.id = id;
        this.menu = menu;
        this.total = total;
        this.price = price;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
