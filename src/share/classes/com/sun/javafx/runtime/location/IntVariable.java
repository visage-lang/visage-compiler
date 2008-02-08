package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * IntVariable
 *
 * @author Brian Goetz
 */
public class IntVariable extends AbstractIntLocation implements IntLocation, BindableLocation<IntBindingExpression> {

    protected IntBindingExpression binding;
    protected boolean isLazy;

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

    private IntVariable() {
        super(false);
    }

    private IntVariable(int value) {
        super(true);
        $value = value;
    }

    private IntVariable(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        super(false);
        bind(binding, lazy);
        addDependencies(dependencies);
    }


    public boolean isMutable() {
        return !isBound();
    }

    public int getAsInt() {
        if (isBound() && !isValid())
            update();
        return super.getAsInt();
    }

    public int setAsInt(int value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        if (this.$value != value)
            replaceValue(value);
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
        else
            setAsInt(value);
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

    public void bind(IntBindingExpression binding, boolean lazy) {
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
