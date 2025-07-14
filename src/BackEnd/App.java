// package BackEnd;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Scanner;

// import BackEnd.Basic.Date;
// import BackEnd.Basic.Priority;
// import BackEnd.Exceptions.LogicalException;
// import BackEnd.Store.Data;
// import BackEnd.Store.Freight;
// import BackEnd.Store.Orders;
// import BackEnd.Store.Product;

// public class App {
//         static Scanner scan = new Scanner(System.in);

//         public static void pause() {
//                 System.out.println("Enter any button for continue");
//                 scan.next();
//         }

//         public static void clearTerminal() {
//                 System.out.print("\033[H\033[2J");
//                 System.out.flush();
//         }

//         public static void createData() {
//                 try {
//                         Data.goods.add(new Product(1, "ProductA", 100, 100));
//                         Data.goods.add(new Product(5, "ProductE", 50.0, 500));
//                         Data.goods.add(new Product(4, "ProductD", 40.0, 200));
//                         Data.goods.add(new Product(3, "ProductC", 30.0, 300));
//                         Data.goods.add(new Product(2, "ProductB", 20.0, 200));
//                         Data.goods.add(new Product(0, "ProductB", 20.0, 200));

//                         Data.orders.insert(new Orders("Order4", Priority.REGULAR, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(1), Service.sendProduct(3), Service.sendProduct(5)))));
//                         Data.orders.insert(new Orders("Order5", Priority.URGENT, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(2), Service.sendProduct(4)))));
//                         Data.orders.insert(new Orders("Order2", Priority.URGENT, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(3)))));
//                         Data.orders.insert(new Orders("Order3", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(4), Service.sendProduct(5)))));
//                         Data.orders.insert(new Orders("Order1", Priority.REGULAR, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(1), Service.sendProduct(2)))));
//                         Data.orders.insert(new Orders("Order1", Priority.SPECIAL, new ArrayList<>(Arrays.asList(
//                                         Service.sendProduct(1), Service.sendProduct(2)))));

//                         Date date1 = new Date(2025, 7, 1);
//                         Date date2 = new Date(2025, 7, 2);
//                         Date date3 = new Date(2025, 7, 3);
//                         Date date4 = new Date(2025, 7, 4);
//                         Date date5 = new Date(2025, 7, 5);

//                         Freight f1 = new Freight(101, "DestinationA", date1,
//                                         new ArrayList<>(Arrays.asList(Data.orders.getHeap().get(0),
//                                                         Data.orders.getHeap().get(1))));
//                         Freight f2 = new Freight(102, "DestinationB", date2,
//                                         new ArrayList<>(Arrays.asList(Data.orders.getHeap().get(2))));
//                         Freight f3 = new Freight(103, "DestinationC", date3,
//                                         new ArrayList<>(Arrays.asList(Data.orders.getHeap().get(3))));
//                         Freight f4 = new Freight(104, "DestinationD", date4,
//                                         new ArrayList<>(Arrays.asList(Data.orders.getHeap().get(4))));
//                         Freight f5 = new Freight(105, "DestinationE", date5,
//                                         new ArrayList<>(Arrays.asList(Data.orders.getHeap().get(0),
//                                                         Data.orders.getHeap().get(4))));

//                         Data.freight.add(f4);
//                         Data.freight.add(f3);
//                         Data.freight.add(f5);
//                         Data.freight.add(f2);
//                         Data.freight.add(f1);

//                 } catch (LogicalException e) {
//                         System.err.println("Logical Exception: " + e.getMessage());
//                 } catch (Exception e) {
//                         System.err.println("Exception: " + e.getMessage());
//                 }
//         }

//         public static void product() {
//                 Loop: while (true) {
//                         clearTerminal();
//                         System.out.println("0. go Back");
//                         System.out.println("1. add new products");
//                         System.out.println("2. search product");
//                         System.out.println("3. update attribute product");
//                         System.out.println("4. delet product");
//                         System.out.println("5. print all products");
//                         System.out.print("Enter number :");
//                         int num = scan.nextInt();
//                         switch (num) {
//                                 case 0:
//                                         break Loop;
//                                 case 1:
//                                         Service.addNewProduct();
//                                         break;
//                                 case 2:
//                                         Service.search(Data.goods);
//                                         break;
//                                 case 3:
//                                         Service.updateProduct();
//                                         break;
//                                 case 4:
//                                         Service.deletProduct();
//                                         break;
//                                 case 5:
//                                         Data.goods.preOrder();
//                                         break;
//                                 default:
//                                         System.out.println("Enter wrong number .");
//                                         continue;
//                         }
//                         pause();
//                 }

//         }

//         public static void orders() {
//                 Loop: while (true) {
//                         clearTerminal();
//                         System.out.println("0. go Back");
//                         System.out.println("1. add new order");
//                         System.out.println("2. the important order");
//                         System.out.println("3. update priority order");
//                         System.out.println("4. print all products");
//                         System.out.print("Enter number :");
//                         int num = scan.nextInt();
//                         switch (num) {
//                                 case 0:
//                                         break Loop;
//                                 case 1:
//                                         Service.addNewOrderWithPriority();
//                                         break;
//                                 case 2:
//                                         System.out.println(Data.orders.extractHeightPriority());
//                                         break;
//                                 case 3:
//                                         Service.updatePriorityOrder();
//                                         break;
//                                 case 4:
//                                         System.out.println(Data.orders);
//                                         break;
//                                 default:
//                                         System.out.println("Enter wrong number .");
//                                         continue;
//                         }
//                         pause();
//                 }
//         }

//         public static void freight() {
//                 Loop: while (true) {
//                         clearTerminal();
//                         System.out.println("0. go Back");
//                         System.out.println("1. add new freight");
//                         System.out.println("2. search about freight");
//                         System.out.println("3. update Date of recept");
//                         System.out.println("4. print all freight");
//                         System.out.print("Enter number :");
//                         int num = scan.nextInt();
//                         switch (num) {
//                                 case 0:
//                                         break Loop;
//                                 case 1:
//                                         Service.addFreight();
//                                         break;
//                                 case 2:
//                                         Service.search(Data.freight);
//                                         break;
//                                 case 3:
//                                         Service.updateDateOfReceipt();
//                                         break;
//                                 case 4:
//                                         Data.freight.preOrder();
//                                         break;
//                                 default:
//                                         System.out.println("Enter wrong number .");
//                                         continue;
//                         }
//                         pause();
//                 }
//         }

//         public static void main(String[] args) {
//                 createData();

//                 System.out.println();
//                 System.out.println("Enter Level money for Orders: ");
//                 Data.levelMoneySideOrders = scan.nextDouble();
//                 System.out.println("Enter Level money for Freight: ");
//                 Data.levelMoneySideFreight = scan.nextDouble();

//                 System.out.println();
//                 Loop: while (true) {
//                         clearTerminal();
//                         System.out.println("0. Exit");
//                         System.out.println("1. Products");
//                         System.out.println("2. Orders");
//                         System.out.println("3. Freight");
//                         System.out.println("4. Analytical Reports");
//                         System.out.print("Enter number :");
//                         int num = scan.nextInt();
//                         switch (num) {
//                                 case 0:
//                                         break Loop;
//                                 case 1:
//                                         product();
//                                         break;
//                                 case 2:
//                                         orders();
//                                         break;
//                                 case 3:
//                                         freight();
//                                         break;
//                                 case 4:
//                                         Service.analyticalReports();
//                                         pause();
//                                         break;
//                                 default:
//                                         System.out.println("Enter wrong number .");
//                                         continue;
//                         }
//                 }

//         }
// }
