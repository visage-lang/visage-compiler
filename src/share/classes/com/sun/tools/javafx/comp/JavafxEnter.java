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

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javafx.tree.JavafxTag.*;
import com.sun.tools.javac.comp.Enter;

@Version("@(#)JavafxEnter.java	1.140 07/05/05")
public class JavafxEnter extends Enter {
    private Table nameTable;
    private Target target;

    public static Enter instance0(Context context) {
        Enter instance = context.get(enterKey);
        if (instance == null)
            instance = new JavafxEnter(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(enterKey, new Context.Factory<Enter>() {
	       public Enter make() {
		   return new JavafxEnter(context);
	       }
        });
    }

    protected JavafxEnter(Context context) {
        super(context);
       
        nameTable = Table.instance(context);
        target = Target.instance(context);
    }

   public void visitClassDef(JCClassDecl tree) {
	Symbol owner = env.info.scope.owner;
	Scope enclScope = enterScope(env);
	ClassSymbol c;
	if (owner.kind == PCK) {
	    // We are seeing a toplevel class.
	    PackageSymbol packge = (PackageSymbol)owner;
	    for (Symbol q = packge; q != null && q.kind == PCK; q = q.owner)
		q.flags_field |= EXISTS;
	    c = reader.enterClass(tree.name, packge);
	    packge.members().enterIfAbsent(c);
	} else {
	    if (tree.name.len != 0 &&
		!chk.checkUniqueClassName(tree.pos(), tree.name, enclScope)) {
		result = null;
		return;
	    }
	    if (owner.kind == TYP) {
		// We are seeing a member class.
		c = reader.enterClass(tree.name, (TypeSymbol)owner);
		if ((owner.flags_field & INTERFACE) != 0) {
		    tree.mods.flags |= PUBLIC | STATIC;
		}
	    } else {
		// We are seeing a local class.
		c = reader.defineClass(tree.name, owner);
		c.flatname = chk.localClassName(c);
		if (c.name.len != 0)
		    chk.checkTransparentClass(tree.pos(), c, env.info.scope);
	    }
	}
	tree.sym = c;

	// Enter class into `compiled' table and enclosing scope.
	if (chk.compiled.get(c.flatname) != null) {
	    duplicateClass(tree.pos(), c);
	    result = new ErrorType(tree.name, (TypeSymbol)owner);
	    tree.sym = (ClassSymbol)result.tsym;
	    return;
	}
	chk.compiled.put(c.flatname, c);
	enclScope.enter(c);

	// Set up an environment for class block and store in `typeEnvs'
	// table, to be retrieved later in memberEnter and attribution.
	Env<AttrContext> localEnv = classEnv(tree, env);
	typeEnvs.put(c, localEnv);

	// Fill out class fields.
	c.completer = memberEnter;
	c.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, c, tree);
	c.sourcefile = env.toplevel.sourcefile;
	c.members_field = new Scope(c);

	ClassType ct = (ClassType)c.type;
	if (owner.kind != PCK && (c.flags_field & STATIC) == 0) {
	    // We are seeing a local or inner class.
	    // Set outer_field of this class to closest enclosing class
	    // which contains this class in a non-static context
	    // (its "enclosing instance class"), provided such a class exists.
	    Symbol owner1 = owner;
	    while ((owner1.kind & (VAR | MTH)) != 0 &&
		   (owner1.flags_field & STATIC) == 0) {
		owner1 = owner1.owner;
	    }
	    if (owner1.kind == TYP) {
		ct.setEnclosingType(owner1.type);
	    }
	}

	// Enter type parameters.
	ct.typarams_field = classEnter(tree.typarams, localEnv);

	// Add non-local class to uncompleted, to make sure it will be
	// completed later.
	if (!c.isLocal() && uncompleted != null) uncompleted.append(c);
//	System.err.println("entering " + c.fullname + " in " + c.owner);//DEBUG

	// Recursively enter all member classes.
	classEnter(tree.defs, localEnv);

	result = c.type;
    }
}
