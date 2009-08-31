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

package com.sun.tools.javafx.comp;

import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Symbol.*;

import com.sun.tools.javafx.tree.JavafxTreeMaker;

/** Enter annotations on symbols.  Annotations accumulate in a queue,
 *  which is processed at the top level of any set of recursive calls
 *  requesting it be processed.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxAnnotate {
    protected static final Context.Key<JavafxAnnotate> javafxAnnotateKey =
	new Context.Key<JavafxAnnotate>();

    public static JavafxAnnotate instance(Context context) {
	JavafxAnnotate instance = context.get(javafxAnnotateKey);
	if (instance == null)
	    instance = new JavafxAnnotate(context);
	return instance;
    }

    final JavafxAttr attr;
    final JavafxTreeMaker fxmake;
    final Log log;
    final Symtab syms;
    final Name.Table names;
    final JavafxResolve rs;
    final Types types;
//TODO:?    final ConstFold cfolder;
    final JavafxCheck chk;

    protected JavafxAnnotate(Context context) {
	context.put(javafxAnnotateKey, this);
	attr = JavafxAttr.instance(context);
	fxmake = JavafxTreeMaker.instance(context);
	log = Log.instance(context);
	syms = Symtab.instance(context);
	names = Name.Table.instance(context);
	rs = JavafxResolve.instance(context);
	types = Types.instance(context);
//TODO:?	cfolder = ConstFold.instance(context);
	chk = (JavafxCheck)JavafxCheck.instance(context);
    }

/* ********************************************************************
 * Queue maintenance
 *********************************************************************/

    private int enterCount = 0;

    ListBuffer<Annotator> q = new ListBuffer<Annotator>();

    public void later(Annotator a) {
	q.append(a);
    }

    public void earlier(Annotator a) {
	q.prepend(a);
    }

    /** Called when the Enter phase starts. */
    public void enterStart() {
        enterCount++;
    }

    /** Called after the Enter phase completes. */
    public void enterDone() {
        enterCount--;
	flush();
    }

    public void flush() {
	if (enterCount != 0) return;
        enterCount++;
	try {
	    while (q.nonEmpty())
                q.next().enterAnnotation();
	} finally {
            enterCount--;
	}
    }

    /** A client that has annotations to add registers an annotator,
     *  the method it will use to add the annotation.  There are no
     *  parameters; any needed data should be captured by the
     *  Annotator.
     */
    public interface Annotator {
	void enterAnnotation();
	String toString();
    }


/* ********************************************************************
 * Compute an attribute from its annotation.
 *********************************************************************/

}
