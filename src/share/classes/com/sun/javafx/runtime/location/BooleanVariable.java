package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * BooleanVariable
 *
 * @author Brian Goetz
 */
public class BooleanVariable
        extends AbstractVariable<Boolean, BooleanLocation, BooleanBindingExpression>
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
        if (oldValue != newValue || !isInitialized() || !isEverValid()) {
            boolean notifyDependencies = isValid() || !isInitialized() || !isEverValid();
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, notifyDependencies);
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

    protected BooleanBindingExpression makeBindingExpression(final BooleanLocation otherLocation) {
        return new BooleanBindingExpression() {
            public boolean computeValue() {
                return otherLocation.getAsBoolean();
            }
        };
    }

    public boolean setAsBoolean(boolean value) {
        if (isBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
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
        try {
            if (isBound() && !isValid())
                replaceValue(binding.computeValue());
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(DEFAULT);
        }
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

    private void notifyListeners(boolean oldValue, boolean newValue, boolean notifyDependencies) {
        if (notifyDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (BooleanChangeListener listener : replaceListeners)
                try {
                    listener.onChange(oldValue, newValue);
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
        }
    }

}
