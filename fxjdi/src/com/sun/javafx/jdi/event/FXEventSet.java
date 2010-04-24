/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.jdi.event;

import com.sun.javafx.jdi.FXMirror;
import com.sun.javafx.jdi.FXVirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author sundar
 */
public class FXEventSet extends FXMirror implements EventSet {
    public FXEventSet(FXVirtualMachine fxvm, EventSet underlying) {
        super(fxvm, underlying);
    }

    public FXEventIterator eventIterator() {
        return FXEventIterator.wrap(virtualMachine(), underlying().eventIterator());
    }

    public void resume() {
        underlying().resume();
    }

    public int suspendPolicy() {
        return underlying().suspendPolicy();
    }

    public boolean add(Event evt) {
        return underlying().add(FXEvent.unwrap(evt));
    }

    public boolean addAll(Collection<? extends Event> events) {
        return underlying().addAll(FXEvent.unwrapEvents(events));
    }

    public void clear() {
        underlying().clear();
    }

    public boolean contains(Object obj) {
        return underlying().contains((obj instanceof Event)? FXEvent.unwrap((Event)obj) : obj);
    }

    public boolean containsAll(Collection<?> arg0) {
        return underlying().containsAll(FXEvent.unwrapEvents(arg0));
    }

    public boolean isEmpty() {
        return underlying().isEmpty();
    }

    public Iterator<Event> iterator() {
        final Iterator<Event> wrapped = underlying().iterator();
        if (wrapped != null) {
            return new Iterator<Event>() {
                public boolean hasNext() {
                    return wrapped.hasNext();
                }

                public Event next() {
                    return FXEvent.wrap(virtualMachine(), wrapped.next());
                }

                public void remove() {
                    wrapped.remove();
                }
            };
        } else {
            return null;
        }
    }

    public boolean remove(Object obj) {
        return underlying().remove((obj instanceof Event)?
            FXEvent.unwrap((Event)obj) : obj);
    }

    public boolean removeAll(Collection<?> arg0) {
        return underlying().removeAll(FXEvent.unwrapEvents(arg0));
    }

    public boolean retainAll(Collection<?> arg0) {
        return underlying().retainAll(FXEvent.unwrapEvents(arg0));
    }

    public int size() {
        return underlying().size();
    }

    public Object[] toArray() {
        Object[] res = underlying().toArray();
        if (res != null) {
            for (int i = 0; i < res.length; i++) {
                if (res[i] instanceof Event) {
                    res[i] = FXEvent.unwrap((Event)res[i]);
                }
            }
        }
        return res;
    }

    public <T> T[] toArray(T[] arg0) {
        // FIXME
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected EventSet underlying() {
        return (EventSet) super.underlying();
    }

    public static FXEventSet wrap(FXVirtualMachine fxvm, EventSet evtSet) {
        return (evtSet == null)? null : new FXEventSet(fxvm, evtSet);
    }
}