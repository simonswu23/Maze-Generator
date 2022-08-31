package disjointsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> indexLookup;
    int numSets;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>(10);
        this.indexLookup = new HashMap<>();
        this.numSets = 0;
        //this.reverseMap = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (!indexLookup.containsKey(item)) {
            this.pointers.add(-1);
            this.indexLookup.put(item, this.pointers.size() - 1);
            this.numSets++;
            //this.reverseMap.put(this.pointers.size(), item);
        } else {
            throw new IllegalArgumentException("Item already exists in set");
        }
    }

    @Override
    public int findSet(T item) {
        System.out.println("Findset pointers: " + pointers);
        if (!indexLookup.containsKey(item)) {
            throw new IllegalArgumentException("Check your item again. The item does not appear to exist!");
        }
        List<Integer> temp = new ArrayList<>();
        int head = 0;
        int index = this.indexLookup.get(item);
        while (index >= 0) {
            temp.add(index);
            head = index;
            index = pointers.get(index);
        }
        System.out.println("index: " + index);
        System.out.println("head: "  + head);
        for (int i = 0; i < temp.size() - 1; i++) {
            pointers.set(temp.get(i), head);
        }
        return head;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!indexLookup.containsKey(item1) || !indexLookup.containsKey(item2)) {
            throw new IllegalArgumentException("Check Your inputs!!");
        }
        System.out.println("union pointers: " + pointers);
        int headLoc1 = findSet(item1);
        int headLoc2 = findSet(item2);
        System.out.println("HeadLoc1: " + headLoc1);
        System.out.println("HeadLoc2: " + headLoc2);
        int headSize1 = this.pointers.get(headLoc1);
        int headSize2 = this.pointers.get(headLoc2);
        System.out.println("headSize1: " + headSize1);
        System.out.println("headSize2: " + headSize2);
        if (headLoc1 == headLoc2) {
            return false;
        }

        if (headSize1 > headSize2) {
            this.pointers.set(headLoc1, headLoc2);
            this.pointers.set(headLoc2, headSize1 + headSize2);
        } else {
            this.pointers.set(headLoc2, headLoc1);
            this.pointers.set(headLoc1, headSize1 + headSize2);
        }
        numSets--;
        return true;
    }

    public int returnNumSets() {
        return numSets;
    }


}
