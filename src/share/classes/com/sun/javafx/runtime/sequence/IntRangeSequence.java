package com.sun.javafx.runtime.sequence;

/**
 * IntRangeSequence
 *
 * @author Brian Goetz
 */
public class IntRangeSequence extends AbstractSequence<Integer> implements Sequence<Integer> {

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
