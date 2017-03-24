package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/12/2017.
 */

public class HistoryModel {
    private String id;
    private String menu;
    private String deliveryDate;
    private String status;
    private int total_pay;

    public HistoryModel() {
    }

    public HistoryModel(String id, String menu, int total_pay, String deliveryDate, String status) {
        this.id = id;
        this.menu = menu;
        this.total_pay = total_pay;
        this.deliveryDate = deliveryDate;
        this.status = status;
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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(int total_pay) {
        this.total_pay = total_pay;
    }
}
