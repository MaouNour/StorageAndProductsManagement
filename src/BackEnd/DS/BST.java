package BackEnd.DS;

import BackEnd.Exceptions.IDException;
import BackEnd.Store.BasicClass;
import BackEnd.Store.Freight;
import BackEnd.Store.Product;

public class BST<T extends BasicClass> {
private Node<T> root;
static class Node<T extends BasicClass>{
    T item;
    Node<T> left,right;
    public Node(T item)
    {
        this.item=item;
    }
    public Node<T> getLeft()
    {
        return left;
    }
    public Node<T> getRight()
    {
        return right;
    }
    public T getItem()
    {
        return item;
    }
}
public Node<T> getRoot()
{
    return root;
}
private Node<T> min(Node<T> node)
{
    while (node.left!=null) {
        node=node.left;
    }
    return node;
}
    private Node<T> delet(Node<T> node, int target) throws IDException {
        if (node == null)
            throw new IDException("ID not Exist , Pls Again Enter another ID");
        else if (target < node.item.getID())
            node.left = delet(node.left, target);
        else if (target > node.item.getID())
            node.right = delet(node.right, target);
        else {
            if (node.right == null || node.left == null) {
                Node<T> temp = null;
                if (node.right == null)
                    temp = node.left;
                else if (node.left == null)
                    temp = node.right;
                return temp;
            } else {
                node.item = min(node.right).item;
                node.right = delet(node.right, node.item.getID());
                return node;
            }
        }
        return node;
    }

    public void delet(int target) throws IDException {
        this.root = delet(root, target);

    }
public Node<T> insert(Node<T> node,T data) throws IDException
{
    if(node==null)
    return new Node<T>(data);
    else if (node.item.getID() > data.getID())
            node.left = insert(node.left, data);
    else if (node.item.getID() < data.getID())
            node.right = insert(node.right, data);
    else
            throw new IDException("ID Exist , Pls, Again Enter Anothor ID");
    return node;
}
    private Node<T> search(Node<T> node, int target) throws IDException {
        if (node == null)
            throw new IDException("ID not Exist , Pls Again Enter another ID");
        else if (node.item.getID() > target)
            return search(node.left, target);
        else if (node.item.getID() < target)
            return search(node.right, target);
        else // node.item.getID() == target
            return node;
    }
    public Node<T> search(int target) throws IDException {
        return search(root, target);
    }

    // print BST
    private void preOrder(Node<T> node) {
        if (node == null)
            return;
        node.item.print();
        preOrder(node.left);
        preOrder(node.right);
    }

    public void preOrder() {
        preOrder(this.root);
    }
     public double valueProductsStore(Node<Product> product) {
        if (product != null)
            return product.getItem().getPrice() + valueProductsStore(product.left) + valueProductsStore(product.right);
        else
            return 0;
    }

    public void printHeightCostFreight(Node<Freight> freight, double levelHieght) {
        if (freight != null) {
            printHeightCostFreight(freight.right, levelHieght);
            if (freight.item.getCost() >= levelHieght)
                freight.item.print();
            printHeightCostFreight(freight.left, levelHieght);
        }
    }
    public void add(T data) throws IDException {
        this.root = insert(root, data);
    }
}