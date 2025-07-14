package BackEnd.Store;

import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Exceptions.NegativePriceException;

public class Product extends BasicClass {
    private String name;
    private double price;
    private double availableAmount;

    public Product(int ID, String name, double price, double availableAmount) {
        super(ID);
        this.name = name;
        this.price = price;
        this.availableAmount = availableAmount;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice(double price) throws NegativePriceException {
        if (price <= 0)
            throw new NegativePriceException("Price mustn't containe negative number or zero");
        this.price = price;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void updateAvailableAmount(double availableAmount) throws IllegalAvailableAmountException {
        if (availableAmount <= 0)
            throw new IllegalAvailableAmountException("There must be one piece of each item.");
        else if (availableAmount > 1000)
            throw new IllegalAvailableAmountException("The quantity exceeded the required limit.");
        else
            this.availableAmount = availableAmount;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return this.availableAmount < 1;
    }

    @Override
    public void print() {
        System.out.println();
        System.out.printf("id : %d , name : %s , price : %f , availavle amount : %f ",
                this.getID(),
                this.name,
                this.price,
                this.availableAmount);
        System.out.println();
    }

    @Override
    public String toString() {
        String product = "";
        product += "\nID : " + this.getID();
        product += "\nName : " + this.name;
        product += "\nPrice : " + this.price;
        product += "\nAvailable Amount : " + this.availableAmount;
        return product;
    }

    public void setName(String text) {
        name = text;
    }
}
