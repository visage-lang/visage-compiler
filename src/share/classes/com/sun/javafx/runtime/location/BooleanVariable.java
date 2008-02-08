package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * BooleanVariable
 *
 * @author Brian Goetz
 */
public class BooleanVariable extends AbstractBooleanLocation implements BooleanLocation, BindableLocation<BooleanBindingExpression> {

    protected BooleanBindingExpression binding;
    protected boolean isLazy;

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

    private BooleanVariable() {
        super(false);
    }

    private BooleanVariable(boolean value) {
        super(true);
        $value = value;
    }

    private BooleanVariable(boolean lazy, BooleanBindingExpression binding, Location... dependencies) {
        super(false);
        bind(binding, lazy);
        addDependencies(dependencies);
    }


    public boolean isMutable() {
        return !isBound();
    }

    public boolean getAsBoolean() {
        if (isBound() && !isValid())
            update();
        return super.getAsBoolean();
    }

    public boolean setAsBoolean(boolean value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        if (this.$value != value)
            replaceValue(value);
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

    public void bind(BooleanBindingExpression binding, boolean lazy) {
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
