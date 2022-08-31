package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    private Map<T, Integer> getItem;

    public ArrayHeapMinPQ() {
        this.items = new ArrayList<>(10);
        this.getItem =  new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp1 = items.get(a);
        PriorityNode<T> temp2 = items.get(b);
        getItem.put(temp1.getItem(), b);
        getItem.put(temp2.getItem(), a);
        items.set(a, temp2);
        items.set(b, temp1);
    }

    private void percolateUp(double priority, int index) {
        int pIndex = ((index - 1) / 2);
        if (pIndex >= 0 && items.size() > 0) {
            double parentPrio = items.get(pIndex).getPriority();
            if (parentPrio > priority) {
                swap(index, pIndex);
                percolateUp(priority, pIndex);
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (getItem.containsKey(item)) {
            throw new IllegalArgumentException("No duplicate items!");
        }
        int index = items.size();
        getItem.put(item, index);
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        items.add(newNode);
        percolateUp(priority, index);
    }

    @Override
    public boolean contains(T item) {
        return getItem.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return items.get(0).getItem();
        }
    }

    private void percolateDown(double priority, int index) {
        int leftIndex = 2 * index + 1;
        int rightIndex = 2 * index + 2;
        if ((rightIndex > items.size() && leftIndex < items.size()) ||
            rightIndex < items.size()) {
            double left = items.get(leftIndex).getPriority();
            double right = items.get(rightIndex).getPriority();
            int minIndex;
            double minVal;
            if (left <= right) {
                minIndex = leftIndex;
                minVal = left;
            } else {
                minIndex = rightIndex;
                minVal = right;
            }
            if (priority > minVal) {
                swap(index, minIndex);
                percolateDown(priority, minIndex);
            }
        }
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        PriorityNode<T> returnItem = items.get(0);
        items.set(0, items.get(items.size() - 1));
        percolateDown(items.get(0).getPriority(), 0);
        items.remove(items.size() - 1);
        getItem.remove(returnItem.getItem());
        return returnItem.getItem();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (isEmpty() || !getItem.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int thisIndex = getItem.get(item);
        items.set(thisIndex, new PriorityNode<>(item, priority));
        percolateDown(priority, thisIndex);
        percolateUp(priority, thisIndex);
    }

    @Override
    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
