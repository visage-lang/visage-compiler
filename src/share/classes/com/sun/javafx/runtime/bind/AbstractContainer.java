package com.sun.javafx.runtime.bind;

import java.util.*;

/**
 * AbstractContainer
 *
 * @author Brian Goetz
 */
public abstract class AbstractContainer implements Container {
    protected final String name;
    protected List<Dependency> dependencies =           // @@@ OPT: can be lazily allocated
        new ArrayList<Dependency>();
    protected BitSet hasDependencies = new BitSet();    // @@@ OPT: can be lazily allocated
    protected Map<LocationKey, BindingInfo> bindings =  // @@@ OPT: can be lazily allocated
        new HashMap<LocationKey, BindingInfo>();

    public AbstractContainer(String name) {
        this.name = name;
    }

    protected abstract void doSetInt(LocationKey key, int value);
    protected abstract void doSetDouble(LocationKey key, double value);
    protected abstract void doSetReference(LocationKey key, Object value);
    protected abstract int doGetInt(LocationKey key);
    protected abstract double doGetDouble(LocationKey key);
    protected abstract Object doGetReference(LocationKey key);
    protected abstract void doUpdate(LocationKey key, BindingClosure closure);

    public boolean hasDependencies(LocationKey key) {
        return hasDependencies.get(key.getSequence());
    }

    public boolean isValid(LocationKey key) {
        BindingInfo bi = bindings.get(key);
        return (bi == null || bi.isValid);
    }

    public void addDependency(LocationKey localKey, Container remoteContainer, LocationKey remoteKey) {
        hasDependencies.set(localKey.getSequence());
        dependencies.add(new Dependency(localKey, remoteContainer, remoteKey));
    }

    public void invalidate(LocationKey key) {
        BindingInfo bi = bindings.get(key);

        if (bi != null) {
            bi.isValid = false;
            if (!bi.isLazy)
                BindingTransaction.addAffected(this, key);
        }
        if (hasDependencies(key)) {
            for (Iterator<Dependency> iter=dependencies.iterator(); iter.hasNext(); ) {
                Dependency d = iter.next();
                if (d.localKey == key) {
                    Container other = d.remoteContainer.get();
                    if (other == null)
                        iter.remove();
                    else
                        other.invalidate(d.remoteKey);
                }
            }
        }
    }

    public void setIntValue(LocationKey key, int value) {
        BindingInfo bi = bindings.get(key);
        if (bi != null) {
            if (bi.isBidirectional) {
                throw new UnsupportedOperationException();
                // @@@ Set the other without forcing recursion
            }
            else
                throw new BindingException("Already bound: (" + name + ", " + key + ")");
        }

        if (hasDependencies(key)) {
            BindingTransaction.begin();
            invalidate(key);
            // @@@ If we want to support rollback, then we can't set it here, we have to set it through the transaction
            doSetInt(key, value);
            BindingTransaction.end();
        }
        else {
            doSetInt(key, value);
        }
    }

    public void setDoubleValue(LocationKey key, double value) {
        BindingInfo bi = bindings.get(key);
        if (bi != null) {
            if (bi.isBidirectional) {
                throw new UnsupportedOperationException();
                // @@@ Set the other without forcing recursion
            }
            else
                throw new BindingException("Already bound: (" + name + ", " + key + ")");
        }

        if (hasDependencies(key)) {
            BindingTransaction.begin();
            invalidate(key);
            // @@@ If we want to support rollback, then we can't set it here, we have to set it through the transaction
            doSetDouble(key, value);
            BindingTransaction.end();
        }
        else {
            doSetDouble(key, value);
        }
    }

    public void setReferenceValue(LocationKey key, Object value) {
        BindingInfo bi = bindings.get(key);
        if (bi != null) {
            if (bi.isBidirectional) {
                throw new UnsupportedOperationException();
                // @@@ Set the other without forcing recursion
            }
            else
                throw new BindingException("Already bound: (" + name + ", " + key + ")");
        }

        if (hasDependencies(key)) {
            BindingTransaction.begin();
            invalidate(key);
            // @@@ If we want to support rollback, then we can't set it here, we have to set it through the transaction
            doSetReference(key, value);
            BindingTransaction.end();
        }
        else {
            doSetReference(key, value);
        }
    }

    public void setBidirectionalBinding(LocationKey key, Container remoteContainer, LocationKey remoteKey) {
        throw new UnsupportedOperationException();
    }

    public void setBinding(LocationKey key, boolean lazy, BindingClosure closure) {
        BindingInfo newBinding = new BindingInfo(closure);
        BindingInfo oldBinding = bindings.put(key, newBinding);
        if (oldBinding != null) {
            bindings.put(key, oldBinding);
            throw new BindingException("Already bound: (" + name + ", " + key + ")");
        }

        if (lazy) {
            newBinding.isLazy = true;
            newBinding.isValid = false;
        } else {
            recalculate(key);
        }
    }

    public void recalculate(LocationKey key) {
        BindingInfo bi = bindings.get(key);
        if (bi == null)
            throw new IllegalStateException("Cannot find binding for key " + key);
        doUpdate(key, bi.closure);
        bi.isValid = true;
    }

    public int getIntValue(LocationKey key) {
        if (!isValid(key))
            recalculate(key);
        return doGetInt(key);
    }

    public double getDoubleValue(LocationKey key) {
        if (!isValid(key))
            recalculate(key);
        return doGetDouble(key);
    }

    public Object getReferenceValue(LocationKey key) {
        if (!isValid(key))
            recalculate(key);
        return doGetReference(key);
    }

    private static class BindingInfo {
        boolean isValid = true;                         // @@@ OPT: merge these bits into a single word
        boolean isLazy = false;
        boolean isBidirectional = false;
        final BindingClosure closure;
        final Container remoteContainer;
        final LocationKey remoteKey;

        public BindingInfo(BindingClosure closure) {
            this.closure = closure;
            this.remoteContainer = null;
            this.remoteKey = null;
        }

        public BindingInfo(Container remoteContainer, LocationKey remoteKey) {
            this.closure = null;
            this.remoteContainer = remoteContainer;
            this.remoteKey = remoteKey;
        }
    }

    private static class BidirectionalBindingInfo extends BindingInfo {
        final Container remoteContainer;
        final LocationKey remoteKey;


        public BidirectionalBindingInfo(Container remoteContainer, LocationKey remoteKey) {
            super(null);
            this.remoteContainer = remoteContainer;
            this.remoteKey = remoteKey;
            this.isBidirectional = true;
        }
    }
}
