package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author StellarLane
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private final int defaultInitialSize = 16;
    private final double defaultLoadFactor = 0.75;
    private final double loadFactor;
    // You should probably define some more!;


    /** Constructors */
    public MyHashMap() {
        loadFactor = defaultLoadFactor;
        buckets = createTable(defaultInitialSize);
    }

    public MyHashMap(int initialSize) {
        loadFactor = defaultLoadFactor;
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        loadFactor = maxLoad;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }
    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] hashTable = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = createBucket();
        }
        return hashTable;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<K>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                set.add(node.key);
            }
        }
        return set;
//        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    @Override
    public void clear() {
        buckets = createTable(defaultInitialSize);
        size = 0;
//        throw new UnsupportedOperationException();
    }

    private int getIndex(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    private Node getNode(K key, int index) {
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int curIndex = Math.floorMod(key.hashCode(), buckets.length);
        return getNode(key, curIndex) != null;
//        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key) {
        int curIndex = Math.floorMod(key.hashCode(), buckets.length);
        if (getNode(key, curIndex) != null) {
            return getNode(key, curIndex).value;
        } else {
            return null;
        }
//        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int curIndex = Math.floorMod(key.hashCode(), buckets.length);
        if (getNode(key, curIndex) != null) {
            getNode(key, curIndex).value = value;
        } else {
            buckets[curIndex].add(createNode(key, value));
            size++;
//        throw new UnsupportedOperationException();
        }
        if ((double) size / buckets.length >= loadFactor) {
            resize();
        }
    }

    private void resize() {
        Collection<Node>[] newBuckets = createTable(buckets.length * 2);
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int newIndex = Math.floorMod(node.key.hashCode(), buckets.length * 2);
                newBuckets[newIndex].add(createNode(node.key, node.value));
            }
        }
        buckets = newBuckets;
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V tmp = get(key);
            buckets[getIndex(key)].remove(getNode(key, getIndex(key)));
            size--;
            return tmp;
        } else {
            return null;
        }
//        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key) && get(key) == value) {
            buckets[getIndex(key)].remove(getNode(key, getIndex(key)));
            size--;
            return value;
        } else {
            return null;
        }
//        throw new UnsupportedOperationException();
    }

}
