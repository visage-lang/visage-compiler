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

package javafx.lang;
import com.sun.javafx.runtime.Entry;

/**
  * A {@code DeferredTask} represents an action that should be executed at a
  * later time of the system's choosing.
  * <p />
  * In systems based on event dispatch, such as Swing, execution of a
  * {@code DeferredTask} generally means putting it on the event queue
  * for later processing.
  *
  * @profile common
  */
public class DeferredTask {
    /** 
     * A function to be called when the deferred task is executed.
     * <p />
     * Must be provided.
     *
     * @profile common
     */
    public /* required initonly */ var action: function() : Void;

    /** 
     * A {@code boolean} value indicating whether the deferred task has actually
     * been run, which can be bound to.  Read-only. 
     *
     * @profile common
     * @readonly
     */
    public /* readable private */ var done : Boolean;

    postinit {
        Entry.deferTask(function() : Void { try { action(); } finally { done = true } });
    }
}
