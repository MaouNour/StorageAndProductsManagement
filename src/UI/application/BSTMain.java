package UI.application;

import BackEnd.DS.BST;
import BackEnd.Exceptions.IDException;
import BackEnd.Store.Product;

public class BSTMain {
public static void main(String[] args) {
        Product [] products= {new Product(1, "ProductA", 100, 100), 
        new Product(5, "ProductE", 50.0, 500),
        new Product(4, "ProductD", 40.0, 200),
        new Product(3, "ProductC", 30.0, 300),
        new Product(2, "ProductB", 20.0, 200),
        new Product(0, "ProductB", 20.0, 200)};
        BST<Product> productTree = new BST<>();
        for (Product product : products) {
            try {
                productTree.add(product);
            } catch (IDException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Pre Order of products tree");
        productTree.preOrder();
        System.out.println("after removing the id 3");
        try {
            productTree.delet(3);
        } catch (IDException e) {
            e.printStackTrace();
        }
        productTree.preOrder();
        System.out.println("if removing non existant id:");
        try {
            productTree.delet(-10);
        } catch (IDException e) {
            e.printStackTrace();
        }
        productTree.preOrder();
        System.out.println("adding a new product to tree : (with existent id)");
        try {
            productTree.add(new Product(0, "ppppp", 90, 10));
        } catch (IDException e) {
            e.printStackTrace();
        }
        productTree.preOrder();
        System.out.println("adding a new product to tree : ");
        try {
            productTree.add(new Product(10, "prod", 88, 30));
        } catch (IDException e) {
            e.printStackTrace();
        }
        productTree.preOrder();
        
        
        

}
}