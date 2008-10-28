package com.sun.javafx.runtime.util;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;

/**
 * LinkableTest
 *
 * @author Brian Goetz
 */
public class LinkableTest extends JavaFXTestCase {
    public static Linkable.HeadAccessor<Node, Holder> head = new Linkable.HeadAccessor<Node, Holder>() {
        public Node getHead(Holder host) {
            return host.nodes;
        }

        public void setHead(Holder host, Node newHead) {
            host.nodes = newHead;
        }
    };

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
        final SequenceLocation<Integer> a = SequenceVariable.make(Integer.class);
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

        final SequenceLocation<Integer> a = SequenceVariable.make(Integer.class);

        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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

        Linkables.remove(head, h, node);

        Linkables.addAtEnd(head, h, node);
        assertEquals(1, Linkables.size(h.nodes));
        assertTrue(!Linkables.isUnused(node));
        assertEquals(node, h.nodes);
        assertTrue(node.next == null);
        assertTrue(node.host == h);

        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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

        Linkables.remove(head, h, equalNode);
        assertEquals(h, new Integer[] { 1 });
        Linkables.remove(head, h, node);
        assertEquals(h);
    }

    public void testAddAndRemove() {
        Holder h = new Holder();
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        assertEquals(h);
        Linkables.addAtEnd(head, h, node1);
        assertEquals(h, new Integer[] { 1 });
        Linkables.addAtEnd(head, h, node2);
        assertEquals(h, 1, 2);
        Linkables.addAtEnd(head, h, node3);
        assertEquals(h, 1, 2, 3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(head, h, node3);
        assertEquals(h, 1, 2, 4);
        Linkables.remove(head, h, node3);
        assertEquals(h, 1, 2, 4);
        Linkables.remove(head, h, node1);
        assertEquals(h, 2, 4);
        Linkables.remove(head, h, node4);
        assertEquals(h, new Integer[] { 2 });
        Linkables.remove(head, h, node2);
        assertEquals(h);

        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(head, h, node4);
        assertEquals(h, 1, 2, 3);
        Linkables.remove(head, h, node3);
        assertEquals(h, 1, 2);
        Linkables.remove(head, h, node2);
        assertEquals(h, new Integer[] { 1 });
        Linkables.remove(head, h, node1);
        assertEquals(h);

        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);

        Linkables.remove(head, h, node1);
        assertEquals(h, 2, 3, 4);
        Linkables.remove(head, h, node2);
        assertEquals(h, 3, 4);
        Linkables.remove(head, h, node3);
        assertEquals(h, new Integer[] { 4 });
        Linkables.remove(head, h, node4);
        assertEquals(h);
    }

    public void testMIC() {
        for (int i=0; i<4; i++) {
            Holder h = new Holder();
            Node node1 = new Node(1);
            Node node2 = new Node(2);
            Node node3 = new Node(3);
            Node node4 = new Node(4);

            Linkables.addAtEnd(head, h, node1);
            Linkables.addAtEnd(head, h, node2);
            Linkables.addAtEnd(head, h, node3);
            Linkables.addAtEnd(head, h, node4);
            assertEquals(h, 1, 2, 3, 4);
            assertEquals(4, Linkables.size(h.nodes));
            assertEquals(10, sum(h));
            final int iCopy = i;
            Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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

        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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
        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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
        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
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
        Linkables.addAtEnd(head, h, node1);
        Linkables.addAtEnd(head, h, node2);
        Linkables.addAtEnd(head, h, node3);
        Linkables.addAtEnd(head, h, node4);
        assertEquals(h, 1, 2, 3, 4);
        Linkables.iterate(head, h, new Linkable.MutativeIterationClosure<Node, Holder>() {
            public boolean action(Node element) {
                return element.value % 2 == 0;
            }
        });
        assertEquals(h, 2, 4);
    }
}

class Holder {
    Node nodes;
}

class Node implements Linkable<Node, Holder> {
    Node next;
    Holder host;
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

    public Holder getHost() {
        return host;
    }

    public void setHost(Holder host) {
        this.host = host;
    }
}