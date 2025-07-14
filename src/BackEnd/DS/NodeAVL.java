package BackEnd.DS;

import BackEnd.Store.BasicClass;

public class NodeAVL<E extends BasicClass> {
    E item;
    int height;
    NodeAVL<E> left, right;

    public NodeAVL(E data) {
        this.item = data;
        this.height = 1;
        this.left = null;
        this.right = null;
    }

    public E getItem() {
        return item;
    }

    public NodeAVL<E> getLeft() {
        return left;
    }

    public NodeAVL<E> getRight() {
        return right;
    }
}
