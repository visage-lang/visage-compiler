/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequenceMutator;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * SequenceVariable
 *
 * @author Brian Goetz
 */
public class SequenceVariable<T>
        extends AbstractVariable<Sequence<T>, SequenceLocation<T>, SequenceBindingExpression<T>, SequenceChangeListener<T>>
        implements SequenceLocation<T> {

    private final Class<T> clazz;
    private final SequenceMutator.Listener<T> mutationListener;
    private Sequence<T> $value;
    private BoundLocationInfo boundLocation;


    public static <T> SequenceVariable<T> make(Class clazz) {
        return new SequenceVariable<T>(clazz);
    }

    public static <T> SequenceVariable<T> make(Sequence<T> value) {
        return new SequenceVariable<T>(value);
    }

    public static <T> SequenceVariable<T> make(Class clazz, Sequence<? extends T> value) {
        return new SequenceVariable<T>(clazz, value);
    }

    public static <T> SequenceVariable<T> make(Class clazz, boolean lazy, SequenceBindingExpression<T> binding, Location... dependencies) {
        return new SequenceVariable<T>(clazz, lazy, binding, dependencies);
    }

    public static <T> SequenceVariable<T> make(Class clazz, SequenceBindingExpression<T> binding, Location... dependencies) {
        return new SequenceVariable<T>(clazz, false, binding, dependencies);
    }

    /**
     * Create a bijectively bound variable
     */
    public static <T> SequenceVariable<T> makeBijective(Class clazz, SequenceVariable<T> other) {
        SequenceVariable<T> me = SequenceVariable.<T>make(clazz);
        me.bijectiveBind(other);
        return me;
    }

    protected SequenceVariable(Class clazz) {
        this.clazz = clazz;
        this.$value = Sequences.<T>emptySequence(this.clazz);
        this.mutationListener = new SequenceMutator.Listener<T>() {
            public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                replaceSlice(startPos, endPos, newElements, newValue);
            }
        };
    }

    protected SequenceVariable(Sequence<T> value) {
        this(value.getElementType());
        replaceValue(value);
    }

    protected SequenceVariable(Class clazz, Sequence<? extends T> value) {
        this(clazz);
        if (value == null)
            value = Sequences.emptySequence(this.clazz);
        replaceValue(Sequences.<T>upcast(this.clazz, value));
    }

    protected SequenceVariable(Class clazz, boolean lazy, SequenceBindingExpression<T> binding, Location... dependencies) {
        this(clazz);
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    private void ensureValid() {
        if (isUnidirectionallyBound() && !isValid())
            update();
    }

    /**
     * Update the held value, notifying change listeners
     */
    private Sequence<T> replaceValue(Sequence<T> newValue) {
        assert (boundLocation == null);
        if (newValue == null)
            newValue = Sequences.emptySequence(clazz);
        return replaceSlice(0, Sequences.size($value) - 1, newValue, newValue);
    }

    /**
     * Update the held value, notifying change listeners
     */
    private Sequence<T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> newValue) {
        assert (boundLocation == null);
        Sequence<T> oldValue = $value;

        if (preReplace(!Sequences.sliceEqual(oldValue, startPos, endPos, newElements))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            $value = newValue;
            setValid();
            notifyListeners(startPos, endPos, newElements, oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return getRawValue();
    }


    private Sequence<T> getRawValue() {
        return $value;
    }

    public Sequence<T> get() {
        return getAsSequence();
    }

    public T get(int position) {
        return getAsSequence().get(position);
    }

    public Sequence<T> getAsSequence() {
        ensureValid();
        return $value;
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    protected SequenceBindingExpression<T> makeBindingExpression(final SequenceLocation<T> otherLocation) {
        return new SequenceBindingExpression<T>() {
            public Sequence<T> computeValue() {
                return otherLocation.getAsSequence();
            }
        };
    }

    public void addChangeListener(final ObjectChangeListener<Sequence<T>> listener) {
        addChangeListener(new SequenceChangeListener<T>() {
            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(int startPos, int endPos,
                                   Sequence<? extends T> newElements,
                                   Sequence<T> oldValue, Sequence<T> newValue,
                                   boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (SequenceChangeListener<T> listener : replaceListeners)
                try {
                    listener.onChange(startPos, endPos, newElements, oldValue, newValue);
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
        }
    }

    public void bind(SequenceLocation<T> otherLocation) {
        ensureBindable();
        Sequence<T> oldValue = $value;
        $value = otherLocation.get();
        boundLocation = new BoundLocationInfo(otherLocation);
        boundLocation.bind();
        notifyListeners(0, Sequences.size(oldValue) - 1, $value, oldValue, $value, true);
    }

    protected void rebind(SequenceLocation<T> otherLocation) {
        if (boundLocation != null)
            boundLocation.unbind();
        boundLocation = null;
        bind(otherLocation);
    }

    protected boolean isUnidirectionallyBound() {
        return super.isUnidirectionallyBound() || (boundLocation != null);
    }

    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    public Iterator<T> iterator() {
        return getAsSequence().iterator();
    }

    @Override
    public void update() {
        try {
            if (isUnidirectionallyBound() && !isValid() && boundLocation == null) {
                replaceValue(Sequences.upcast(clazz, binding.computeValue()));
            }
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(Sequences.emptySequence(clazz));
        }
    }

    private void ensureNotBound() {
        if (isUnidirectionallyBound())
            throw new AssignToBoundException("Cannot mutate bound sequence");
    }

    public Sequence<T> set(Sequence<T> value) {
        return setAsSequence(value);
    }

    public void setDefault() {
        Sequence<T> empty = Sequences.emptySequence(clazz);
        if (state == STATE_INITIAL) {
            $value = Sequences.emptySequence(clazz);
            state = STATE_UNBOUND_DEFAULT;
            // @@@ Uncomment this to make sequence trigger behavior consistent with others (see JFXC-885)
            // notifyListeners(0, -1, $value, $value, $value, true);
        }
        else
            setAsSequence(empty);
    }

    public Sequence<T> setAsSequence(Sequence<? extends T> newValue) {
        Sequence<T> result;
        ensureNotBound();
        Sequence<T> oldValue = getRawValue();
        state = STATE_UNBOUND;
        // @@@ To make sequence triggers consistent with others (more JFXC-885), use replaceValue() instead
        if (!Sequences.isEqual(oldValue, newValue) || state == STATE_UNBOUND && !isValid()) {
            result = SequenceMutator.replaceSlice(oldValue, mutationListener, 0, Sequences.size(oldValue) - 1, newValue);
        }
        else
            result = oldValue;
        return result;
    }

    public Sequence<T> setAsSequenceFromLiteral(final Sequence<? extends T> value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsSequence(value);
            }
        };
        return Sequences.upcast(clazz, value);
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

    private class BoundLocationInfo {
        public final SequenceLocation<T> otherLocation;
        public ChangeListener changeListener;
        public SequenceChangeListener<T> sequenceChangeListener;

        BoundLocationInfo(SequenceLocation<T> otherLocation) {
            this.otherLocation = otherLocation;
        }

        void bind() {
            changeListener = new ChangeListener() {
                public boolean onChange() {
                    invalidateDependencies();
                    return true;
                }
            };
            sequenceChangeListener = new SequenceChangeListener<T>() {
                public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                    $value = newValue;
                    // @@@ Right value of notifyDependencies?
                    notifyListeners(startPos, endPos, newElements, oldValue, newValue, false);
                }
            };
            otherLocation.addChangeListener(changeListener);
            otherLocation.addChangeListener(sequenceChangeListener);
        }

        void unbind() {
            otherLocation.removeChangeListener(changeListener);
            otherLocation.removeChangeListener(sequenceChangeListener);
            changeListener = null;
            sequenceChangeListener = null;
        }
    }
}


