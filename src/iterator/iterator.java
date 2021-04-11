package iterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;



class NodeIterator implements Iterator<NodeIterator.Node> {
    public static class Node {
        private Node parent;
        private int indexInParentCollection;
        private Integer value; // unique
        private Collection<Node> siblings; // collection of nodes such that for each sibling in colection edge between this node and sibling exists

        public Node (int value, Collection<Node> siblings) {
            this.value = value;
            this.siblings = siblings;
            this.parent = null;
        }

        public Node(Node node) {
            this.siblings = node.siblings;
            this.value = node.value;
            this.parent = node.parent;
            this.indexInParentCollection = node.indexInParentCollection;

        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private Node node;
    public NodeIterator(Node start) {
        this.node = start;
    }

    @Override
    public boolean hasNext() {
        if (node.value == null) {
            return false;
        }
        Node temp = new Node (node);
        if (temp.parent == null)
        {
            return !temp.siblings.isEmpty();
        }
        if (!temp.siblings.isEmpty()) {
            return true;
        }
        while (temp.parent != null) {
            if ((temp.parent.siblings.size() - 1) > temp.indexInParentCollection) {
                return true;
            }
            temp = temp.parent;
        }
        return true;
    }

    @Override
    public Node next() {
        Node retNode = new Node (node);
        if (!node.siblings.isEmpty()) {
            Node parent = new Node (node);
            node = ((Node) node.siblings.toArray()[0]);
            node.parent = parent;
            node.indexInParentCollection = 0;
        }
        else {
            while ((node != null && (node.parent != null))
                    && ((node.parent.siblings.size() - 1) <= node.indexInParentCollection)) {
                node = node.parent;
            }
            if (node != null && node.parent != null) {
                Node temp = new Node((Node) node.parent.siblings.toArray()[node.indexInParentCollection + 1]);
                temp.parent = node.parent;
                temp.indexInParentCollection = node.indexInParentCollection + 1;
                node = temp;
            }
            if (node.parent == null) {
                Node temp = new Node(node);
                node.value = null;
                node.siblings = null;
                node.parent = temp;
            }
        }
        return retNode;
    }

    public static void main(String[] args) {
        Node start = new Node(1, Arrays.asList(
                new Node(2, Collections.emptyList()),
                new Node(3, Arrays.asList(
                        new Node(4, Arrays.asList(new Node(5, Collections.emptyList()),
                                new Node(6, Collections.emptyList()))),
                        new Node(7, Collections.emptyList()))
                )
        ));

        NodeIterator iterator = new NodeIterator(start);
        // should print numbers 1-4 in any order
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
