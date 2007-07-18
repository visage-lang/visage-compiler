/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import java.util.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import com.sun.tools.javac.comp.Check;

/** Type checking helper class for the attribution phase.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("@(#)JavafxCheck.java	1.173 07/05/05")
public class JavafxCheck extends Check {
    public static Check instance0(Context context) {
        Check instance = context.get(checkKey);
        if (instance == null)
            instance = new JavafxCheck(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(checkKey, new Context.Factory<Check>() {
	       public Check make() {
		   return new JavafxCheck(context);
	       }
        });
    }

    protected JavafxCheck(Context context) {
        super(context);
    }

    /** Check that symbol is unique in given scope.
     *	@param pos	     Position for error reporting.
     *	@param sym	     The symbol.
     *	@param s	     The scope.
     */
    public boolean checkUnique(DiagnosticPosition pos, Symbol sym, Scope s) {
// Javafx change
//        if (sym.type.isErroneous())
        if (sym.type != null && sym.type.isErroneous())
// Javafx change
	    return true;
	if (sym.owner.name == names.any) return false;
	for (Scope.Entry e = s.lookup(sym.name); e.scope == s; e = e.next()) {
	    if (sym != e.sym &&
		sym.kind == e.sym.kind &&
		sym.name != names.error &&
		(sym.kind != MTH || types.overrideEquivalent(sym.type, e.sym.type))) {
		if ((sym.flags() & VARARGS) != (e.sym.flags() & VARARGS))
		    varargsDuplicateError(pos, sym, e.sym);
		else 
		    duplicateError(pos, e.sym);
		return false;
	    }
	}
	return true;
    }
}
