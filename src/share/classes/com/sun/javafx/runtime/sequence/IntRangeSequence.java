package com.sun.javafx.runtime.sequence;

/**
 * Special case implementation for sequences that are ranges of integers, such as [1..10].  Range sequences should
 * be constructed with the Sequences.range() factory method rather than with the IntRangeSequence constructor.
 * O(1) space and time construction costs.
 *
 * @author Brian Goetz
 */
class IntRangeSequence extends AbstractSequence<Integer> implements Sequence<Integer> {

    private final int lower, upper;


    public IntRangeSequence(int lower, int upper) {
        super(Integer.class);
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public int size() {
        return (upper >= lower) ? upper - lower + 1 : 0;
    }

    @Override
    public Integer get(int position) {
        return (position < 0 || position >= upper - lower + 1)
                ? nullValue
                : (lower + position);
    }

    @Override
    public void toArray(Object[] array, int destOffset) {
        for (int i = lower, index=destOffset; i <= upper; i++, index++)
            array[index] = i;
    }
}
