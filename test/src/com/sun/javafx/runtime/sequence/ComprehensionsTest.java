package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * ComprehensionsTest
 *
 * @author Brian Goetz
 */
public class ComprehensionsTest extends JavaFXTestCase {

    /**  foreach (i in sequence) { 2*i } */
    public void testSimpleForeach() {
        Sequence<Integer> five = Sequences.range(0, 5);
        SequenceMapper<Integer, Integer> doubler = new SequenceMapper<Integer, Integer>() {
            public Integer map(Sequence<Integer> sequence, int index, Integer value) {
                return value*2;
            }
        };
        Sequence<Integer> doubled = five.map(Integer.class, doubler);
        assertEquals(doubled, 0, 2, 4, 6, 8, 10);
        assertEquals(doubled, doubled.flatten());
    }

    /** select x from foo where x > 3
     *  select x from foo where indexof x % 2 != 0
     */
    public void testSimpleSelect() {
        Sequence<Integer> five = Sequences.range(1, 5);
        Sequence<Integer> six = Sequences.range(1, 6);
        SequencePredicate<Integer> greaterThanThree = new SequencePredicate<Integer>() {
            public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                return value > 3;
            }
        };
        SequencePredicate<Integer> oddIndex = new SequencePredicate<Integer>() {
            public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                return index % 2 != 0;
            }
        };
        assertEquals(five.get(greaterThanThree), 4, 5);
        assertEquals(six.get(greaterThanThree), 4, 5, 6);
        assertEquals(five.get(oddIndex), 2, 4);
        assertEquals(six.get(oddIndex), 2, 4, 6);
    }

    /** foreach (i in outer) foreach (j in inner) { content } */
    public void test2Dforeach() {
        Sequence<Integer> outer = Sequences.range(1, 2);
        Sequence<Integer> inner = Sequences.range(1, 3);
        SequenceBuilder<Integer> sb = new SequenceBuilder<Integer>(Integer.class, outer.size() * inner.size());
        for (Integer i : outer) {
            for (Integer j : inner) {
                sb.add(1);
            }
        }
        Sequence<Integer> result = sb.toSequence();
        assertEquals(result, 1, 1, 1, 1, 1, 1);
        Sequence<Integer> c2dResult = new CartesianProduct2D<Integer, Integer, Integer>(Integer.class, outer, inner,
                new CartesianProduct2D.Mapper<Integer, Integer, Integer>() {
                    public Integer map(int index1, Integer value1, int index2, Integer value2) {
                        return 1;
                    }
                });
        assertEquals(result, c2dResult);

        Sequence<Integer> cnResult = new CartesianProduct<Integer>(Integer.class,
                new CartesianProduct.Mapper<Integer>() {
                    public Integer map(int[] indexes, Object[] values) {
                        return 1;
                    }
                }, outer, inner);
        assertEquals(result, cnResult);

        // outer = [ 1..2 ], inner = [ 1..3 ], content = [ i*j ]
        outer = Sequences.range(1, 2);
        inner = Sequences.range(1, 3);
        sb = new SequenceBuilder<Integer>(Integer.class, outer.size() * inner.size());
        for (Integer i : outer) {
            for (Integer j : inner) {
                sb.add(i*j);
            }
        }
        result = sb.toSequence();
        assertEquals(result, 1, 2, 3, 2, 4, 6);
        c2dResult = new CartesianProduct2D<Integer, Integer, Integer>(Integer.class, outer, inner,
                new CartesianProduct2D.Mapper<Integer, Integer, Integer>() {
                    public Integer map(int index1, Integer value1, int index2, Integer value2) {
                        return value1 * value2;
                    }
                });
        assertEquals(result, c2dResult);

        cnResult = new CartesianProduct<Integer>(Integer.class,
                new CartesianProduct.Mapper<Integer>() {
                    public Integer map(int[] indexes, Object[] values) {
                        return ((Integer) values[0]) * ((Integer) values[1]);
                    }
                }, outer, inner);
        assertEquals(result, cnResult);
    }

    /** foreach (i in [1..2], j in [1..3], k in [1..4]) { i*j*k } */
    public void test3Dforeach() {
        Sequence<Integer> first = Sequences.range(1, 2);
        Sequence<Integer> second = Sequences.range(1, 3);
        Sequence<Integer> third = Sequences.range(1, 4);
        SequenceBuilder<Integer> sb = new SequenceBuilder<Integer>(Integer.class);
        for (Integer i : first) {
            for (Integer j : second) {
                for (Integer k : third) {
                    sb.add(i*j*k);
                }
            }
        }
        Sequence<Integer> result = sb.toSequence();
        assertEquals(result, 1, 2, 3, 4, 2, 4, 6, 8, 3, 6, 9, 12, 2, 4, 6, 8, 4, 8, 12, 16, 6, 12, 18, 24);

        Sequence<Integer> cnResult = new CartesianProduct<Integer>(Integer.class,
                new CartesianProduct.Mapper<Integer>() {
                    public Integer map(int[] indexes, Object[] values) {
                        return ((Integer) values[0]) * ((Integer) values[1]) * ((Integer) values[2]);
                    }
                }, first, second, third);
        assertEquals(result, cnResult);
    }
}
