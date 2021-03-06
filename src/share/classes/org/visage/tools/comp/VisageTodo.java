/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.tools.comp;

import com.sun.tools.mjavac.util.*;

/** A queue of all as yet unattributed classes.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class VisageTodo extends ListBuffer<VisageEnv<VisageAttrContext>> {
    /** The context key for the todo list. */
    protected static final Context.Key<VisageTodo> visageTodoKey =
	new Context.Key<VisageTodo>();

    /** Get the Todo instance for this context. */
    public static VisageTodo instance(Context context) {
	VisageTodo instance = context.get(visageTodoKey);
	if (instance == null)
	    instance = new VisageTodo(context);
	return instance;
    }

    /** Create a new todo list. */
    protected VisageTodo(Context context) {
	context.put(visageTodoKey, this);
    }
}
