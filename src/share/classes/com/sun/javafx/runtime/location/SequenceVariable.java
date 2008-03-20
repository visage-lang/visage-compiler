package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.sequence.*;

/**
 * SequenceVariable
 *
 * @author Brian Goetz
 */
public class SequenceVariable<T>
        extends AbstractVariable<Sequence<T>, SequenceBindingExpression<T>>
        implements SequenceLocation<T> {

    private final SequenceMutator.Listener<T> mutationListener;
    private final SequenceHelper<T> helper;


    public static <T> SequenceVariable<T> make(Class<T> clazz) {
        return new SequenceVariable<T>(clazz);
    }

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

    public static<T> SequenceVariable<T> make(Class<T> clazz, SequenceLocation<T> otherLocation) {
        SequenceVariable<T> me = make(clazz);
        me.bind(otherLocation);
        return me;
    }

    /** Create a bijectively bound variable */
    public static<T> SequenceVariable<T> makeBijective(Class<T> clazz, SequenceVariable<T> other) {
        SequenceVariable<T> me = SequenceVariable.<T>make(clazz);
        me.bijectiveBind(other);
        return me;
    }

    protected SequenceVariable(Class<T> clazz) {
        helper = new SequenceHelper<T>(clazz) {
            protected void ensureValid() {
                if (isBound() && !isValid())
                    update();
            }

            protected boolean isInitialized() {
                return SequenceVariable.this.isInitialized();
            }

            protected void setValid() {
                SequenceVariable.this.setValid();
            }

            protected void valueChanged() {
                SequenceVariable.this.valueChanged();
            }
        };
        mutationListener = new SequenceMutator.Listener<T>() {
            public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                helper.replaceSlice(startPos, endPos, newElements, newValue);
            }
        };
    }

    protected SequenceVariable(Sequence<T> value) {
        this(value.getElementType());
        helper.replaceValue(value);
    }

    protected SequenceVariable(Class<T> clazz, Sequence<? extends T> value) {
        this(clazz);
        if (value == null)
            value = Sequences.emptySequence(clazz);
        helper.replaceValue(Sequences.upcast(clazz, value));
    }

    protected SequenceVariable(Class<T> clazz, boolean lazy, SequenceBindingExpression<T> binding, Location... dependencies) {
        this(clazz);
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    private Sequence<T> getRawValue() {
        return helper.getRawValue();
    }

    public Sequence<T> get() {
        return helper.get();
    }

    public T get(int position) {
        return helper.get(position);
    }

    public Sequence<T> getAsSequence() {
        return helper.getAsSequence();
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return helper.getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return helper.isNull();
    }

    public void bind(final SequenceLocation<T> otherLocation) {
        bind(false, new SequenceBindingExpression<T>() {
            public Sequence<T> computeValue() {
                return otherLocation.getAsSequence();
            }
        }, otherLocation);
    }

    public void bindFromLiteral(final SequenceLocation<T> otherLocation) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(otherLocation);
            }
        };
    }

    public void addChangeListener(SequenceChangeListener<T> listener) {
        helper.addChangeListener(listener);
    }

    public void addChangeListener(ObjectChangeListener<Sequence<T>> listener) {
        helper.addChangeListener(listener);
    }

    public void removeChangeListener(SequenceChangeListener<T> listener) {
        helper.removeChangeListener(listener);
    }

    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    public Iterator<T> iterator() {
        return helper.iterator();
    }

    @Override
    public void update() {
        try {
            if (isBound() && !isValid())
                helper.replaceValue(Sequences.upcast(helper.getClazz(), binding.computeValue()));
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                helper.replaceValue(Sequences.emptySequence(helper.getClazz()));
        }
    }

    private void ensureNotBound() {
        if (isBound())
            throw new BindingException("Cannot mutate bound sequence");
    }

    public Sequence<T> set(Sequence<T> value) {
        ensureNotBound();
        Sequence<T> v = getRawValue();
        if (SequenceHelper.equals(v, value))
            return v;
        else
            return SequenceMutator.replaceSlice(v, mutationListener, 0, Sequences.size(v) - 1, value);
    }

    public void setDefault() {
        setAsSequence(Sequences.emptySequence(helper.getClazz()));
    }

    public Sequence<T> setAsSequence(Sequence<? extends T> value) {
        Sequence<T> result;
        ensureNotBound();
        Sequence<T> value1 = getRawValue();
        if (SequenceHelper.equals(value1, value))
            result = value1;
        else {
            result = SequenceMutator.replaceSlice(value1, mutationListener, 0, Sequences.size(value1) - 1, value);
        }
        return result;
    }

    public Sequence<T> setAsSequenceFromLiteral(final Sequence<? extends T> value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsSequence(value);
            }
        };
        return Sequences.upcast(helper.getClazz(), value);
    }

    @Override
    public T set(int position, T newValue) {
        ensureNotBound();
        SequenceMutator.set(getRawValue(), mutationListener, position, newValue);
        return newValue;
    }

    @Override
    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        ensureNotBound();
        SequenceMutator.replaceSlice(getRawValue(), mutationListener, startPos, endPos, newValues);
        return newValues;
    }

    @Override
    public void delete(int position) {
        ensureNotBound();
        SequenceMutator.delete(getRawValue(), mutationListener, position);
    }

    @Override
    public void deleteSlice(int startPos, int endPos) {
        replaceSlice(startPos, endPos, null);
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.delete(getRawValue(), mutationListener, sequencePredicate);
    }

    @Override
    public void deleteAll() {
        ensureNotBound();
        setAsSequence(Sequences.emptySequence(helper.getClazz()));
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
        SequenceMutator.insert(getRawValue(), mutationListener, value);
    }

    @Override
    public void insert(Sequence<? extends T> values) {
        ensureNotBound();
        SequenceMutator.insert(getRawValue(), mutationListener, values);
    }

    public void insertFirst(T value) {
        ensureNotBound();
        SequenceMutator.insertFirst(getRawValue(), mutationListener, value);
    }

    @Override
    public void insertFirst(Sequence<? extends T> values) {
        ensureNotBound();
        SequenceMutator.insertFirst(getRawValue(), mutationListener, values);
    }

    @Override
    public void insertBefore(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertBefore(getRawValue(), mutationListener, value, position);
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertBefore(getRawValue(), mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        ensureNotBound();
        SequenceMutator.insertBefore(getRawValue(), mutationListener, values, position);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertBefore(getRawValue(), mutationListener, values, sequencePredicate);
    }

    @Override
    public void insertAfter(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertAfter(this.getRawValue(), mutationListener, value, position);
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertAfter(getRawValue(), mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, int position) {
        ensureNotBound();
        SequenceMutator.insertAfter(getRawValue(), mutationListener, values, position);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertAfter(getRawValue(), mutationListener, values, sequencePredicate);
    }
}
