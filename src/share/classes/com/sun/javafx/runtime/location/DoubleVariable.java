package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * DoubleVariable
 *
 * @author Brian Goetz
 */
public class DoubleVariable
        extends AbstractVariable<Double, DoubleLocation, DoubleBindingExpression>
        implements DoubleLocation {

    public static final double DEFAULT = 0.0;

    protected double $value = DEFAULT;
    private List<DoubleChangeListener> replaceListeners;


    public static DoubleVariable make() {
        return new DoubleVariable();
    }

    public static DoubleVariable make(double value) {
        return new DoubleVariable(value);
    }

    public static DoubleVariable make(boolean lazy, DoubleBindingExpression binding, Location... dependencies) {
        return new DoubleVariable(lazy, binding, dependencies);
    }

    public static DoubleVariable make(DoubleBindingExpression binding, Location... dependencies) {
        return new DoubleVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static DoubleVariable makeBijective(ObjectVariable<Double> other) {
        DoubleVariable me = DoubleVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected DoubleVariable() { }

    protected DoubleVariable(double value) {
        this();
        $value = value;
        setValid();
    }

    protected DoubleVariable(boolean lazy, DoubleBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }


    public double getAsDouble() {
        if (isBound() && !isValid())
            update();
        return $value;
    }

    public Double get() {
        return getAsDouble();
    }

    public boolean isNull() {
        return false;
    }

    protected double replaceValue(double newValue) {
        double oldValue = $value;
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

    protected DoubleBindingExpression makeBindingExpression(final DoubleLocation otherLocation) {
        return new DoubleBindingExpression() {
            public double computeValue() {
                return otherLocation.getAsDouble();
            }
        };
    }

    public double setAsDouble(double value) {
        if (isBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
        return replaceValue(value);
    }

    public double setAsDoubleFromLiteral(final double value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsDouble(value);
            }
        };
        return value;
    }

    public void setDefault() {
        setAsDouble(DEFAULT);
    }

    public Double set(Double value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Double");
            setDefault();
        }
        else
            setAsDouble(value);
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

    public void addChangeListener(DoubleChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<DoubleChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Double> listener) {
        addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(double oldValue, double newValue, boolean notifyDependencies) {
        if (notifyDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (DoubleChangeListener listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

}
