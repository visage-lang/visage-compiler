package com.sun.javafx.runtime.sequence;

import junit.framework.TestCase;

import java.util.BitSet;

/**
 * TestIntegerSequence
 *
 * @author Brian Goetz
 */
public class IntegerArraySequenceTest extends TestCase {

    private final Sequence<Integer> EMPTY_SEQUENCE = EmptySequence.get(Integer.class);
    private final Sequence<Integer> ONE_SEQUENCE = new ArraySequence<Integer>(Integer.class, 1);

    private final SequenceSelector<Integer> nullMatcher = new SequenceSelector<Integer>() {
        public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
            return false;
        }
    };
    private final SequenceSelector<Integer> allMatcher = new SequenceSelector<Integer>() {
        public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
            return true;
        }
    };
    private final SequenceSelector<Integer> firstMatcher = new SequenceSelector<Integer>() {
        public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
            return index == 0;
        }
    };
    private final SequenceSelector<Integer> lastMatcher = new SequenceSelector<Integer>() {
        public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
            return index == sequence.size() - 1;
        }
    };

    private void emptySequenceAssertions(Sequence<Integer> seq) {
        assertEquals(0, seq.size());
        assertTrue(seq.isEmpty());
        assertEquals(seq, seq);
        assertEquals(0, seq.hashCode());
        assertEquals(null, seq.get(-1));
        assertEquals(null, seq.get(0));
        assertEquals(null, seq.get(1));
        assertEquals("[ ]", seq.toString());
        assertEquals(seq, EMPTY_SEQUENCE);
        assertEquals(seq, seq.get(nullMatcher));
        assertEquals(seq, seq.get(firstMatcher));
        assertEquals(seq, seq.get(lastMatcher));
        assertEquals(seq, seq.get(allMatcher));
        assertFalse(seq.equals(ONE_SEQUENCE));
    }

    private void oneElementAssertions(Sequence<Integer> seq, int value) {
        assertEquals(1, seq.size());
        assertFalse(seq.isEmpty());
        assertEquals(seq, seq);
        assertEquals(value, seq.hashCode());
        assertEquals("[ " + value + " ]", seq.toString());
        assertEquals(null, seq.get(-1));
        assertEquals(new Integer(value), seq.get(0));
        assertEquals(null, seq.get(1));
        emptySequenceAssertions(seq.get(nullMatcher));
        assertEquals(seq, seq.get(firstMatcher));
        assertEquals(seq, seq.get(lastMatcher));
        assertEquals(seq, seq.get(allMatcher));
        assertFalse(seq.equals(EMPTY_SEQUENCE));
    }

    
    private void emptyHelper(Sequence<Integer> seq) {
        emptySequenceAssertions(seq);

        // Test insertion
        oneElementAssertions(seq.insert(3), 3);
        oneElementAssertions(seq.insertFirst(3), 3);
        oneElementAssertions(seq.insert(new SingletonSequence<Integer>(Integer.class, 4)), 4);
        oneElementAssertions(seq.insertFirst(new SingletonSequence<Integer>(Integer.class, 4)), 4);

        // Test deletion
        emptySequenceAssertions(seq.delete(0));
        emptySequenceAssertions(seq.delete(1));
        emptySequenceAssertions(seq.delete(-1));
        emptySequenceAssertions(seq.delete(allMatcher));
        emptySequenceAssertions(seq.delete(nullMatcher));
        emptySequenceAssertions(seq.delete(firstMatcher));
        emptySequenceAssertions(seq.delete(lastMatcher));
    }

    public void testEmptySequence() {
        emptyHelper(new ArraySequence<Integer>(Integer.class, EMPTY_SEQUENCE));
        emptyHelper(new ArraySequence<Integer>(Integer.class, new Integer[0]));
        emptyHelper(new IntRangeSequence(0, -1));
        emptyHelper(new CompositeSequence<Integer>(Integer.class));
        emptyHelper(new CompositeSequence<Integer>(Integer.class, EMPTY_SEQUENCE));
        emptyHelper(new CompositeSequence<Integer>(Integer.class, EMPTY_SEQUENCE, EMPTY_SEQUENCE));
        emptyHelper(new FilterSequence<Integer>(EMPTY_SEQUENCE, new BitSet()));
        emptyHelper(new FilterSequence<Integer>(ONE_SEQUENCE, new BitSet()));
    }

    private void oneElementHelper(Sequence<Integer> seq, int value) {
        oneElementAssertions(seq, value);

        // Test bulk extraction
        emptySequenceAssertions(seq.get(nullMatcher));
        oneElementAssertions(seq.get(allMatcher), value);
        oneElementAssertions(seq.get(firstMatcher), value);
        oneElementAssertions(seq.get(lastMatcher), value);

        // Test deletion
        emptySequenceAssertions(seq.delete(0));
        oneElementAssertions(seq.delete(1), value);
        oneElementAssertions(seq.delete(-1), value);
    }

    public void testOneElementArraySequence() {
        oneElementHelper(new ArraySequence<Integer>(Integer.class, 1), 1);
        oneElementHelper(new SingletonSequence<Integer>(Integer.class, 3), 3);
        oneElementHelper(new IntRangeSequence(2, 2), 2);
        oneElementHelper(new CompositeSequence<Integer>(Integer.class, EMPTY_SEQUENCE, ONE_SEQUENCE), 1);
        oneElementHelper(new CompositeSequence<Integer>(Integer.class, ONE_SEQUENCE, EMPTY_SEQUENCE), 1);
        BitSet oneBit = new BitSet();
        oneBit.set(0);
        oneElementHelper(new FilterSequence<Integer>(ONE_SEQUENCE, oneBit), 1);
    }

    public void testTwoElementSequence() {

    }
}
