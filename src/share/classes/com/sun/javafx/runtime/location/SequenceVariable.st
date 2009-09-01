/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.lang.ref.WeakReference;
import java.util.BitSet;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.*;
import com.sun.javafx.runtime.util.Linkables;

/**
 * SequenceVariable
 *
 * @author Brian Goetz
 */
public class SequenceVariable<T>
        extends AbstractVariable<Sequence<? extends T>, SequenceLocation<? extends T>>
        implements SequenceLocation<T> {

    private final TypeInfo<T, ?> typeInfo;
    public/*protected*/ Sequence<? extends T> \$value;
    private BoundLocationInfo<T> boundLocation;


    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo) {
        return new SequenceVariable<T>(typeInfo);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, Sequence<? extends T> value) {
        return new SequenceVariable<T>(typeInfo, value);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, boolean lazy, BindingExpression binding, DependencySource... dependencies) {
        return new SequenceVariable<T>(typeInfo, lazy, binding, dependencies);
    }

    public static <T> SequenceVariable<T> make(TypeInfo<T, ?> typeInfo, BindingExpression binding, DependencySource... dependencies) {
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
        this.\$value = typeInfo.emptySequence;

    }

    protected SequenceVariable(TypeInfo<T, ?> typeInfo, Sequence<? extends T> value) {
        this(typeInfo);
        if (value == null)
            value = typeInfo.emptySequence;
        setAsSequence(value);
    }

    protected SequenceVariable(TypeInfo<T, ?> typeInfo, boolean lazy, BindingExpression binding, DependencySource... dependencies) {
        this(typeInfo);
        bind(lazy, binding);
        addDependency(dependencies);
    }

    public TypeInfo<T, ?> getElementType() {
        return typeInfo;
    }

    public Sequence<? extends T> get() {
        return getAsSequence();
    }

    public T get(int position) {
        return getAsSequence().get(position);
    }

    public Sequence<? extends T> getAsSequence() {
        ensureValid();
        return \$value;
    }

    public Sequence<? extends T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    protected BindingExpression makeBindingExpression(final SequenceLocation<? extends T> otherLocation) {
        return new AbstractBindingExpression() {
            public void compute() {
                pushValue(otherLocation.getAsSequence());
           }
        };
    }

    public SequenceLocation<T> bijectiveBind(SequenceVariable<T> other) {
        ensureBindable();
        resetState(STATE_BIDI_BOUND);
        Bindings.bijectiveBind(this, other);
        return this;
    }

    public void addSequenceChangeListener(ChangeListener listener) {
        addChild(listener);
    }

    public void removeSequenceChangeListener(ChangeListener listener) {
        removeChild(listener);
    }

    @Override
    public void addChangeListener(final ChangeListener<Sequence<? extends T>> listener) {
        addSequenceChangeListener(new WrappedChangeListener<T>(listener));
    }

    private static class WrappedChangeListener<T> extends ChangeListener<T> {
        private ChangeListener<Sequence<? extends T>> listener;

        WrappedChangeListener(ChangeListener<Sequence<? extends T>> listener) {
            this.listener = listener;
        }

        @Override
        public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            onChangeB(buffer, oldValue, startPos, endPos, newElements);
        }

        @Override
        public boolean onChangeB(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            Sequence<? extends T> newValue = buffer != null ? buffer : newElements;
            return listener.onChangeB(oldValue, newValue);
        }
    };


    public /*protected*/ void notifyListeners(ArraySequence<T> buffer,
                                   Sequence<? extends T> oldValue,
                                   int startPos, int endPos,
                                   Sequence<? extends T> newElements,
                                   boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();

        // Ugly: the logic below was cut-pasted and edited from "iterateChildren"
        // method of AbstractLocation to avoid creating an instance of iteration
        // closure class for every iteration.

        if (hasChildren(CHILD_KIND_TRIGGER)) {
            beginUpdate();
            try {
                boolean removed = false;
                int mask = 0;
                for (LocationDependency cur = children;  cur != null; ) {
                    LocationDependency next = cur.getNext();
                    int curKind = cur.getDependencyKind();
                    handle: {
                        if (curKind == CHILD_KIND_TRIGGER) {
                            boolean keep;
                            try {
                                keep = ((ChangeListener<T>)cur).onChangeB(buffer, oldValue, startPos, endPos, newElements);
                            } catch (RuntimeException e) {
                                ErrorHandler.triggerException(e);
                                keep = true;
                            }

                            if (! keep) {
                                Linkables.remove(cur);
                                removed = true;
                                break handle;
                            }
                        }
                        mask |= curKind;
                    }
                    cur = next;
                }
                if (removed)
                    setChildKindMask((byte) mask);
            } finally {
                endUpdate();
            }
        }
    }

    @Override
    public SequenceVariable<T> bind(boolean lazy, SequenceLocation<? extends T> otherLocation) {
        ensureBindable();
        boundLocation = new BoundLocationInfo<T>(this, lazy, otherLocation);
        boundLocation.bind();
        if (lazy)
            invalidate();
        else {
            Sequence<? extends T> oldValue = \$value;
            \$value = otherLocation.get();
            setValid();
            notifyListeners(null, oldValue, 0, Sequences.size(oldValue), \$value, true);
        }
        return this;
    }

    protected void rebind(boolean lazy, SequenceLocation<T> otherLocation) {
        if (boundLocation != null)
            boundLocation.unbind();
        boundLocation = null;
        bind(lazy, otherLocation);
    }

    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    public void replaceWithDefault() {
        replaceValue(typeInfo.emptySequence);
    }

    @Override
    public void update() {
        try {
            if (isUnidirectionallyBound() && !isValid()) {
                if (boundLocation == null)
                    getBindingExpression().compute();
                else if (isLazilyBound()) // always valid if bound non-lazily to another location
                    boundLocation.update();
            }
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized() && !isLazilyBound())
                replaceWithDefault();
        }
    }

    protected void ensureNotBound() {
        if (isUnidirectionallyBound())
            throw new AssignToBoundException("Cannot mutate bound sequence");
    }

    public Sequence<? extends T> set(Sequence<? extends T> value) {
        return setAsSequence(value);
    }

    public void setDefault() {
        Sequence<T> empty = typeInfo.emptySequence;
        if (state == STATE_INITIAL) {
            \$value = empty;
            state = STATE_UNBOUND_DEFAULT;
            // @@@ Uncomment this to make sequence trigger behavior consistent with others (see JFXC-885)
            // notifyListeners(0, -1, \$value, \$value, \$value, true);
        }
        else
            setAsSequence(empty);
    }

    public Sequence<? extends T> setAsSequence(Sequence<? extends T> newValue) {
        ensureNotBound();
        // @@@ To make sequence triggers consistent with others (more JFXC-885), use replaceValue() instead
        if (false)
            return replaceValue(newValue);
        // @@@ The following aims to be bug-compatible ...
        Sequence<? extends T> oldValue = \$value;
        if (newValue == null)
            newValue = typeInfo.emptySequence;
        else
            newValue.incrementSharing();
        oldValue.decrementSharing();
        if (!hasDependencies()) {
            preReplace(true);
            \$value = newValue;
            setValid();
        }
        else if (!Sequences.isEqual(oldValue, newValue) || state == STATE_UNBOUND && !isValid()) {
            preReplace(true);
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            \$value = newValue;
            setValid();
            notifyListeners(null, oldValue, 0, Sequences.size(oldValue), newValue, invalidateDependencies);
        }
        else {
            preReplace(true);
            setValid();
        }
        return newValue;
    }

    public Sequence<? extends T> replaceValue (Sequence<? extends T> newValue) {
        Sequence<? extends T> oldValue = \$value;
        if (newValue == null)
            newValue = typeInfo.emptySequence;
        else
            newValue.incrementSharing();
        oldValue.decrementSharing();
        if (!hasDependencies()) {
            preReplace(true);
            \$value = newValue;
            setValid();
        }
        else if (preReplace (! Sequences.isEqual(oldValue, newValue))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            \$value = newValue;
            setValid();
            notifyListeners(null, oldValue, 0, Sequences.size(oldValue), newValue, invalidateDependencies);
        }
        else
            setValid();
        return newValue;
    }

    //@Override
    public T set(int position, T newValue) {
        replaceSlice(position, position+1, newValue);
        return newValue;
    }
    
    public void replaceSlice(int startPos, int endPos/*exclusive*/, T newValue) {
        if (newValue == null) {
            deleteSlice(startPos, endPos);
            return;
        }
        int oldSize = \$value.size();
        if (startPos < 0)
            startPos = 0;
        else if (startPos > oldSize)
            startPos = oldSize;
        if (endPos > oldSize)
            endPos = oldSize;
        else if (endPos < startPos)
            endPos = startPos;
        ensureNotBound();
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, \$value);
        if (!hasDependencies()) {
            preReplace(true);
            arr.replace(startPos, endPos, newValue, false);
            \$value = arr;
            setValid();
        }
        else if (preReplace(endPos != startPos + 1 || !newValue.equals(arr.get(startPos)))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            arr.replace(startPos, endPos, newValue, true);
            \$value = arr;
            setValid();
            notifyListeners(arr, null, startPos, endPos, null, invalidateDependencies);
            arr.clearOldValues(endPos-startPos);
        }
        else
            setValid();
    }

    //@Override
    public Sequence<? extends T> replaceSlice(int startPos, int endPos/*exclusive*/, Sequence<? extends T> newValues) {
        if (newValues == null)
            newValues = typeInfo.emptySequence;
        Sequence<? extends T> oldValue = \$value;
        int oldSize = oldValue.size();
        if (startPos < 0)
            startPos = 0;
        else if (startPos > oldSize)
            startPos = oldSize;
        if (endPos > oldSize)
            endPos = oldSize;
        else if (endPos < startPos)
            endPos = startPos;
        if (startPos==0 && endPos == oldSize) {
            setAsSequence(newValues);
            return newValues;
        }
        ensureNotBound();
        ArraySequence<T> arr;
        if (! (oldValue instanceof ArraySequence<?>)
            || (arr = (ArraySequence<T>) oldValue).isShared()) {
            arr = typeInfo.emptySequence.makeNew(0);
            arr.incrementSharing();
            arr.add(oldValue);
        }
        int newSize = newValues.size();
        if (!hasDependencies()) {
            preReplace(true);
            arr.replace(startPos, endPos, newValues, 0, newSize, false);
            \$value = arr;
            setValid();
        }
        else if (preReplace(!Sequences.sliceEqual(\$value, startPos, endPos, newValues))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            arr.replace(startPos, endPos, newValues, 0, newSize, true);
            \$value = arr;
            setValid();
            notifyListeners(arr, null, startPos, endPos, newValues, invalidateDependencies);
            arr.clearOldValues(endPos-startPos);
        }
        else
            setValid();
        return newValues;
    }

    //@Override
    public void delete(int position) {
        replaceSlice(position, position+1, (Sequence<? extends T>) null);
    }

    //@Override
    public void deleteSlice(int startPos, int endPos/* exclusive */) {
        replaceSlice(startPos, endPos, (Sequence<? extends T>) null);
    }

    //@Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        ensureNotBound();
        Sequence<? extends T> oldValue = \$value;
        BitSet bits = oldValue.getBits(sequencePredicate);
        if (bits.cardinality() == 0)
            return;
        int endPos = oldValue.size();
        while (endPos > 0) {
            if (! bits.get(endPos-1))
                endPos--;
            else {
                int startPos = endPos-1;
                while (startPos > 0 && bits.get(startPos-1))
                    startPos--;
                deleteSlice(startPos, endPos);
                endPos = startPos-1;
            }
        }

        /* Likely to be more efficient, but deletes segments by increasing order.
        int previouslyRemoved = 0;
        int startPos = 0;
        for (;;) {
            startPos = bits.nextSetBit(startPos);
            if (startPos < 0)
                break;
            int endPos = bits.nextClearBit(startPos+1);
            deleteSlice(startPos - previouslyRemoved, endPos - previouslyRemoved);
            previouslyRemoved += endPos - startPos;
            startPos = endPos + 1;
        }
        */
    }

    //@Override
    public void deleteAll() {
        ensureNotBound();
        setAsSequence(typeInfo.emptySequence);
    }

    //@Override
    public void deleteValue(final T targetValue) {
        delete(new SequencePredicate<T>() {
            public boolean matches(Sequence<? extends T> sequence, int index, T value) {
                if (value == null)
                    return targetValue == null;
                return value.equals(targetValue);
            }
        });
    }

    //@Override
    public void insert(T value) {
        int sz = \$value.size();
        replaceSlice(sz, sz, value);
    }

    //@Override
    public void insert(Sequence<? extends T> values) {
        int sz = \$value.size();
        replaceSlice(sz, sz, values);
    }

    //@Override
    public void insertBefore(T value, int position) {
        replaceSlice(position, position, value);
    }

    //@Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        replaceSlice(position, position, values);
    }

$PREFIX:{T|
    public static $PRIM_MAP.(T)$ set(SequenceLocation<$BOXED_MAP.(T)$> var, int position, $PRIM_MAP.(T)$ newValue) {
        replaceSlice(var, position, position+1, newValue);
        return newValue;
    }

    public static void insert(SequenceLocation<$BOXED_MAP.(T)$> var, $PRIM_MAP.(T)$ value) {
        int sz = var.getAsSequence().size();
        replaceSlice(var, sz, sz, value);
    }

    public static void insertBefore(SequenceLocation<$BOXED_MAP.(T)$> var, $PRIM_MAP.(T)$ value, int position) {
        replaceSlice(var, position, position, value);
    }

    public static void replaceSlice(SequenceLocation<$BOXED_MAP.(T)$> loc, int startPos, int endPos, $PRIM_MAP.(T)$ newValue) {
        if (! (loc instanceof SequenceVariable<?>))
             throw new UnsupportedOperationException();
        SequenceVariable<$BOXED_MAP.(T)$> var =
            (SequenceVariable<$BOXED_MAP.(T)$>) loc;
        Sequence<? extends $BOXED_MAP.(T)$> oldValue = var.\$value;
        int oldSize = oldValue.size();
        if (startPos < 0)
            startPos = 0;
        else if (startPos > oldSize)
            startPos = oldSize;
        if (endPos > oldSize)
            endPos = oldSize;
        else if (endPos < startPos)
            endPos = startPos;
        var.ensureNotBound();
        $T$ArraySequence arr;
        if (! (oldValue instanceof $T$ArraySequence)
            || (arr = ($T$ArraySequence) oldValue).isShared()) {
            arr = new $T$ArraySequence(oldValue);
            arr.incrementSharing();
        }
        if (!var.hasDependencies()) {
            var.preReplace(true);
            arr.replace(startPos, endPos, newValue, false);
            var.\$value = arr;
            var.setValid();
        }
        else if (var.preReplace(endPos != startPos + 1 || newValue != arr.getAs$T$(startPos))) {
            boolean invalidateDependencies = var.isValid() || var.state == STATE_UNBOUND;
            arr.replace(startPos, endPos, newValue, true);
            var.\$value = arr;
            var.setValid();
            var.notifyListeners(arr, null, startPos, endPos, null, invalidateDependencies);
        }
        else
            var.setValid();
    }


}$
    private static class BoundLocationInfo<T> extends WeakReference<SequenceVariable<T>> {
        public final SequenceLocation<T> otherLocation;
        public InvalidationListener invalidationListener;
        public ChangeListener<T> sequenceChangeListener;
        public final boolean lazy;

        BoundLocationInfo(SequenceVariable me, boolean lazy, SequenceLocation<? extends T> otherLocation) {
            super(me);
            this.otherLocation = (SequenceLocation<T>) otherLocation; // KLUDGE
            this.lazy = lazy;
        }

        void update() {
            SequenceVariable<T> seq = get();
            if (seq != null) {
                Sequence<? extends T> oldValue = seq.\$value;
                seq.\$value = otherLocation.getAsSequence();
                seq.setValid();
                if (seq.hasDependencies() && !Sequences.isEqual(oldValue, seq.\$value))
                    seq.notifyListeners(null, oldValue, 0, oldValue.size(), seq.\$value, true);
            }
        }

        void bind() {
            if (lazy) {
                invalidationListener = new AbstractInvalidationListener() {
                    public boolean onChange() {
                        SequenceVariable<T> seq = get();
                        if (seq != null) {
                            seq.invalidate();
                            return true;
                        } else {
                            return false;
                        }
                    }
                 };
                 otherLocation.addInvalidationListener(invalidationListener);
            } else {
                sequenceChangeListener = new ChangeListener<T>() {
                    @Override
                    public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
                        onChangeB(buffer, oldValue, startPos, endPos, newElements);
                    }
                    
                    @Override
                    public boolean onChangeB(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
                        SequenceVariable<T> seq = get();
                        if (seq != null) {
                            // Don't need to check lazy, since we're never called if binding is lazy
                            seq.\$value = buffer != null ? buffer : newElements;
                            seq.setValid();
                            // JFXC-2627 - changed updateDependencies to true
                            seq.notifyListeners(buffer, oldValue, startPos, endPos, newElements, true);
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
                otherLocation.addSequenceChangeListener(sequenceChangeListener);
            }
            SequenceVariable<T> seq = get();
            if (seq != null)
                seq.state = lazy ? STATE_UNI_BOUND_LAZY : STATE_UNI_BOUND;
        }

        void unbind() {
            if (invalidationListener != null)
                otherLocation.removeInvalidationListener(invalidationListener);
            if (sequenceChangeListener != null)
                otherLocation.removeSequenceChangeListener(sequenceChangeListener);
            invalidationListener = null;
            sequenceChangeListener = null;
            SequenceVariable<T> seq = get();
            if (seq != null)
               seq.state = STATE_UNBOUND;
        }
    }
}
