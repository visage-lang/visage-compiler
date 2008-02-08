package com.sun.javafx.runtime.location;

/**
 * AbstractBindingExpression
 *
 * @author Brian Goetz
 */
public class AbstractBindingExpression {
    private Location location;

    public void setLocation(Location location) {
        if (this.location != null)
            throw new IllegalStateException("Cannot reuse binding expressions");
        this.location = location;
    }

    protected void addDynamicDependent(Location dep) {
        location.addDynamicDependency(dep);
    }

    protected void addStaticDependent(Location dep) {
        location.addDependencies(dep);
    }

    protected void clearDynamicDependencies() {
        location.clearDynamicDependencies();
    }
}
