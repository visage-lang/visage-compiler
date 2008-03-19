package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * IntVariable
 *
 * @author Brian Goetz
 */
public class IntVariable extends AbstractVariable<Integer, IntBindingExpression> implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;
    private List<IntChangeListener> replaceListeners;

    public static IntVariable make() {
        return new IntVariable();
    }

    public static IntVariable make(int value) {
        return new IntVariable(value);
    }

    public static IntVariable make(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        return new IntVariable(lazy, binding, dependencies);
    }

    public static IntVariable make(IntBindingExpression binding, Location... dependencies) {
        return new IntVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static IntVariable makeBijective(ObjectVariable<Integer> other) {
        IntVariable me = IntVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected IntVariable() { }

    protected IntVariable(int value) {
        this();
        $value = value;
        setValid();
    }

    protected IntVariable(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    public int getAsInt() {
        if (isBound() && !isValid())
            update();
        return $value;
    }

    public Integer get() {
        return getAsInt();
    }

    public boolean isNull() {
        return false;
    }

    protected int replaceValue(int newValue) {
        int oldValue = $value;
        if (oldValue != newValue || !isInitialized()) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public void bind(final IntLocation otherLocation) {
        bind(false, new IntBindingExpression() {
            public int computeValue() {
                return otherLocation.getAsInt();
            }
        }, otherLocation);
    }

    public void bindFromLiteral(final IntLocation otherLocation) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(otherLocation);
            }
        };
    }

    public int setAsInt(int value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        return replaceValue(value);
    }

    public int setAsIntFromLiteral(final int value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsInt(value);
            }
        };
        return value;
    }

    public void setDefault() {
        setAsInt(DEFAULT);
    }

    public Integer set(Integer value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Integer");
            setDefault();
        }
        else {
            if (isBound())
                throw new BindingException("Cannot assign to bound variable");
            replaceValue(value);
        }
        return value;
    }

    @Override
    public void update() {
        if (isBound() && !isValid())
            replaceValue(binding.computeValue());
    }

    public void addChangeListener(IntChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<IntChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Integer> listener) {
        addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(final int oldValue, final int newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (IntChangeListener listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

}
