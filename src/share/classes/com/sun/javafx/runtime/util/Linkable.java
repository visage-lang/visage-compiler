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

package com.sun.javafx.runtime.util;

/**
 * Linkable represents a class that can be linked together with other objects of its kind into a linked list.  There
 * is a link to the next element, and a link back to the object that holds the list head, so that elements can be
 * removed.  Relevant methods for manipulation are in AbstractLinkable.
 *
 * It may be desirable for an object to have multiple sets of links.  In that case, we'd want to refactor so that rather
 * than the class extending Linkable, there are multiple "link adapters" for each set of link.  It may also be desirable
 * to split into singly linked and backlinked versions, to save space; sometimes the backlink is not needed.  
 *
 * @author Brian Goetz
 */
public interface Linkable<T, H> {
    T getNext();
    H getHost();

    void setNext(T next);
    void setHost(H host);

    public interface HeadAccessor<T, U> {
        public T getHead(U host);
        public void setHead(U host, T newHead);
    }

    public interface MutativeIterationClosure<T, U> {
        /** Returns true if the element should be kept in the list, false otherwise */
        public boolean action(T element);
    }

    public interface IterationClosure<T> {
        public void action(T element);
    }
}
