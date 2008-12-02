package com.sun.javafx.runtime.sequence;

import java.util.Random;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;

/**
 * DumbMutableSequenceTest
 *
 * @author Brian Goetz
 */
public class DumbMutableSequenceTest extends JavaFXTestCase {

    private static void replaceSlice(DumbMutableSequence<Integer> ds, int startPos, int endPos, Integer... values) {
        ds.replaceSlice(startPos, endPos, Sequences.make(TypeInfo.Integer, values));
    }

    private void assertEquals(DumbMutableSequence<Integer> ds, Integer... values) {
        assertEquals(ds.get(Integer.class), values);
    }

    public void testAdHoc() {
        DumbMutableSequence<Integer> ds = new DumbMutableSequence<Integer>();

        assertEquals(ds.get(Integer.class));
        replaceSlice(ds, 0, -1, 1, 2, 3, 4);
        assertEquals(ds, 1, 2, 3, 4);
        ds.testValid();

        replaceSlice(ds, 0, 0);
        assertEquals(ds, 2, 3, 4);
        ds.testValid();
        replaceSlice(ds, 1, 1);
        assertEquals(ds, 2, 4);
        replaceSlice(ds, 1, 1);
        assertEquals(ds, new Integer[]{2});
        replaceSlice(ds, 0, 0, 1, 2, 3, 4);
        assertEquals(ds, 1, 2, 3, 4);
        replaceSlice(ds, 1, 1, 8, 9, 10);
        assertEquals(ds, 1, 8, 9, 10, 3, 4);
        replaceSlice(ds, 5, 5, 5, 6, 7);
        assertEquals(ds, 1, 8, 9, 10, 3, 5, 6, 7);
        replaceSlice(ds, 1, 2);
        assertEquals(ds, 1, 10, 3, 5, 6, 7);
        ds.testValid();
    }

    public void testSet() {
        DumbMutableSequence<Integer> ds = new DumbMutableSequence<Integer>();

        replaceSlice(ds, 0, -1, 1, 2, 3, 4);
        assertEquals(ds, 1, 2, 3, 4);
        ds.testValid();
        for (int i=0; i<ds.size(); i++) {
            ds.set(i, i);
            assertEquals(i, (int) ds.get(i));
            ds.testValid();
        }
        assertEquals(ds, 0, 1, 2, 3);
        ds.set(4, 4);
        assertEquals(ds, 0, 1, 2, 3, 4);
        ds.testValid();
    }

    public void testGrowAndShrink() {
        DumbMutableSequence<Integer> ds = new DumbMutableSequence<Integer>();
        SequenceLocation<Integer> seq = SequenceVariable.make(TypeInfo.Integer);
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            replaceSlice(ds, ds.size(), ds.size() - 1, i);
            seq.insert(i);
            assertEquals(seq.getAsSequence(), ds.get(Integer.class));
            ds.testValid();
        }

        for (int i = 0; i < 10; i++) {
            replaceSlice(ds, 0, -1, i);
            seq.insertFirst(i);
            assertEquals(seq.getAsSequence(), ds.get(Integer.class));
            ds.testValid();
        }

        while (ds.size() > 0) {
            int n = r.nextInt(ds.size());
            replaceSlice(ds, n, n);
            seq.delete(n);
            assertEquals(seq.getAsSequence(), ds.get(Integer.class));
            ds.testValid();
        }
    }

    public void testRandomInsert() {
        DumbMutableSequence<Integer> ds = new DumbMutableSequence<Integer>();
        SequenceLocation<Integer> seq = SequenceVariable.make(TypeInfo.Integer);
        Random r = new Random();

        for (int i = 0; i < 100; i++) {
            int n = r.nextInt(ds.size() + 1);
            replaceSlice(ds, n, n-1, i);
            seq.replaceSlice(n, n-1, Sequences.make(TypeInfo.Integer, i));
            assertEquals(seq.getAsSequence(), ds.get(Integer.class));
            ds.testValid();
        }

        for (int i = 100; i < 200; i++) {
            int n = r.nextInt(ds.size());
            replaceSlice(ds, n, n, i, i);
            seq.replaceSlice(n, n, Sequences.make(TypeInfo.Integer, i, i));
            assertEquals(seq.getAsSequence(), ds.get(Integer.class));
            ds.testValid();
        }
    }
}
