package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * BooleanVariable
 *
 * @author Brian Goetz
 */
public class BooleanVariable
        extends AbstractVariable<Boolean, BooleanBindingExpression>
        implements BooleanLocation {

    public static final boolean DEFAULT = false;
    protected boolean $value = DEFAULT;
    private List<BooleanChangeListener> replaceListeners;

    public static BooleanVariable make() {
        return new BooleanVariable();
    }

    public static BooleanVariable make(boolean value) {
        return new BooleanVariable(value);
    }

    public static BooleanVariable make(boolean lazy, BooleanBindingExpression binding, Location... dependencies) {
        return new BooleanVariable(lazy, binding, dependencies);
    }

    public static BooleanVariable make(BooleanBindingExpression binding, Location... dependencies) {
        return new BooleanVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static BooleanVariable makeBijective(ObjectVariable<Boolean> other) {
        BooleanVariable me = BooleanVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected BooleanVariable() { }

    protected BooleanVariable(boolean value) {
        this();
        $value = value;
        setValid();
    }

    protected BooleanVariable(boolean lazy, BooleanBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    protected boolean replaceValue(boolean newValue) {
        boolean oldValue = $value;
        if (oldValue != newValue || !isInitialized()) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public boolean getAsBoolean() {
        if (isBound() && !isValid())
            update();
        return $value;
    }

    public boolean setAsBoolean(boolean value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        return replaceValue(value);
    }

    public boolean setAsBooleanFromLiteral(final boolean value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsBoolean(value);
            }
        };
        return value;
    }

    public void setDefault() {
        setAsBoolean(DEFAULT);
    }

    public Boolean set(Boolean value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Boolean");
            setDefault();
        }
        else
            setAsBoolean(value);
        return value;
    }

    @Override
    public void update() {
        if (isBound() && !isValid())
            replaceValue(binding.computeValue());
    }

    public Boolean get() {
        return getAsBoolean();
    }

    public boolean isNull() {
        return false;
    }

    public void addChangeListener(BooleanChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<BooleanChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Boolean> listener) {
        addChangeListener(new BooleanChangeListener() {
            public void onChange(boolean oldValue, boolean newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(final boolean oldValue, final boolean newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (BooleanChangeListener listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

}
