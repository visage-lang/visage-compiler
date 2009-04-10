/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.util.*;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.CircularBindingException;

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
 * @author Brian Goetz
 */
public class Bindings {

    public static<T> Bijection<T, T> identityBinding() {
        return new Bijection<T, T>() {
            public T mapForwards(T a) {
                return a;
            }

            public T mapBackwards(T b) {
                return b;
            }
        };
    }

    /** Create a bijective binding between objects of type T and U */
    public static <T, U> void bijectiveBind(ObjectLocation<T> a, ObjectLocation<U> b, Bijection<T, U> mapper) {
        new BijectiveBinding<T, U>(a, b, mapper);
    }

    /** Create a bijective binding between objects of type T and U */
    public static <T> void bijectiveBind(ObjectLocation<T> a, ObjectLocation<T> b) {
        Bijection<T, T> id = identityBinding();
        bijectiveBind(a, b, id);
    }

    /** Create a new location that is bidirectionally bound to another */
    public static<T> ObjectLocation<T> makeBijectiveBind(ObjectLocation<T> other) {
        ObjectVariable<T> me = ObjectVariable.make();
        bijectiveBind(me, other);
        return me;
    }

    /** Return the set of locations that are "peered" with this one through a chain of bidirectional bindings */
    static Collection<Location> getPeerLocations(Location location) {
        Collection<Location> newLocs = BijectiveBinding.getDirectPeers(location);
        if (newLocs.size() == 0)
            return newLocs;

        Set<Location> knownLocs = new HashSet<Location>();
        LinkedList<Location> toExplore = new LinkedList<Location>(newLocs);
        while (toExplore.size() > 0) {
            Location loc = toExplore.removeFirst();
            while (loc.isViewLocation())
                loc = loc.getUnderlyingLocation();
            if (!knownLocs.contains(loc) && loc != location) {
                knownLocs.add(loc);
                toExplore.addAll(BijectiveBinding.getDirectPeers(loc));
            }
        }
        return knownLocs;
    }

    static boolean isPeerLocation(Location a, Location b) {
        Collection<Location> aPeers = getPeerLocations(a);
        while (b.isViewLocation())
            b = b.getUnderlyingLocation();
        return (aPeers != null && aPeers.contains(b));
    }

    private static class BijectiveBinding<T, U> {
        private final WeakReference<ObjectLocation<T>> aRef;
        private final WeakReference<ObjectLocation<U>> bRef;
        private final Bijection<T, U> mapper;
        private T lastA;
        private U lastB;

        public BijectiveBinding(ObjectLocation<T> a, ObjectLocation<U> b, Bijection<T, U> mapper) {
            if (!(a.isMutable()) || !(b.isMutable()))
                throw new BindingException("Both components of bijective bind must be mutable");
            if (isPeerLocation(a, b))
                throw new CircularBindingException("Binding circularity detected");
            
            this.aRef = new WeakReference<ObjectLocation<T>>(a);
            this.bRef = new WeakReference<ObjectLocation<U>>(b);
            this.mapper = mapper;

            // Set A before setting up the listeners
            a.set(mapper.mapBackwards(b.get()));

            a.addInvalidationListener(new BijectiveInvalidationListener() {
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
            b.addInvalidationListener(new BijectiveInvalidationListener() {
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

        public static List<Location> getDirectPeers(final Location loc) {
            class DirectPeerClosure extends DependencyIterator<InvalidationListener> {
                List<Location> list = null;

                public DirectPeerClosure() {
                    super(AbstractLocation.CHILD_KIND_CHANGE_LISTENER);
                }

                public void onAction(InvalidationListener element) {
                    if (element instanceof BijectiveInvalidationListener) {
                        BijectiveBinding<?, ?> bb = ((BijectiveInvalidationListener) element).getBijectiveBinding();
                        ObjectLocation<?> a = (ObjectLocation) bb.aRef.get();
                        ObjectLocation<?> b = (ObjectLocation) bb.bRef.get();
                        if (a != null && a != loc) {
                            if (list == null)
                                list = new ArrayList<Location>();
                            list.add(a);
                        }
                        if (b != null && b != loc) {
                            if (list == null)
                                list = new ArrayList<Location>();
                            list.add(b);
                        }
                    }
                }
            }

            DirectPeerClosure closure = new DirectPeerClosure();
            loc.iterateChildren(closure);
            if (closure.list != null)
                return closure.list;
            else
                return Collections.emptyList();
        }

        private static abstract class BijectiveInvalidationListener extends InvalidationListener {
            public abstract BijectiveBinding getBijectiveBinding();
        }
    }

}
