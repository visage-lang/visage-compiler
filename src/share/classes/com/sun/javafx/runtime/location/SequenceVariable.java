package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.sequence.SequenceMutator;
import com.sun.javafx.runtime.sequence.SequencePredicate;

/**
 * SequenceVariable
 *
 * @author Brian Goetz
 */
public class SequenceVariable<T> extends AbstractSequenceLocation<T>
        implements SequenceLocation<T>, BindableLocation<SequenceBindingExpression<T>> {

    private final SequenceMutator.Listener<T> mutationListener = new MutationListener();
    protected SequenceBindingExpression<T> binding;
    protected boolean isLazy;

    public static <T> SequenceVariable<T> make(Sequence<T> value) {
        return new SequenceVariable<T>(value);
    }

    public static <T> SequenceVariable<T> make(Class<T> clazz, Sequence<? extends T> value) {
        return new SequenceVariable<T>(clazz, value);
    }

    public static<T> SequenceVariable<T> make(Class<T> clazz, boolean lazy, SequenceBindingExpression<T> binding, Location... dependencies) {
        return new SequenceVariable<T>(clazz, lazy, binding, dependencies);
    }

    public static<T> SequenceVariable<T> make(Class<T> clazz, SequenceBindingExpression<T> binding, Location... dependencies) {
        return new SequenceVariable<T>(clazz, false, binding, dependencies);
    }

    private SequenceVariable(Sequence<T> value) {
        super(value.getElementType(), true);
        replaceValue(value);
    }

    private SequenceVariable(Class<T> clazz, Sequence<? extends T> value) {
        super(clazz, true);
        if (value == null)
            throw new NullPointerException();
        replaceValue(Sequences.upcast(clazz, value));
    }

    public SequenceVariable(Class<T> clazz, boolean lazy, SequenceBindingExpression<T> binding, Location... dependencies) {
        super(clazz, false);
        bind(binding, lazy);
        addDependencies(dependencies);
    }


    public boolean isMutable() {
        return !isBound();
    }

    public Sequence<T> getAsSequence() {
        if (isBound() && !isValid())
            update();
        return super.getAsSequence();
    }

    private void ensureValid() {
        if (isBound() && !isValid())
            update();
    }

    private void ensureNotBound() {
        if (isBound())
            throw new BindingException("Cannot mutate bound sequence");
    }

    @Override
    public String toString() {
        ensureValid();
        return super.toString();
    }

    @Override
    public Iterator<T> iterator() {
        ensureValid();
        return super.iterator();
    }

    @Override
    public T get(int position) {
        ensureValid();
        return super.get(position);
    }

    @Override
    public Sequence<T> setAsSequence(Sequence<? extends T> newValue) {
        ensureNotBound();
        if (equals($value, newValue))
            return $value;
        else
            return SequenceMutator.replaceSlice($value, mutationListener, 0, Sequences.size($value) - 1, newValue);
    }

    @Override
    public T set(int position, T newValue) {
        ensureNotBound();
        SequenceMutator.set($value, mutationListener, position, newValue);
        return newValue;
    }

    @Override
    public void replaceSlice(int startPos, int endPos, Sequence<T> newValues) {
        ensureNotBound();
        SequenceMutator.replaceSlice($value, mutationListener, startPos, endPos, newValues);
    }

    @Override
    public void delete(int position) {
        ensureNotBound();
        SequenceMutator.delete($value, mutationListener, position);
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.delete($value, mutationListener, sequencePredicate);
    }

    @Override
    public void deleteAll() {
        ensureNotBound();
        setAsSequence(Sequences.emptySequence(clazz));
    }

    @Override
    public void deleteValue(final T targetValue) {
        ensureNotBound();
        delete(new SequencePredicate<T>() {
            public boolean matches(Sequence<? extends T> sequence, int index, T value) {
                if (value == null)
                    return targetValue == null;
                return value.equals(targetValue);
            }
        });
    }

    @Override
    public void insert(T value) {
        ensureNotBound();
        SequenceMutator.insert(this.$value, mutationListener, value);
    }

    @Override
    public void insert(Sequence<? extends T> values) {
        ensureNotBound();
        SequenceMutator.insert($value, mutationListener, values);
    }

    public void insertFirst(T value) {
        ensureNotBound();
        SequenceMutator.insertFirst(this.$value, mutationListener, value);
    }

    @Override
    public void insertFirst(Sequence<? extends T> values) {
        ensureNotBound();
        SequenceMutator.insertFirst($value, mutationListener, values);
    }

    @Override
    public void insertBefore(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertBefore(this.$value, mutationListener, value, position);
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertBefore(this.$value, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        ensureNotBound();
        SequenceMutator.insertBefore($value, mutationListener, values, position);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertBefore($value, mutationListener, values, sequencePredicate);
    }

    @Override
    public void insertAfter(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertAfter(this.$value, mutationListener, value, position);
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertAfter(this.$value, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, int position) {
        ensureNotBound();
        SequenceMutator.insertAfter($value, mutationListener, values, position);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertAfter($value, mutationListener, values, sequencePredicate);
    }

    @Override
    public void update() {
        if (isBound() && !isValid())
            replaceValue(Sequences.upcast(clazz, binding.computeValue()));
    }

    @Override
    public void invalidate() {
        if (isBound()) {
            super.invalidate();
            if (!isLazy())
                update();
        }
        else
            throw new BindingException("Cannot invalidate non-bound variable");
    }

    public void bind(SequenceBindingExpression<T> binding, boolean lazy) {
        if (isBound())
            throw new BindingException("Cannot rebind variable");
        else if (isInitialized())
            throw new BindingException("Cannot bind variable that already has a value");
        this.binding = binding;
        binding.setLocation(this);
        isLazy = lazy;
    }

    public boolean isBound() {
        return binding != null;
    }

    public boolean isLazy() {
        return isLazy;
    }

    private class MutationListener implements SequenceMutator.Listener<T> {
        public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
            SequenceVariable.this.replaceSlice(startPos, endPos, newElements, newValue);
        }
    }
}
