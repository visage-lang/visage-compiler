package com.sun.javafx.runtime.sequence;

/**
 * Special case implementation for sequences that are ranges of integers, such as [1..10].  Range sequences should
 * be constructed with the Sequences.range() factory method rather than with the IntRangeSequence constructor.  An
 * optional "step" allows sequences to vary by more than one; [ 1..5 STEP 2 ] is [ 1, 3, 5 ].  
 * O(1) space and time construction costs.
 *
 * @author Brian Goetz
 */
class IntRangeSequence extends AbstractSequence<Integer> implements Sequence<Integer> {

    private final int start, bound, step, size;


    public IntRangeSequence(int start, int bound, int step) {
        super(Integer.class);
        this.start = start;
        this.bound = bound;
        this.step = step;
        if (bound == start) {
            this.size = 1;
        }
        else if (bound > start) {
            this.size = step > 0 ? (((bound - start) / step) + 1) : 0;
        }
        else {
            this.size = step < 0 ? (((start - bound) / -step) + 1) : 0;
        }
    }


    public IntRangeSequence(int start, int bound) {
        this(start, bound, 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Integer get(int position) {
        return (position < 0 || position >= size)
                ? nullValue
                : (start + position * step);
    }

    @Override
    public void toArray(Object[] array, int destOffset) {
        for (int value = start, index = destOffset; index < destOffset+size; value += step, index++)
            array[index] = value;
    }
}
