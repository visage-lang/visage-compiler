package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for initializing JavaFX instances from object literals.  To initialize an object literal
 * Foo { a: 1 }, the client will do
 * Foo f = new Foo$Impl();
 * InitHelper helper = new InitHelper(f);
 * f.init$a(helper.addProvided(IntVar.make(1)));
 * helper.initialize();
 *
 * @author Brian Goetz
 */
public class InitHelper<T extends FXObject> {
    private final T object;
    private final List<Location> providedInLiteral = new ArrayList<Location>();
    private final List<Location> didDefaults = new ArrayList<Location>();

    public InitHelper(T object) {
        this.object = object;
    }

    public T getInitTarget() { return object; }

    public <V extends Location> V addDefaulted(V loc) {
        didDefaults.add(loc);
        return loc;
    }

    public <V extends Location> V addProvided(V loc) {
        providedInLiteral.add(loc);
        return loc;
    }

    public T initialize() {
        object.setDefaults$(this);
        object.userInit$();
        for (Location loc : providedInLiteral)
            loc.valueChanged();
        for (Location loc : didDefaults)
            loc.valueChanged();
        return object;
    }

    public static void assertNonNull(Location location, String name) {
        if (location != null)
            throw new IllegalStateException("Duplicate initialization for attribute: " + name);
    }

}
