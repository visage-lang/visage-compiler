package com.sun.javafx.runtime.util;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;

/**
 * LinkableTest
 *
 * @author Brian Goetz
 */
public class LinkableTest extends JavaFXTestCase {
    private int sum(Holder h) {
        final int[] sum = new int[1];
        Linkables.iterate(h.nodes, new Linkable.IterationClosure<Node>() {
            public void action(Node element) {
                sum[0] += element.value;
            }
        });
        return sum[0];
    }

    private void assertEquals(Holder h, Integer... values) {
        final SequenceLocation<Integer> a = SequenceVariable.make(TypeInfo.Integer);
        assertEquals(values.length, Linkables.size(h.nodes));
        Linkables.iterate(h.nodes, new Linkable.IterationClosure<Node>() {
            public void action(Node element) {
                a.insert(element.value);
            }
        });
        assertEquals(a, values);
    }

    public void testEmpty() {
        Holder h = new Holder();
        Node node = new Node(1);
        Node equalNode = new Node(1);

        assertEquals(0, Linkables.size(h.nodes));
        assertTrue(Linkables.isUnused(node));

        final SequenceLocation<Integer> a = SequenceVariable.make(TypeInfo.Integer);

        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                a.insert(element.value);
                return true;
            }
        });
        assertEquals(a);

        Linkables.iterate(h.nodes, new Linkable.IterationClosure<Node>() {
            public void action(Node element) {
                a.insert(element.value);
            }
        });
        assertEquals(a);

        Linkables.remove(node);

        Linkables.addAtEnd(h, node);
        assertEquals(1, Linkables.size(h.nodes));
        assertTrue(!Linkables.isUnused(node));
        assertEquals(node, h.nodes);
        assertTrue(node.next == null);
        assertTrue(node.prev == h);

        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                a.insert(element.value);
                return true;
            }
        });
        assertEquals(a, 1);

        Linkables.iterate(h.nodes, new Linkable.IterationClosure<Node>() {
            public void action(Node element) {
                a.insert(element.value);
            }
        });
        assertEquals(a, 1, 1);

        Linkables.remove(equalNode);
        assertEquals(h, new Integer[] { 1 });
        Linkables.remove(node);
        assertEquals(h);
    }

    public void testAddAndRemove() {
        Holder h = new Holder();
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        assertEquals(h);
        Linkables.addAtEnd(h, node1);
        assertEquals(h, new Integer[] { 1 });
        Linkables.addAtEnd(h, node2);
        assertEquals(h, 1, 2);
        Linkables.addAtEnd(h, node3);
        assertEquals(h, 1, 2, 3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(node3);
        assertEquals(h, 1, 2, 4);
        Linkables.remove(node3);
        assertEquals(h, 1, 2, 4);
        Linkables.remove(node1);
        assertEquals(h, 2, 4);
        Linkables.remove(node4);
        assertEquals(h, new Integer[] { 2 });
        Linkables.remove(node2);
        assertEquals(h);

        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(node4);
        assertEquals(h, 1, 2, 3);
        Linkables.remove(node3);
        assertEquals(h, 1, 2);
        Linkables.remove(node2);
        assertEquals(h, new Integer[] { 1 });
        Linkables.remove(node1);
        assertEquals(h);

        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(node1);
        assertEquals(h, 2, 3, 4);
        Linkables.remove(node2);
        assertEquals(h, 3, 4);
        Linkables.remove(node3);
        assertEquals(h, new Integer[] { 4 });
        Linkables.remove(node4);
        assertEquals(h);
    }

    public void testMIC() {
        for (int i=0; i<4; i++) {
            Holder h = new Holder();
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            Node node3 = new Node(3);
            Node node4 = new Node(4);

            Linkables.addAtEnd(h, node1);
            Linkables.addAtEnd(h, node2);
            Linkables.addAtEnd(h, node3);
            Linkables.addAtEnd(h, node4);
            assertEquals(h, 1, 2, 3, 4);
            assertEquals(4, Linkables.size(h.nodes));
            assertEquals(10, sum(h));
            final int iCopy = i;
            Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
                public boolean action(Node element) {
                    return element.value != iCopy+1;
                }
            });
            assertEquals(3, Linkables.size(h.nodes));
            assertEquals(10-(i+1), sum(h));
        }

        Holder h = new Holder();
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                return element.value <= 2;
            }
        });
        assertEquals(h, 1, 2);

        h = new Holder();
        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node4 = new Node(4);
        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                return element.value > 2;
            }
        });
        assertEquals(h, 3, 4);

        h = new Holder();
        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node4 = new Node(4);
        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                return element.value % 2 == 1;
            }
        });
        assertEquals(h, 1, 3);

        h = new Holder();
        node1 = new Node(1);
        node2 = new Node(2);
        node3 = new Node(3);
        node4 = new Node(4);
        Linkables.addAtEnd(h, node1);
        Linkables.addAtEnd(h, node2);
        Linkables.addAtEnd(h, node3);
        Linkables.addAtEnd(h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(h.nodes, new Linkable.MutativeIterationClosure<Node>() {
            public boolean action(Node element) {
                return element.value % 2 == 0;
            }
        });
        assertEquals(h, 2, 4);
    }
}

class Holder implements Linkable<Node> {
    Node nodes;

    public Node getNext() {
        return nodes;
    }

    public Linkable<Node> getPrev() {
        throw new UnsupportedOperationException();
    }

    public void setNext(Node next) {
        nodes = next;
    }

    public void setPrev(Linkable<Node> prev) {
        throw new UnsupportedOperationException();
    }
}

class Node implements Linkable<Node> {
    Node next;
    Linkable<Node> prev;
    int value;

    Node(int value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Linkable<Node> getPrev() {
        return prev;
    }

    public void setPrev(Linkable<Node> prev) {
        this.prev = prev;
    }
}
