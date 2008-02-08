package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * DoubleVariable
 *
 * @author Brian Goetz
 */
public class DoubleVariable extends AbstractDoubleLocation implements DoubleLocation, BindableLocation<DoubleBindingExpression> {

    protected DoubleBindingExpression binding;
    protected boolean isLazy;

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

    private DoubleVariable() {
        super(false);
    }

    private DoubleVariable(double value) {
        super(true);
        $value = value;
    }

    private DoubleVariable(boolean lazy, DoubleBindingExpression binding, Location... dependencies) {
        super(false);
        bind(binding, lazy);
        addDependencies(dependencies);
    }


    public boolean isMutable() {
        return !isBound();
    }

    public double getAsDouble() {
        if (isBound() && !isValid())
            update();
        return super.getAsDouble();
    }

    public double setAsDouble(double value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        if (this.$value != value)
            replaceValue(value);
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
        if (isBound() && !isValid())
            replaceValue(binding.computeValue());
    }

    @Override
    public void invalidate() {
        if (isBound()) {
            super.invalidate();
            if (!isLazy())
                update();
        }
        else
            throw new BindingException("Cannot invalidate non-bound variable");
    }

    public void bind(DoubleBindingExpression binding, boolean lazy) {
        if (isBound())
            throw new BindingException("Cannot rebind variable");
        else if (isInitialized())
            throw new BindingException("Cannot bind variable that already has a value");
        this.binding = binding;
        binding.setLocation(this);
        isLazy = lazy;
    }

    public boolean isBound() {
        return binding != null;
    }

    public boolean isLazy() {
        return isLazy;
    }
}
