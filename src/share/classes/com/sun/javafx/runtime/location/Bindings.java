package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.CircularBindingException;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Bindings -- helper class for setting up bijective bindings.
 *
 * Bijective bindings between locations A and B are implemented by attaching a (strong) listener to each A and
 * B, and updating the other when one is updated. The listeners share state, so they do not pass the updated
 * on to the other if the other is already in the right state, avoiding an infinite mutual ping-ponging of
 * updates. The listeners share weak references back to A and B, so that when one of A or B is garbage
 * collected, the next time the other is updated, the listeners observe that and return false from the
 * onChange() method to detach the listener.
 *
 * Implementation note: to avoid a combinatorial explosion, the bidirectional binding machinery is defined in terms
 * of a pair of object locations.  For primitive-valued locations (e.g., IntLocation), the factory methods wrap
 * them with a wrapper that implements ObjectLocation<PrimitiveWrapperClass>.
 *
 * @author Brian Goetz
 */
public class Bindings {

    /** Creation a bijective binding between objects of type T and U */
    public static <T, U> void bijectiveBind(ObjectLocation<T> a, ObjectLocation<U> b, Bijection<T, U> mapper) {
        new BijectiveBinding<T, U>(a, b, mapper);
    }

    /** Convenience method for bijection between IntLocation and IntLocation */
    public static void bijectiveBind(IntLocation a, IntLocation b, Bijection<Integer, Integer> mapper) {
        new BijectiveBinding<Integer, Integer>(a.asIntegerLocation(), b.asIntegerLocation(), mapper);
    }

    /** Convenience method for bijection between IntLocation and ObjectLocation */
    public static<T> void bijectiveBind(IntLocation a, ObjectLocation<T> b, Bijection<Integer, T> mapper) {
        new BijectiveBinding<Integer, T>(a.asIntegerLocation(), b, mapper);
    }

    /** Convenience method for bijection between IntLocation and ObjectLocation */
    public static<T> void bijectiveBind(ObjectLocation<T> a, IntLocation b, Bijection<T, Integer> mapper) {
        new BijectiveBinding<T, Integer>(a, b.asIntegerLocation(), mapper);
    }

    /** Convenience method for bijection between DoubleLocation and DoubleLocation */
    public static void bijectiveBind(DoubleLocation a, DoubleLocation b, Bijection<Double, Double> mapper) {
        new BijectiveBinding<Double, Double>(a.asDoubleLocation(), b.asDoubleLocation(), mapper);
    }

    /** Convenience method for bijection between DoubleLocation and ObjectLocation */
    public static<T> void bijectiveBind(DoubleLocation a, ObjectLocation<T> b, Bijection<Double, T> mapper) {
        new BijectiveBinding<Double, T>(a.asDoubleLocation(), b, mapper);
    }

    /** Convenience method for bijection between DoubleLocation and ObjectLocation */
    public static<T> void bijectiveBind(ObjectLocation<T> a, DoubleLocation b, Bijection<T, Double> mapper) {
        new BijectiveBinding<T, Double>(a, b.asDoubleLocation(), mapper);
    }

    /** Return the set of locations that are "peered" with this one through a chain of bidirectional bindings */
    public static Collection<Location> getPeerLocations(Location location) {
        Collection<Location> newLocs = BijectiveBinding.getDirectPeers(location);
        if (newLocs.size() == 0)
            return newLocs;

        Set<Location> knownLocs = new HashSet<Location>();
        LinkedList<Location> toExplore = new LinkedList<Location>(newLocs);
        while (toExplore.size() > 0) {
            Location loc = toExplore.removeFirst();
            while (loc instanceof ViewLocation)
                loc = ((ViewLocation) loc).getUnderlyingLocation();
            if (!knownLocs.contains(loc) && loc != location) {
                knownLocs.add(loc);
                toExplore.addAll(BijectiveBinding.getDirectPeers(loc));
            }
        }
        return knownLocs;
    }

    public static boolean isPeerLocation(Location a, Location b) {
        Collection<Location> aPeers = getPeerLocations(a);
        while (b instanceof ViewLocation)
            b = ((ViewLocation) b).getUnderlyingLocation();
        return (aPeers != null && aPeers.contains(b));
    }

    private static class BijectiveBinding<T, U> {
        private final WeakReference<ObjectLocation<T>> aRef;
        private final WeakReference<ObjectLocation<U>> bRef;
        private final Bijection<T, U> mapper;
        private T lastA;
        private U lastB;

        public BijectiveBinding(ObjectLocation<T> a, ObjectLocation<U> b, Bijection<T, U> mapper) {
            if (!(a instanceof MutableLocation) || !(b instanceof MutableLocation))
                throw new IllegalArgumentException("Both components of bijective bind must be mutable");
            if (isPeerLocation(a, b))
                throw new CircularBindingException("Binding circularity detected");
            
            this.aRef = new WeakReference<ObjectLocation<T>>(a);
            this.bRef = new WeakReference<ObjectLocation<U>>(b);
            this.mapper = mapper;

            // Set A before setting up the listeners
            a.set(mapper.mapBackwards(b.get()));

            a.addChangeListener(new BijectiveChangeListener() {
                public boolean onChange() {
                    ObjectLocation<T> a = aRef.get();
                    ObjectLocation<U> b = bRef.get();
                    if (a == null || b == null)
                        return false;
                    T newA = a.get();
                    if ((newA == null && lastA == null) || (newA != null && newA.equals(lastA)))
                        return true;
                    U newB = BijectiveBinding.this.mapper.mapForwards(newA);
                    lastA = newA;
                    lastB = newB;
                    b.set(newB);
                    return true;
                }

                public BijectiveBinding getBijectiveBinding() {
                    return BijectiveBinding.this;
                }
            });
            b.addChangeListener(new BijectiveChangeListener() {
                public boolean onChange() {
                    ObjectLocation<T> a = aRef.get();
                    ObjectLocation<U> b = bRef.get();
                    if (a == null || b == null)
                        return false;
                    U newB = b.get();
                    if ((newB == null && lastB == null) || (newB != null && newB.equals(lastB)))
                        return true;
                    T newA = BijectiveBinding.this.mapper.mapBackwards(newB);
                    lastA = newA;
                    lastB = newB;
                    a.set(newA);
                    return true;
                }

                public BijectiveBinding getBijectiveBinding() {
                    return BijectiveBinding.this;
                }
            });
        }

        public static Collection<Location> getDirectPeers(Location loc) {
            Set<Location> set = null;
            for (ChangeListener cl : loc.getListeners()) {
                if (cl instanceof BijectiveChangeListener) {
                    BijectiveBinding<?, ?> bb = ((BijectiveChangeListener) cl).getBijectiveBinding();
                    ObjectLocation<?> a = (ObjectLocation) bb.aRef.get();
                    ObjectLocation<?> b = (ObjectLocation) bb.bRef.get();
                    if (a != null && a != loc) {
                        if (set == null)
                            set = new HashSet<Location>();
                        set.add(a);
                    }
                    if (b != null && b != loc) {
                        if (set == null)
                            set = new HashSet<Location>();
                        set.add(b);
                    }
                }
            }
            if (set != null)
                return set;
            else
                return Collections.emptySet();
        }

        private interface BijectiveChangeListener extends ChangeListener {
            public abstract BijectiveBinding getBijectiveBinding();
        }
    }

}
