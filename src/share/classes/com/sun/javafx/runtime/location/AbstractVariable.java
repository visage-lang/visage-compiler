package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;

/**
 * AbstractBindableLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractVariable<T_VALUE, T_LOCATION extends ObjectLocation<T_VALUE>, T_BINDING extends AbstractBindingExpression>
        extends AbstractLocation
        implements ObjectLocation<T_VALUE>, BindableLocation<T_VALUE, T_BINDING> {

    protected T_BINDING binding;
    protected boolean isLazy, everInitialized, everValid;
    protected DeferredInitializer deferredLiteral;

    protected AbstractVariable() { }

    protected void setInitialized() {
        everInitialized = true;
    }

    public boolean isInitialized() {
        return everInitialized;
    }

    protected boolean isEverValid() {
        return everValid;
    }

    protected void setValid() {
        super.setValid();
        everValid = true;
        setInitialized();
    }

    protected void ensureBindable() {
        if (isBound())
            throw new BindingException("Cannot rebind variable");
        //TODO: commented-out as a temporary work-around to JFXC-979
        //else if (isInitialized())
        //    throw new BindingException("Cannot bind variable that already has a value");
    }

    public void bijectiveBind(ObjectLocation<T_VALUE> other) {
        ensureBindable();
        super.invalidate(); //TODO: this is a work-around for JFXC-979
        setInitialized();
        Bindings.bijectiveBind(this, other);
    }

    public void bijectiveBindFromLiteral(final ObjectLocation<T_VALUE> other) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bijectiveBind(other);
            }
        };
    }

    protected abstract T_BINDING makeBindingExpression(T_LOCATION location);

    public void bind(T_LOCATION otherLocation) {
        bind(false, makeBindingExpression(otherLocation), otherLocation);
    }

    public void bindFromLiteral(final T_LOCATION otherLocation) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(otherLocation);
            }
        };
    }

    public void bind(boolean lazy, T_BINDING binding, Location... dependencies) {
        ensureBindable();
        super.invalidate(); //TODO: this is a work-around for JFXC-979
        setInitialized();
        this.binding = binding;
        binding.setLocation(this);
        isLazy = lazy;
        addDependencies(dependencies);
        if (!isLazy)
            update();
    }

    public void bindFromLiteral(final boolean lazy, final T_BINDING binding, final Location... dependencies) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(lazy, binding, dependencies);
            }
        };
    }

    public T_VALUE setFromLiteral(final T_VALUE value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                set(value);
            }
        };
        return value;
    }

    public boolean isBound() {
        return binding != null;
    }

    public boolean isLazy() {
        return isBound() && isLazy;
    }

    /** Returns true if this instance needs a default value.  Warning: this method has side effects; when called,
     * it will try and apply any deferred values from the object literal, if there is one.  */
    public boolean needDefault() {
        if (deferredLiteral != null) {
            deferredLiteral.apply();
            deferredLiteral = null;
            return false;
        }
        else
            return !isInitialized();
    }

    public void initialize() {
        // This is where we used to do fireInitialTriggers when we were deferring triggers
        assert(deferredLiteral == null);
        deferredLiteral = null;
        if (isBound() && !isLazy())
            update();
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

    public boolean isMutable() {
        return !isBound();
    }
}

interface DeferredInitializer {
    public void apply();
}
