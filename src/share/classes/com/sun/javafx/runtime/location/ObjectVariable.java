package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.AssignToBoundException;

/**
 * ObjectVariable
 *
 * @author Brian Goetz
 */
public class ObjectVariable<T>
        extends AbstractVariable<T, ObjectLocation<T>, ObjectBindingExpression<T>>
        implements ObjectLocation<T> {

    protected T $value;
    private List<ObjectChangeListener<T>> replaceListeners;

    public static<T> ObjectVariable<T> make() {
        return new ObjectVariable<T>();
    }

    public static<T> ObjectVariable<T> make(T value) {
        return new ObjectVariable<T>(value);
    }

    public static<T> ObjectVariable<T> make(boolean lazy, ObjectBindingExpression<T> binding, Location... dependencies) {
        return new ObjectVariable<T>(lazy, binding, dependencies);
    }

    public static<T> ObjectVariable<T> make(ObjectBindingExpression<T> binding, Location... dependencies) {
        return new ObjectVariable<T>(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static<T> ObjectVariable<T> makeBijective(ObjectLocation<T> other) {
        ObjectVariable<T> me = ObjectVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected ObjectVariable() { }

    protected ObjectVariable(T value) {
        this();
        $value = value;
        setValid();
    }

    protected ObjectVariable(boolean lazy, ObjectBindingExpression<T> binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    protected ObjectBindingExpression<T> makeBindingExpression(final ObjectLocation<T> otherLocation) {
        return new ObjectBindingExpression<T>() {
            public T computeValue() {
                return otherLocation.get();
            }
        };
    }

    public T get() {
        if (isBound() && !isValid())
            update();
        return $value;
    }

    protected T replaceValue(T newValue) {
        T oldValue = $value;
        if (!Util.isEqual(oldValue, newValue) || !isInitialized()) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public T set(T value) {
        if (isBound() && !Util.isEqual($value, value))
            throw new AssignToBoundException("Cannot assign to bound variable");
        return replaceValue(value);
    }

    public void setDefault() {
        set(null);
    }

    @Override
    public void update() {
        try {
            if (isBound() && !isValid())
                replaceValue(binding.computeValue());
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(null);
        }
    }

    public boolean isNull() {
        return $value == null;
    }

    public void addChangeListener(ObjectChangeListener<T> listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<ObjectChangeListener<T>>();
        replaceListeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    protected void notifyListeners(final T oldValue, final T newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (ObjectChangeListener<T> listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

}
