package com.sun.javafx.runtime.util;

/**
 * Linkable
 *
 * @author Brian Goetz
 */
public abstract class AbstractLinkable<T, H> implements Linkable<T, H> {

    public static<T, H> boolean isUnused(AbstractLinkable<T, H> element) {
        return element.getHost() == null && element.getNext() == null;
    }

    public static<T extends Linkable<T, H>, H> void addAtEnd(HeadAccessor<T, H> ha, H host, T element) {
        T head = ha.getHead(host);
        assert (element.getNext() == null);
        assert (element.getHost() == null);
        element.setHost(host);
        if (head == null)
            ha.setHead(host, element);
        else {
            T cur = head;
            T nextPtr;
            while ((nextPtr = cur.getNext()) != null)
                cur = nextPtr;
            cur.setNext(element);
        }
    }

    public static<T extends Linkable<T, H>, H> void remove(HeadAccessor<T, H> ha, H host, T element) {
        T head = ha.getHead(host);
        if (head == element)
            ha.setHead(host, element.getNext());
        else {
            T cur = head;
            T nextPtr;
            while (cur != null && (nextPtr = cur.getNext()) != element)
                cur = nextPtr;
            if (cur != null)
                cur.setNext(element.getNext());
        }
        element.setNext(null);
        element.setHost(null);
    }

    public static<T extends Linkable<T, ?>> int size(T head) {
        int size = 0;
        for (T cur = head; cur != null; cur = cur.getNext())
            ++size;
        return size;
    }

    public static<T extends Linkable<T, ?>> void iterate(T head, IterationClosure<T> closure) {
        for (T cur = head; cur != null; cur = cur.getNext())
            closure.action(cur);
    }

    public static<T extends Linkable<T, H>, H> void iterate(HeadAccessor<T, H> ha, H host, MutativeIterationClosure<T, H> closure) {
        T cur = ha.getHead(host);
        while (cur != null && !closure.action(cur)) {
            T next = cur.getNext();
            cur.setNext(null);
            cur.setHost(null);
            cur = next;
            ha.setHead(host, cur);
        }
        if (cur == null)
            return;
        T prev = cur;
        cur = cur.getNext();
        while (cur != null) {
            if (!closure.action(cur)) {
                T next = cur.getNext();
                cur.setNext(null);
                cur.setHost(null);
                cur = next;
                prev.setNext(cur);
            }
            else {
                prev = cur;
                cur = cur.getNext();
            }
        }
    }
}
