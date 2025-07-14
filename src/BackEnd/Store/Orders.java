package BackEnd.Store;

import java.util.ArrayList;

import BackEnd.Basic.Priority;

public class Orders {
    private double costTotal;
    private String name;
    private ArrayList<Product> contentOrder;
    private Priority priority;

    public Orders(String name, Priority priority, ArrayList<Product> contentOrder) {
        this.name = name;
        this.priority = priority;
        this.contentOrder = contentOrder;
        for (Product product : contentOrder) {
            costTotal += product.getPrice();
        }
    }

    public Orders(Orders order) {
        this.name = order.getName();
        this.priority = order.getPriority();
        this.contentOrder = order.getContentOrder();
        this.costTotal = order.getCostTotal();
    }

    public Priority getPriority() {
        return priority;
    }

    public void updatePriority(Priority priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public double getCostTotal() {
        return costTotal;
    }

    public ArrayList<Product> getContentOrder() {
        return contentOrder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContentOrder(ArrayList<Product> contentOrder) {
        this.contentOrder = contentOrder;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        String order = "";
        order += "\nName : " + this.name;
        order += "\nPriority : " + this.priority;
        order += "\nCost Order: " + costTotal;
        for (int i = 0; i < this.contentOrder.size(); i++) {
            order += "\n  Products" + i + ". " + this.contentOrder.get(i).toString();
        }
        return order;
    }
}
