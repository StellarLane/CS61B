package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class Node {
        public K key;
        public V value;
        public Node leftChild;
        public Node rightChild;

        public Node(K thisKey, V thisValue) {
            key = thisKey;
            value = thisValue;
        }
    }

    public Node root;
    public int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
//        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(K key) {
        return nodeContains(root, key);
//        throw new UnsupportedOperationException();
    }

    private boolean nodeContains(Node node, K key) {
        if (node == null) {
            return false;
        }
        int tmp = key.compareTo(node.key);
        if (tmp != 0) {
            if (tmp < 0) {
                return nodeContains(node.leftChild, key);
            } else {
                return nodeContains(node.rightChild, key);
            }
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        return nodeGet(root, key);
//        throw new UnsupportedOperationException();
    }

    private V nodeGet(Node node, K key) {
        if (node == null) {
            return null;
        }
        int tmp = key.compareTo(node.key);
        if (tmp != 0) {
            if (tmp > 0) {
                return nodeGet(node.rightChild, key);
            } else {
                return nodeGet(node.leftChild, key);
            }
        } else {
            return node.value;
        }
    }

    @Override
    public int size() {
        return size;
//        throw new UnsupportedOperationException();
    }

    @Override
    public void put(K key, V value) {
        root = nodePut(root, key, value);
        size++;
//        throw new UnsupportedOperationException();
    }

    private Node nodePut(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value);
        }
        int tmp = key.compareTo(node.key);
        if (tmp != 0) {
            if (tmp > 0) {
                node.rightChild = nodePut(node.rightChild, key, value);
            } else {
                node.leftChild = nodePut(node.leftChild, key, value);
            }
        } else {
            node.value = value;
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
