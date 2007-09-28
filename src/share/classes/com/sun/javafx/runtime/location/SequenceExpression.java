package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.util.Iterator;

/**
 * SequenceExpression represents an integer-value bound expression.  Associated with an SequenceExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  SequenceExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class SequenceExpression<T> extends AbstractLocation implements SequenceLocation<T> {

    private final SequenceBindingExpression<T> expression;
    private Sequence<T> value;

    /** Create an SequenceExpression with the specified expression and dependencies. */
    public static<T> SequenceLocation<T> make(SequenceBindingExpression<T> exp, Location... dependencies) {
        SequenceExpression<T> loc = new SequenceExpression<T>(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    /** Create a lazy SequenceExpression with the specified expression and dependencies. */
    public static<T> SequenceLocation<T> makeLazy(SequenceBindingExpression<T> exp, Location... dependencies) {
        SequenceExpression<T> loc = new SequenceExpression<T>(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    private SequenceExpression(boolean lazy, SequenceBindingExpression<T> expression) {
        super(false, lazy);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return getSequence().toString(); 
    }

    @Override
    public Iterator<T> iterator() {
        return getSequence().iterator(); 
    }

    @Override
    public T get(int position) {
        return getSequence().get(position);
    }
    
    @Override
    public SequenceLocation<T> get() {
        return this;
    }

    @Override
    public void set(SequenceLocation<T> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sequence<T> getSequence() {
        if (!isValid())
            update();
        return value;
    }

    public void set(Sequence<T> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get().getSequence();
            setValid();
        }
    }


    @Override
    public void set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Sequence<T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(Sequence<T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }
}
