package BackEnd;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import BackEnd.Basic.Date;
import BackEnd.Basic.Priority;
import BackEnd.DS.AVL;
import BackEnd.DS.NodeAVL;
import BackEnd.Exceptions.ExceedingBudgetLimitsException;
import BackEnd.Exceptions.IDException;
import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Exceptions.NegativePriceException;
import BackEnd.Exceptions.PastDatesException;
import BackEnd.Store.BasicClass;
import BackEnd.Store.Data;
import BackEnd.Store.Freight;
import BackEnd.Store.Orders;
import BackEnd.Store.Product;

public interface Service {
    static Scanner scan = new Scanner(System.in);

    public static void createData() throws PastDatesException, IDException, IllegalAvailableAmountException {
        Product p1 = new Product(1, "ProductA", 100, 100);
        Product p2 = new Product(5, "ProductE", 50.0, 500);
        Product p3 = new Product(4, "ProductD", 40.0, 200);
        Product p4 = new Product(3, "ProductC", 30.0, 300);
        Product p5 = new Product(2, "ProductB", 20.0, 200);
        Product p6 = new Product(0, "ProductB", 20.0, 200);
        Product p7 = new Product(55, "ggg", 10.0, 10);

        Data.goods.add(p1);
        Data.goods.add(p2);
        Data.goods.add(p3);
        Data.goods.add(p4);
        Data.goods.add(p5);
        Data.goods.add(p6);
        Data.goods.add(p7);

        Orders o1 = new Orders("Order4", Priority.REGULAR, new ArrayList<>(Arrays.asList(
                p1, p3, p6)));
        Orders o2 = new Orders("Order5", Priority.URGENT, new ArrayList<>(Arrays.asList(
                p4, p5)));
        Orders o3 = new Orders("Order2", Priority.URGENT, new ArrayList<>(Arrays.asList(
                p1)));
        Orders o4 = new Orders("Order3", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
                p2, p4)));
        Orders o5 = new Orders("Order1", Priority.REGULAR, new ArrayList<>(Arrays.asList(
                p3, p5)));
        Orders o6 = new Orders("Order6", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
                p1, p6)));
        Orders o7 = new Orders("Order7", Priority.REGULAR, new ArrayList<>(Arrays.asList(
                p1, p3, p6)));
        Orders o8 = new Orders("Order8", Priority.URGENT, new ArrayList<>(Arrays.asList(
                p4, p5)));
        Orders o9 = new Orders("Order9", Priority.URGENT, new ArrayList<>(Arrays.asList(
                p1)));
        Orders o10 = new Orders("Order10", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
                p2, p4)));
        Orders o11 = new Orders("Order11", Priority.REGULAR, new ArrayList<>(Arrays.asList(
                p3, p5)));
        Orders o12 = new Orders("Order12", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
                p1, p6)));

        Data.orders.insert(o1);
        Data.orders.insert(o2);
        Data.orders.insert(o3);
        Data.orders.insert(o4);
        Data.orders.insert(o5);
        Data.orders.insert(o6);
        Data.orders.insert(o7);
        Data.orders.insert(o8);
        Data.orders.insert(o9);
        Data.orders.insert(o10);
        Data.orders.insert(o11);
        Data.orders.insert(o12);

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        Date date1 = new Date(currentYear, currentMonth, currentDay);
        Date date2 = new Date(currentYear + 1, currentMonth, currentDay);
        Date date3 = new Date(currentYear + 2, currentMonth, currentDay);
        Date date4 = new Date(currentYear + 5, currentMonth, currentDay);
        Date date5 = new Date(currentYear + 4, currentMonth, currentDay);

        Freight f1 = new Freight(101, "DestinationA", date1,
                new ArrayList<>(Arrays.asList(Service.sendOrder(0),
                        Service.sendOrder(1))));
        Freight f2 = new Freight(102, "DestinationB", date2,
                new ArrayList<>(Arrays.asList(Service.sendOrder(2))));
        Freight f3 = new Freight(103, "DestinationC", date3,
                new ArrayList<>(Arrays.asList(Service.sendOrder(3))));
        Freight f4 = new Freight(104, "DestinationD", date4,
                new ArrayList<>(Arrays.asList(Service.sendOrder(5))));
        Freight f5 = new Freight(105, "DestinationE", date5,
                new ArrayList<>(Arrays.asList(Service.sendOrder(4))));
        Data.freight.add(f4);
        Data.freight.add(f3);
        Data.freight.add(f5);
        Data.freight.add(f2);
        Data.freight.add(f1);

    }

    public static boolean checkOnLevelMoneySideOrder(double cost, Product product) {
        if (cost + product.getPrice() <= Data.levelMoneySideOrders)
            return true;
        return false;
    }

    public static boolean checkOnLevelMoneySideFreight(double cost, Orders orders) {
        if (cost + orders.getCostTotal() <= Data.levelMoneySideFreight)
            return true;
        return false;
    }

    public static void addNewProduct(Product product)
            throws NegativePriceException, IDException, IllegalAvailableAmountException {
        if (product.getPrice() <= 0)
            throw new NegativePriceException("Price is negative !!!");

        if (product.getAvailableAmount() <= 0 || product.getAvailableAmount() > 1000)
            if (product.getAvailableAmount() <= 0)
                throw new IllegalAvailableAmountException("quantity is negative !!!");
            else
                throw new IllegalAvailableAmountException("The quantity exceeded the required limit.");

        Data.goods.add(product);
    }

    public static void addFreight(Freight freight) throws IDException, ExceedingBudgetLimitsException {
        if (freight.getCost() > Data.levelMoneySideFreight)
            throw new ExceedingBudgetLimitsException(
                    "Exceeding the budget limit of " + Data.levelMoneySideFreight + "$.");
        Data.freight.add(freight);

    }

    public static void addNewOrderWithPriority(Orders order)
            throws IllegalAvailableAmountException, IDException, ExceedingBudgetLimitsException {
        if (order.getCostTotal() > Data.levelMoneySideOrders)
            throw new ExceedingBudgetLimitsException(
                    "Exceeding the budget limit of " + Data.levelMoneySideOrders + "$.");

        Data.orders.insert(new Orders(order));

    }

    public static Orders sendOrder(int key) throws IllegalAvailableAmountException, IDException {
        Orders wanted = null;

        wanted = Data.orders.remove(key);

        for (Product product : wanted.getContentOrder()) {

            if (wanted != null)
                product.updateAvailableAmount(product.getAvailableAmount() - 1);

        }
        return wanted;
    }

    public static Orders sendOrder(Orders wanted) throws IllegalAvailableAmountException, IDException {
        for (Product product : wanted.getContentOrder()) {

            if (wanted != null)
                product.updateAvailableAmount(product.getAvailableAmount() - 1);

        }
        return wanted;
    }

    public static <T extends BasicClass> NodeAVL<T> search(AVL<T> basic, int ID) throws IDException {
        return basic.search(ID);
    }

    public static void updateProduct(Product update, Product orginalProduct)
            throws IllegalAvailableAmountException, NegativePriceException {
        orginalProduct.setName(update.getName().trim());
        orginalProduct.updatePrice(update.getPrice());
        orginalProduct.updateAvailableAmount(update.getAvailableAmount());

    }

    public static Product deletProduct(int id) throws IDException {
        Product pro = Data.goods.search(id).getItem();
        Data.goods.delet(id);
        return pro;
    }

    public static void updateDateOfReceipt(Freight update, Freight orginalFreight) {
        orginalFreight.setDestination(update.getDestination());
        orginalFreight.setCost(update.getCost());
        orginalFreight.setOrders(update.getOrders());
        orginalFreight.updateDateOfReceipt(update.getDateOfReceipt());
    }

    public static void updatePriorityOrder(Orders order, int index) {

        Data.orders.updatePriorityWithDetect(index, order.getPriority());
    }

    public static void analyticalReports() {
        System.out.println("*******************************************************************************");
        System.out.println("values products Store : " +
                Data.goods.valueProductsStore(Data.goods.getRoot()));
        System.out.println("Enter Level wanted Hiehgt : ");
        double levelHieght = scan.nextDouble();
        System.out.println();
        System.out.print("height cost friehgt : ");
        Data.freight.printHeightCostFreight(Data.freight.getRoot(), levelHieght);
        System.out.println();
        System.out.println("cost orders : " + Data.orders.costOrders());
        System.out.println("*******************************************************************************");

    }
}
