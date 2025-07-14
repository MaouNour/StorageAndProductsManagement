package BackEnd.DS;

import BackEnd.Exceptions.IDException;
import BackEnd.Store.BasicClass;
import BackEnd.Store.Freight;
import BackEnd.Store.Product;

public class AVL<T extends BasicClass> {

    private NodeAVL<T> root;

    public NodeAVL<T> getRoot() {
        return root;
    }

 
    private NodeAVL<T> min(NodeAVL<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private boolean isLeaf(NodeAVL<T> node) {
        if (node.left == null && node.right == null)
            return true;
        return false;
    }

    private int getHeight(NodeAVL<T> node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanced(NodeAVL<T> node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private NodeAVL<T> leftRotation(NodeAVL<T> node) {
        NodeAVL<T> rightChild = node.right;
        NodeAVL<T> leftRightChild = rightChild.left;

        node.right = leftRightChild;
        rightChild.left = node;

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        rightChild.height = 1 + Math.max(getHeight(rightChild.left), getHeight(rightChild.right));

        return rightChild;
    }

    private NodeAVL<T> rightRotation(NodeAVL<T> node) {
        NodeAVL<T> leftChild = node.left;
        NodeAVL<T> rightLeftChild = leftChild.right;

        node.left = rightLeftChild;
        leftChild.right = node;

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        leftChild.height = 1 + Math.max(getHeight(leftChild.left), getHeight(leftChild.right));

        return leftChild;
    }

    private NodeAVL<T> rebalance(NodeAVL<T> node) {
        if (getBalanced(node) > 1) {
            if (getBalanced(node.left) < 0)
                node.left = leftRotation(node.left);
            return rightRotation(node);
        } else if (getBalanced(node) < -1) {
            if (getBalanced(node.right) > 0)
                node.right = rightRotation(node.right);
            return leftRotation(node);
        } else
            return node;
    }

    // delet node
    private NodeAVL<T> delet(NodeAVL<T> node, int target) throws IDException {
        if (node == null)
            throw new IDException("ID not Exist , Pls Again Enter another ID");
        else if (target < node.item.getID())
            node.left = delet(node.left, target);
        else if (target > node.item.getID())
            node.right = delet(node.right, target);
        else {
            if (node.right == null || node.left == null) {
                NodeAVL<T> temp = null;
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

        if (!isLeaf(node))
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        else
            node.height = Math.max(getHeight(node.left), getHeight(node.right));
        return rebalance(node);
    }

    public void delet(int target) throws IDException {
        this.root = delet(root, target);

    }

    // insert node to BST
    private NodeAVL<T> insert(NodeAVL<T> node, T data) throws IDException {
        if (node == null)
            return new NodeAVL<>(data);
        else if (node.item.getID() > data.getID())
            node.left = insert(node.left, data);
        else if (node.item.getID() < data.getID())
            node.right = insert(node.right, data);
        else
            throw new IDException("ID Exist , Pls, Again Enter Anothor ID");

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        return rebalance(node);
    }

    public void add(T data) throws IDException {
        this.root = insert(root, data);
    }

    // search about particular node
    private NodeAVL<T> search(NodeAVL<T> node, int target) throws IDException {
        if (node == null)
            throw new IDException("ID not Exist , Pls Again Enter another ID");
        else if (node.item.getID() > target)
            return search(node.left, target);
        else if (node.item.getID() < target)
            return search(node.right, target);
        else // node.item.getID() == target
            return node;
    }

    public NodeAVL<T> search(int target) throws IDException {
        return search(root, target);
    }

    // print BST
    private void preOrder(NodeAVL<T> node) {
        if (node == null)
            return;
        node.item.print();
        preOrder(node.left);
        preOrder(node.right);
    }

    public void preOrder() {
        preOrder(this.root);
    }

    public double valueProductsStore(NodeAVL<Product> product) {
        if (product != null)
            return product.getItem().getPrice() + valueProductsStore(product.left) + valueProductsStore(product.right);
        else
            return 0;
    }

    public void printHeightCostFreight(NodeAVL<Freight> freight, double levelHieght) {
        if (freight != null) {
            printHeightCostFreight(freight.right, levelHieght);
            if (freight.item.getCost() >= levelHieght)
                freight.item.print();
            printHeightCostFreight(freight.left, levelHieght);
        }
    }

}