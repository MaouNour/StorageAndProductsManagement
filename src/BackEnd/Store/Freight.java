package BackEnd.Store;

import java.util.ArrayList;

import BackEnd.Basic.Date;

public class Freight extends BasicClass {
    private String destination;
    private ArrayList<Orders> orders;
    private double cost;
    private Date dateOfReceipt;

    public Freight(int ID, String destination, Date dateOfReceipt, ArrayList<Orders> orders) {
        super(ID);
        this.destination = destination;
        this.orders = orders;
        this.dateOfReceipt = dateOfReceipt;
        for (Orders order : orders) {
            this.cost += order.getCostTotal();
        }
    }

    public Freight(Freight freight) {
        super(freight.getID());
        this.destination = freight.getDestination();
        this.orders = freight.getOrders();
        this.dateOfReceipt = freight.getDateOfReceipt();
        for (Orders order : orders) {
            this.cost += order.getCostTotal();
        }
    }

    public String getDestination() {
        return destination;
    }

    public double getCost() {
        return cost;
    }

    public Date getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void updateDateOfReceipt(Date dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void updateOrders(Orders orders) {
        this.orders.add(orders);
        this.cost += orders.getCostTotal();
    }

    

    @Override
    public void print() {
        String freight;
        System.out.println();
        freight = "{ id : " + this.getID()
                + " , destination : " + this.destination
                + " , cost : " + this.cost
                + " , date of receipt : " + this.dateOfReceipt + " }";
        System.out.println(freight);
    }

    @Override
    public String toString() {
        String frieght = "";
        frieght += "\nID : " + this.getID();
        frieght += "\nDestination : " + this.destination;
        frieght += "\nDate of Receipt : " + this.dateOfReceipt;
        frieght += "\nCost Shipment : " + this.cost;
        frieght += "\nOrder : ";
        for (int i = 0; i < this.orders.size(); i++) {
            frieght += "\n    Order " + i + ". " + this.orders.get(i).toString();
        }
        return frieght;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDateOfReceipt(Date dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }
}
