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
import com.sun.javafx.runtime.TypeInfo;
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
        extends AbstractVariable<Sequence<T>, SequenceLocation<T>, SequenceChangeListener<T>>
        implements SequenceLocation<T> {

    private final TypeInfo<T, ?> typeInfo;
    private final SequenceMutator.Listener<T> mutationListener;
    private Sequence<T> $value;
    private BoundLocationInfo boundLocation;


    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo) {
        return new SequenceVariable<T>(typeInfo);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, Sequence<? extends T> value) {
        return new SequenceVariable<T>(typeInfo, value);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, boolean lazy, BindingExpression binding, Location... dependencies) {
        return new SequenceVariable<T>(typeInfo, lazy, binding, dependencies);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, BindingExpression binding, Location... dependencies) {
        return new SequenceVariable<T>(typeInfo, false, binding, dependencies);
    }

    /**
     * Create a bijectively bound variable
     */
    public static <T> SequenceVariable<T> makeBijective(TypeInfo<T, ?> typeInfo, SequenceVariable<T> other) {
        SequenceVariable<T> me = SequenceVariable.<T>make(typeInfo);
        me.bijectiveBind(other);
        return me;
    }

    protected SequenceVariable(TypeInfo<T, ?> typeInfo) {
        this.typeInfo = typeInfo;
        this.$value = typeInfo.emptySequence;
        this.mutationListener = new SequenceMutator.Listener<T>() {
            public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                replaceSlice(startPos, endPos, newElements, newValue);
            }

            public void onReplaceElement(int startPos, int endPos, T newElement, Sequence<T> oldValue, Sequence<T> newValue) {
                replaceElement(startPos, endPos, newElement, newValue);
            }
        };
    }

    protected SequenceVariable(TypeInfo<T, ?> typeInfo, Sequence<? extends T> value) {
        this(typeInfo);
        if (value == null)
            value = typeInfo.emptySequence;
        replaceValue(Sequences.<T>upcast(value));
    }

    protected SequenceVariable(TypeInfo<T, ?> typeInfo, boolean lazy, BindingExpression binding, Location... dependencies) {
        this(typeInfo);
        bind(lazy, binding);
        addDependency(dependencies);
    }

    /**
     * Update the held value, notifying change listeners
     */
    protected Sequence<T> replaceValue(Sequence<T> newValue) {
        assert (boundLocation == null);
        if (newValue == null)
            newValue = typeInfo.emptySequence;
        return replaceSlice(0, Sequences.size($value) - 1, newValue, newValue);
    }

    /**
     * Update the held value, notifying change listeners
     */
    private Sequence<T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> newValue) {
        assert (boundLocation == null);
        Sequence<T> oldValue = $value;

        if (!hasDependencies()) {
            preReplace(true);
            $value = newValue;
            setValid();
        }
        else if (preReplace(!Sequences.sliceEqual(oldValue, startPos, endPos, newElements))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            $value = newValue;
            setValid();
            notifyListeners(startPos, endPos, newElements, oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return $value;
    }

    /**
     * Optimized version, for single-element updates
     */
    private Sequence<T> replaceElement(int startPos, int endPos, T newElement, Sequence<T> newValue) {
        assert (boundLocation == null);
        Sequence<T> oldValue = $value;

        if (!hasDependencies()) {
            preReplace(true);
            $value = newValue;
            setValid();
        }
        else if (preReplace(startPos != endPos || !newElement.equals(oldValue.get(startPos)))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            $value = newValue;
            setValid();
            Sequence<T> newElements = Sequences.singleton(oldValue.getElementType(), newElement);
            notifyListeners(startPos, endPos, newElements, oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return $value;
    }


    public TypeInfo<T, ?> getElementType() {
        return typeInfo;
    }

    public Sequence<T> get() {
        return getAsSequence();
    }

    public T get(int position) {
        return getAsSequenceRaw().get(position);
    }

    public Sequence<T> getAsSequence() {
        ensureValid();
        Sequences.noteShared($value);
        return $value;
    }

    /** Same as getAsSequence, but don't call noteShared. */
    public Sequence<T> getAsSequenceRaw() {
        ensureValid();
        return $value;
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    protected BindingExpression makeBindingExpression(final SequenceLocation<T> otherLocation) {
        return new BindingExpression() {
            public void compute() {
                pushValue(otherLocation.getAsSequence());
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

    protected void notifyListeners(final int startPos, final int endPos,
                                   final Sequence<? extends T> newElements,
                                   final Sequence<T> oldValue, final Sequence<T> newValue,
                                   boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (hasChildren(CHILD_KIND_TRIGGER))
            iterateChildren(new DependencyIterator<SequenceChangeListener<T>>(CHILD_KIND_TRIGGER) {
                public void onAction(SequenceChangeListener<T> listener) {
                    try {
                        listener.onChange(startPos, endPos, newElements, oldValue, newValue);
                    }
                    catch (RuntimeException e) {
                        ErrorHandler.triggerException(e);
                    }
                }
            });
    }

    public void bind(boolean lazy, SequenceLocation<T> otherLocation) {
        ensureBindable();
        Sequence<T> oldValue = $value;
        $value = otherLocation.get();
        boundLocation = new BoundLocationInfo(otherLocation, lazy);
        boundLocation.bind();
        notifyListeners(0, Sequences.size(oldValue) - 1, $value, oldValue, $value, true);
    }

    protected void rebind(SequenceLocation<T> otherLocation) {
        if (boundLocation != null)
            boundLocation.unbind();
        boundLocation = null;
        bind(false, otherLocation);
    }

    protected boolean isUnidirectionallyBound() {
        return super.isUnidirectionallyBound() || (boundLocation != null);
    }

    @Override
    public String toString() {
        return getAsSequenceRaw().toString();
    }

    public Iterator<T> iterator() {
        return getAsSequenceRaw().iterator();
    }

    public void replaceWithDefault() {
        replaceValue(typeInfo.emptySequence);
    }

    public void update() {
        try {
            if (isUnidirectionallyBound() && !isValid() && boundLocation == null)
                getBindingExpression().compute();
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceWithDefault();
        }
    }

    private void ensureNotBound() {
        if (isUnidirectionallyBound())
            throw new AssignToBoundException("Cannot mutate bound sequence");
        if (hasDependencies())
            Sequences.noteShared($value);

    }

    private void ensureNotBound(Sequence<? extends T> newValues) {
        if (isUnidirectionallyBound())
            throw new AssignToBoundException("Cannot mutate bound sequence");
        if (hasDependencies()) {
            Sequences.noteShared($value);
            Sequences.noteShared(newValues);
        }
    }

    public Sequence<T> set(Sequence<T> value) {
        return setAsSequence(value);
    }

    public void setDefault() {
        Sequence<T> empty = typeInfo.emptySequence;
        if (state == STATE_INITIAL) {
            $value = empty;
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
        Sequence<T> oldValue = $value;
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
        setDeferredLiteral(new DeferredInitializer() {
            public void apply() {
                setAsSequence(value);
            }
        });
        return Sequences.upcast(value);
    }

    @Override
    public T set(int position, T newValue) {
        ensureNotBound();
        SequenceMutator.set($value, mutationListener, position, newValue);
        return newValue;
    }

    @Override
    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        ensureNotBound(newValues);
        SequenceMutator.replaceSlice($value, mutationListener, startPos, endPos, newValues);
        return newValues;
    }

    @Override
    public void delete(int position) {
        ensureNotBound();
        SequenceMutator.delete($value, mutationListener, position);
    }

    @Override
    public void deleteSlice(int startPos, int endPos) {
        replaceSlice(startPos, endPos, null);
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.delete($value, mutationListener, sequencePredicate);
    }

    @Override
    public void deleteAll() {
        ensureNotBound();
        setAsSequence(typeInfo.emptySequence);
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
        SequenceMutator.insert($value, mutationListener, value);
    }

    @Override
    public void insert(Sequence<? extends T> values) {
        ensureNotBound(values);
        SequenceMutator.insert($value, mutationListener, values);
    }

    public void insertFirst(T value) {
        ensureNotBound();
        SequenceMutator.insertFirst($value, mutationListener, value);
    }

    @Override
    public void insertFirst(Sequence<? extends T> values) {
        ensureNotBound(values);
        SequenceMutator.insertFirst($value, mutationListener, values);
    }

    @Override
    public void insertBefore(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertBefore($value, mutationListener, value, position);
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertBefore($value, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        ensureNotBound(values);
        SequenceMutator.insertBefore($value, mutationListener, values, position);
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound(values);
        SequenceMutator.insertBefore($value, mutationListener, values, sequencePredicate);
    }

    @Override
    public void insertAfter(T value, int position) {
        ensureNotBound();
        SequenceMutator.insertAfter($value, mutationListener, value, position);
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        SequenceMutator.insertAfter($value, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, int position) {
        ensureNotBound(values);
        SequenceMutator.insertAfter($value, mutationListener, values, position);
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        ensureNotBound(values);
        SequenceMutator.insertAfter($value, mutationListener, values, sequencePredicate);
    }

    private class BoundLocationInfo {
        public final SequenceLocation<T> otherLocation;
        public ChangeListener changeListener;
        public SequenceChangeListener<T> sequenceChangeListener;
        public final boolean lazy;

        BoundLocationInfo(SequenceLocation<T> otherLocation, boolean lazy) {
            this.otherLocation = otherLocation;
            this.lazy = lazy;
        }

        void bind() {
            // @@@ lazy ignored -- deferring computation is more expensive than eagerly recomputing
            changeListener = new ChangeListener() {
                public boolean onChange() {
                    invalidateDependencies();
                    return true;
                }
            };
            sequenceChangeListener = new SequenceChangeListener<T>() {
                public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                    $value = newValue;
                    // JFXC-2627 - changed updateDependencies to true
                    notifyListeners(startPos, endPos, newElements, oldValue, newValue, true);
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


