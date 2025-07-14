package BackEnd.FileHandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import BackEnd.DS.NodeAVL;
import BackEnd.Store.Data;
import BackEnd.Store.Product;

public class Statistics {
public static String getStatistics()
{
    String output = "";
    ArrayList<Product> products = getAllNodes(Data.goods.getRoot(),new ArrayList<>());
    double totalCost = 0;
    int numberOfProducts=0;
    for (Product product : products) {
        output+= "\nproduct id : "+product.getID()+"\nName : "+product.getName()+"\nPrice:"+product.getPrice()+"\n";
        totalCost+=product.getPrice();
        numberOfProducts+=product.getAvailableAmount();
    }
    output+="\nproducts total kinds: "+products.size();
    output+="\nproducts total number : "+numberOfProducts;
    output+="\nproducts total row cost : "+(totalCost*numberOfProducts);
    return output;
}
public static void writeToFile(String str)
{
    File file = new File("src/BackEnd/Statistics");
    try {
        BufferedWriter bf = new BufferedWriter(new FileWriter(file,false));
        bf.write(str);
        bf.flush();
        bf.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

}
private static ArrayList<Product> getAllNodes(NodeAVL<Product> node,ArrayList<Product> arrayList)
{
    if (node==null) {
        return arrayList;
    }
    arrayList.add(node.getItem());
    getAllNodes(node.getLeft(), arrayList);
    getAllNodes(node.getRight(), arrayList);
    return arrayList;
}
}
