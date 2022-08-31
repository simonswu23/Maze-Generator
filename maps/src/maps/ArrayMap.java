package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private int size;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.size = 0;
        this.entries = this.createArrayOfEntries(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    public V get(Object key) {
        Iterator<Entry<K, V>> itr = new ArrayMapIterator<>(this.entries, this.size);
        while (itr.hasNext()) {
            Entry<K, V> check = itr.next();
            if (check != null) {
                K keyCheck = check.getKey();
                if ((keyCheck == null && key == null)
                    || (keyCheck != null && keyCheck.equals(key))) {
                    return check.getValue();
                }
            }
        }
        return null;
    }

    public V put(K key, V value) {
        V rtVal = null;
        if (containsKey(key)) {
            rtVal = remove(key);
        }
        if (entries.length == size) {
            resize();
        }
        this.entries[this.size++] = new SimpleEntry<>(key, value);
        return rtVal;
    }

    private void resize() {
        SimpleEntry<K, V>[] newArray = this.createArrayOfEntries(size * 2);
        for (int i = 0; i < size; i++) {
            newArray[i] = entries[i];
        }
        entries = newArray;
    }

    public V remove(Object key) {
        Iterator<Entry<K, V>> itr = new ArrayMapIterator<>(this.entries, this.size);
        int count = 0;
        while (itr.hasNext()) {
            Entry<K, V> check = itr.next();
            if (check != null) {
                K keyCheck = check.getKey();
                boolean removeNull = keyCheck == null && key == null;
                boolean nullCheck = keyCheck != null && keyCheck.equals(key);
                if (removeNull || nullCheck) {
                    V value = get(key);
                    entries[count] = entries[--size];
                    return value;
                }
                count++;
            }
        }
        return null;
    }


    public void clear() {
        entries = this.createArrayOfEntries(DEFAULT_INITIAL_CAPACITY);
        this.size = 0;
    }


    public boolean containsKey(Object key) {
        Iterator<Entry<K, V>> itr = new ArrayMapIterator<>(this.entries, this.size);
        while (itr.hasNext()) {
            Entry<K, V> check = itr.next();
            if (check != null) {
                K keyCheck = check.getKey();
                if (keyCheck == null && key == null) {
                    return true;
                } else if (keyCheck != null && keyCheck.equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }


        public int size() {
            return this.size;
        }


        public Iterator<Map.Entry<K, V>> iterator() {
            // Note: You may or may not need to change this method, depending on whether you
            // add any parameters to the ArrayMapIterator constructor.
            return new ArrayMapIterator<>(this.entries, this.size);
        }

        // Doing so will give you a better string representation for assertion errors the debugger.
        @Override
        public String toString() {
            return super.toString();
        }

        private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
            private final SimpleEntry<K, V>[] entries;
            private int size;
            private int index;
            // You may add more fields and constructor parameters

            public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
                this.size = size;
                this.index = 0;
                this.entries = entries;
            }

            public boolean hasNext() {
                return index < size;
            }

            public Map.Entry<K, V> next() {
                if (size == 0) {
                    throw new NoSuchElementException("Map has no entries");
                } else if (index == size) {
                    throw new NoSuchElementException("No more entries");
                }
                SimpleEntry<K, V> curr = entries[index];
                index++;
                return curr;
            }
        }
    }
