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

    public int size() {
        return (upper >= lower) ? upper - lower + 1: 0;
    }

    public Integer get(int position) {
        return (position < 0 || position >= upper-lower+1) 
                ? null
                : (lower + position);
    }
}
