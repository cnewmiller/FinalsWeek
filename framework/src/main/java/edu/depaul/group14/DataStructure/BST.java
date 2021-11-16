package edu.depaul.group14.DataStructure;

public class BST<T extends Comparable<T>> {

    private int nodeCount = 0;

    private Node root = null;

    private class Node {
        T data;
        Node left, right;

        public Node(Node left, Node right, T elem) {
            this.data = elem;
            this.left = left;
            this.right = right;
        }
    }

    public boolean add(T elem) {

        if (contains(elem)) {
            return false;

        } else {
            root = add(root, elem);
            nodeCount++;
            return true;
        }
    }

    private Node add(Node node, T elem) {

        if (node == null) {
            node = new Node(null, null, elem);

        } else {
            if (elem.compareTo(node.data) < 0) {
                node.left = add(node.left, elem);
            } else {
                node.right = add(node.right, elem);
            }
        }

        return node;
    }

    public boolean contains(T elem) {
        return contains(root, elem);
    }

    private boolean contains(Node node, T elem) {

        if (node == null) return false;

        int cmp = elem.compareTo(node.data);

        if (cmp < 0) return contains(node.left, elem);

        else if (cmp > 0) return contains(node.right, elem);

        else return true;
    }

    public int height() {
        return height(root);
    }
    private int height(Node node) {
        if (node == null) return 0;
        return Math.max(height(node.left), height(node.right)) + 1;
    }
}