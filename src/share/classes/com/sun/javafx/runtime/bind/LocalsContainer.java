package com.sun.javafx.runtime.bind;

/**
 * LocalsContainer
 *
 * @author Brian Goetz
 */
public class LocalsContainer extends AbstractContainer implements Container {
    private final LocalsStorage storage;

    public LocalsContainer(String name, int numPrimitives, int numReferences) {
        super(name);
        storage = new LocalsStorage(numPrimitives, numReferences);
    }

    @Override
    protected void doSetInt(LocationKey key, int value) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        key.setInt(storage, value);
    }

    @Override
    protected void doSetDouble(LocationKey key, double value) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        key.setDouble(storage, value);
    }

    @Override
    protected void doSetReference(LocationKey key, Object value) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        key.setReference(storage, value);
    }

    @Override
    protected int doGetInt(LocationKey key) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        return key.getInt(storage);
    }

    @Override
    protected double doGetDouble(LocationKey key) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        return key.getDouble(storage);
    }

    @Override
    protected Object doGetReference(LocationKey key) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        return key.getReference(storage);
    }

    @Override
    protected void doUpdate(LocationKey key, BindingClosure closure) {
        assert key instanceof LocalsLocationKey : "Key must be a LocalsLocationKey";
        key.update(storage, closure);
    }
}
