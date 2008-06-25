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

package javafx.ext.swing;

import javafx.lang.FX;

/**
 * A {@code Container} is something that can contain {@code Components}.
 */
public abstract class Container {

    /**
     * Called when a {@code Component} is being reparented to notify its
     * old {@code Container} that it is to be removed.
     *
     * @param component the {@code Component} to remove
     */
    protected abstract function remove(component: Component): Void;

    /**
     * Returns this {@code Container's} parent {@code Container},
     * or {@code null} if it doesn't have a parent.
     *
     * @return this {@code Container's} parent or {@code null}
     */
    public abstract bound function getParent(): Container;

    /**
     * Returns this {@code Container's} name,
     * or {@code null} if it doesn't have a name.
     * 
     * @return this {@code Container's} name or {@code null}
     */
    public abstract bound function getName(): String;

    /**
     * Unparents the given {@code Component} from this {@code Container}.
     * If the given {@code Component} is currently parented to this {@code Container},
     * sets the {@code Component's} parent to {@code null}. Otherwise, does nothing.
     * Does nothing if the parameter is {@code null}.
     * 
     * @param component the component to unparent from this {@code Container}
     */
    protected /* final */ function unparentFromThisContainer(component: Component): Void {
        if (component == null) {
            return;
        }

        if (FX.isSameObject(component.parent, this)) {
            component.parent = null;
        }
    }

    /**
     * Parents the given {@code Component} to this {@code Container}.
     * First, unparents the {@code Component} from its current {@code Container},
     * if any, by calling {@code remove(component)}. Then sets the
     * {@code Component's} parent to this.
     * Does nothing if the parameter is {@code null} or is already parented to
     * this {@code Container}.
     * 
     * @param component the component to parent to this {@code Container}
     */
    protected /* final */ function parentToThisContainer(component: Component): Void {
        if (component == null or FX.isSameObject(component.parent, this)) {
            return;
        }

        if (component.parent <> null) {
            component.parent.remove(component);
        }

        component.parent = this;
    }

}
