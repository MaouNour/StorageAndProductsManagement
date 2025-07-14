package BackEnd.DS;

import java.util.ArrayList;

import BackEnd.Basic.Priority;
import BackEnd.Store.Orders;

public class Heap<T extends Orders> {
    private ArrayList<T> heap;

    public Heap() {
        heap = new ArrayList<>();
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int index1, int index2) {
        T temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    public void heapifyUp(int i) {
        while (i > 0 && getValuePriority(heap.get(parent(i))) < getValuePriority(heap.get(i))) {
            swap(parent(i), i);
            i = parent(i);
        }
    }

    private int getValuePriority(T T) {
        return T.getPriority().ordinal();
    }

    public void heapifyDown(int i) {
        int size = heap.size();
        int left = left(i);
        int right = right(i);
        int largest = i;

        if (left < size && getValuePriority(heap.get(left(i))) > getValuePriority(heap.get(i)))
            largest = left;
        if (right < size && getValuePriority(heap.get(right(i))) > getValuePriority(heap.get(i)))
            largest = right;
        if (largest != i) {
            swap(i, largest);
            heapifyDown(largest);
        }
    }

    public void insert(T order) {
        heap.add(order);
        heapifyUp(heap.size() - 1);
    }

    public T extractHeightPriority() {
        T heightPriority = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapifyDown(0);
        return heightPriority;
    }

    public ArrayList<T> getHeap() {
        return heap;
    }

    public void updatePriorityWithDetect(int index, Priority priority) {
        T orderRemove = heap.remove(index);
        orderRemove.updatePriority(priority);
        heap.add(orderRemove);
        heapifyUp(heap.size() - 1);
    }

    @Override
    public String toString() {
        String arr = "";
        for (int i = 0; i < heap.size(); i++) {
            arr += "\n" + (i + 1) + ". " + heap.get(i) + "\n";
        }
        return arr;
    }

    public double costOrders(){
        double costTotal =0;
        for (T t : heap) {
            costTotal +=  t.getCostTotal();
        }
        return costTotal;
    }
}
