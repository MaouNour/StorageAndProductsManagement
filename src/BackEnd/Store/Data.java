
package BackEnd.Store;

import BackEnd.DS.AVL;
import BackEnd.DS.Heap;

public class Data {
    public static double levelMoneySideOrders = 200;
    public static double levelMoneySideFreight = 200;
    public static AVL<Product> goods = new AVL<>();
    public static AVL<Freight> freight = new AVL<>();
    public static Heap<Orders> orders = new Heap<>();
}
