/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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

package com.sun.tools.javafx.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.comp.JavafxDefs;

/** Prints out a tree as an indented Java source program.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code tree depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 */
public class JavaPretty extends Pretty {
	private HashSet<Name> importedPackages = new HashSet<Name>();
	private HashSet<Name> importedClasses = new HashSet<Name>();
	
    public JavaPretty(Writer out, boolean sourceOutput, Context context) {
        super(out, sourceOutput);

		JavafxDefs defs = JavafxDefs.instance(context);
		importedPackages.add(defs.runtimePackageName);
		importedPackages.add(defs.locationPackageName);
		importedPackages.add(defs.sequencePackageName);
		importedPackages.add(defs.functionsPackageName);
                importedPackages.add(defs.javaLangPackageName);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        visitBlockExpression(this, tree);
    }

    public static void visitBlockExpression(Pretty pretty, JFXBlockExpression tree) {
        try {
            pretty.printFlags(tree.flags);
            pretty.print("{");
            pretty.println();
            pretty.indent();
            pretty.printStats(tree.stats);
            if (tree.value != null) {
                pretty.align();
                pretty.printExpr(tree.value);
            }
            pretty.undent();
            pretty.println();
            pretty.align();
            pretty.print("}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
	
	@Override
    public void visitImport(JCImport tree) {
		super.visitImport(tree);

		// save imports for later use
		Name name = TreeInfo.name(tree.qualid);
		if (name == name.table.asterisk)
			if (tree.qualid.getTag() == JCTree.SELECT)
				importedPackages.add(TreeInfo.fullName(((JCFieldAccess) tree.qualid).selected));
			else;
		else
			importedClasses.add(TreeInfo.fullName(tree.qualid));
    }

	@Override
    public void visitSelect(JCFieldAccess tree) {
        try {
			if (!importedPackages.contains(TreeInfo.fullName(tree.selected)) 
					&& !importedClasses.contains(TreeInfo.fullName(tree))) {
				printExpr(tree.selected, TreeInfo.postfixPrec);
				print(".");
			}
            print(tree.name);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }	
}
