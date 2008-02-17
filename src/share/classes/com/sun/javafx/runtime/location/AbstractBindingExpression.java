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

    protected <T extends Location> T addDynamicDependent(T dep) {
        location.addDynamicDependency(dep);
        return dep;
    }

    protected <T extends Location> T addStaticDependent(T dep) {
        location.addDependencies(dep);
        return dep;
    }

    protected void clearDynamicDependencies() {
        location.clearDynamicDependencies();
    }
}
