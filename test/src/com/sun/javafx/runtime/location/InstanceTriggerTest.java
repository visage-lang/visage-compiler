package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * InstanceTriggerTest
 *
 * @author Brian Goetz
 */
public class InstanceTriggerTest extends JavaFXTestCase {

    public void testChangeTrigger() {
        final IntLocation v = IntVar.make(3);
        CountingListener cl = new CountingListener();
        v.addChangeListener(cl);
        assertEquals(0, cl.count);
        v.set(3);
        assertEquals(1, cl.count);
    }

    /** This test relies on System.gc() actually performing a collection to test that dead listeners are actually
     * (lazily) removed from the listener list.  
     */
    public void testWeakRef() {
        final IntLocation v = IntVar.make(3);
        for (int i=0; i<1; i++) {
            CountingListener cl = new CountingListener();
            v.addWeakListener(cl);
            assertEquals(0, cl.count);
            v.set(v.get() + 1);
            assertEquals(1, cl.count);
            assertEquals(i+1, ((AbstractLocation) v).getListenerCount());
        }

        // "Force" GC, make sure weak listener goes away
        System.gc();
        v.set(0);
        assertEquals(0, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure weak listener stays around
        CountingListener cl = new CountingListener();
        v.addWeakListener(cl);
        assertEquals(0, cl.count);
        v.set(2);
        assertEquals(1, cl.count);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());
        System.gc();
        v.set(5);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure strong but out-of-scope listener stays around
        final IntLocation u = IntVar.make(3);
        for (int i=0; i<1; i++) {
            cl = new CountingListener();
            v.addChangeListener(cl);
            assertEquals(0, cl.count);
            v.set(v.get() + 1);
            assertEquals(1, cl.count);
        }
        System.gc();
        v.set(0);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());
    }
}
