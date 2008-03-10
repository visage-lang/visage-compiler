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
        Location[] fixedDependents = getStaticDependents();
        if (fixedDependents != null) {
            location.addDependencies(fixedDependents);
        }
    }

    /**
     * Override to provide an array of static dependents
     * @return an array of static dependents, or null
     */
    protected Location[] getStaticDependents() {
        return null;
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
