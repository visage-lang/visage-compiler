package com.sun.javafx.runtime.location;

import java.util.List;
import java.util.ArrayList;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * DoubleVariable
 *
 * @author Brian Goetz
 */
public class DoubleVariable
        extends AbstractVariable<Double, DoubleBindingExpression>
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
        if (oldValue != newValue || !isInitialized()) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public void bind(final DoubleLocation otherLocation) {
        bind(false, new DoubleBindingExpression() {
            public double computeValue() {
                return otherLocation.get();
            }
        }, otherLocation);
    }

    public void bindFromLiteral(final DoubleLocation otherLocation) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(otherLocation);
            }
        };
    }

    public double setAsDouble(double value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
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

    protected void notifyListeners(final double oldValue, final double newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (DoubleChangeListener listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

}
