package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;

/**
 * ObjectVariable
 *
 * @author Brian Goetz
 */
public class ObjectVariable<T> extends AbstractObjectLocation<T>
        implements ObjectLocation<T>, BindableLocation<ObjectBindingExpression<T>> {

    protected ObjectBindingExpression<T> binding;
    protected boolean isLazy;

    public static ObjectVariable make() {
        return new ObjectVariable();
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

    private ObjectVariable() {
        super(false);
    }

    private ObjectVariable(T value) {
        super(true);
        $value = value;
    }

    private ObjectVariable(boolean lazy, ObjectBindingExpression<T> binding, Location... dependencies) {
        super(false);
        bind(binding, lazy);
        addDependencies(dependencies);
    }


    public boolean isMutable() {
        return !isBound();
    }

    public T get() {
        if (isBound() && !isValid())
            update();
        return super.get();
    }

    public T set(T value) {
        if (isBound())
            throw new BindingException("Cannot assign to bound variable");
        if (this.$value != value)
            replaceValue(value);
        return value;
    }

    public void setDefault() {
        set(null);
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

    public void bind(ObjectBindingExpression<T> binding, boolean lazy) {
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
