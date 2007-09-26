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
public class InitHelper {
    private final FXObject object;
    private final List<Location> providedInLiteral = new ArrayList<Location>();
    private final List<Location> didDefaults = new ArrayList<Location>();

    public InitHelper(FXObject object) {
        this.object = object;
    }

    public <T extends Location> T addDefaulted(T loc) {
        didDefaults.add(loc);
        return loc;
    }

    public <T extends Location> T addProvided(T loc) {
        providedInLiteral.add(loc);
        return loc;
    }

    public void initialize() {
        object.setDefaults$(this);
        object.userInit$();
        for (Location loc : providedInLiteral)
            loc.valueChanged();
        for (Location loc : didDefaults)
            loc.valueChanged();
    }
}
