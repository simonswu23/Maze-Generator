package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.5;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 0;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 1;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;
    private double resize;
    private int size;
    private int capacity;

    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resize = resizingLoadFactorThreshold;
        this.capacity = chainInitialCapacity;
        this.chains = createArrayOfChains(chainInitialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    public int createHash(Object key) {
        if (key == null) { //Consider case when key and value are null.
            return 0;
        } else {
            return Math.abs(key.hashCode() % capacity);
        }
    }

    @Override
    public V get(Object key) {
        int tempHash = createHash(key);
        if (chains[tempHash] != null) {
            return chains[tempHash].get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int tempHash = createHash(key);
        if (chains[tempHash] == null) {
            chains[tempHash] = new ArrayMap<>();
        }
        value = chains[tempHash].put(key, value);
        size++;
        return value;
    }

    @Override
    public V remove(Object key) {
        int tempHash = createHash(key);
        if (chains[tempHash] != null) {
            V val = chains[tempHash].remove(key);
            if (!(val == null && key != null)) {
                size--;
            }
            return val;
        }
        return null;
    }

    @Override
    public void clear() {
        this.chains = this.createArrayOfChains(capacity);
        this.size = 0;
    }

    private void resize() {
        this.capacity *= 2;
        AbstractIterableMap<K, V>[] temp = createArrayOfChains(capacity);
        Iterator<Entry<K, V>> itr = new ChainedHashMapIterator<>(chains);
        while (itr.hasNext()) {
            Entry<K, V> entry = itr.next();
            K key = entry.getKey();
            V value = entry.getValue();
            V help = get(key);

            int hashcode = createHash(key);
            if (temp[hashcode] == null) {
                temp[hashcode] = new ArrayMap<>();
            }
            temp[hashcode].put(key, value);
        }
        chains = temp;
    }

    @Override
    public boolean containsKey(Object key) {
        int tempHash = createHash(key);
        if (chains[tempHash] != null) {
            return chains[tempHash].containsKey(key);
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    // Doing so will give you a better string representation for assertion errors the debugger.
    @Override
    public String toString() {
        return super.toString();
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private AbstractIterableMap<K, V> currArray;
        private int hash;
        private int capacity;
        private Iterator<Entry<K, V>> itr;
        // You may add more fields and constructor parameters

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.capacity = chains.length;
            this.hash = 0;
            while (hash < capacity && chains[hash] == null) {
                hash++;
            }
            if (hash < capacity) {
                this.itr = chains[hash].iterator();
                currArray = chains[hash];
            }
        }

        @Override
        public boolean hasNext() {
            while (capacity != hash) {
                if (itr.hasNext()) {
                    return true;
                } else if (!itr.hasNext() || chains[hash] == null) {
                    hash++;
                }
            }
            return false;
        }

        public Map.Entry<K, V> next() {
            if (itr != null && itr.hasNext()) {
                return itr.next();
            } else {
                hash++;
                while (hash < capacity && chains[hash] == null) {
                    hash++;
                }
                if (hash >= capacity) {
                    throw new NoSuchElementException("End of Map");
                }
                this.itr = chains[hash].iterator();
                if (itr.hasNext()) {
                    return itr.next();
                } else {
                    throw new NoSuchElementException("End of Map");
                }
            }
        }
    }
}
