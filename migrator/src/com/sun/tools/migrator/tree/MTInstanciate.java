/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import  com.sun.tools.migrator.tree.MTTree.*;
import com.sun.tools.javac.util.List;

    /**
     * A new(...) operation.
     */
    public class MTInstanciate extends MTExpression {
        public MTExpression encl;
        public MTExpression clazz;
        public List<MTExpression> args;
        public MTClassDeclaration def;
        protected MTInstanciate(MTExpression encl,
			   MTExpression clazz,
			   List<MTExpression> args,
			   MTClassDeclaration def)
	{
            this.encl = encl;
            this.clazz = clazz;
            this.args = args;
            this.def = def;
        }
        @Override
        public void accept(MTVisitor v) { v.visitInstanciate(this); }

        public Kind getKind() { return Kind.NEW_CLASS; }
        public MTExpression getEnclosingExpression() { // expr.new C< ... > ( ... )
            return encl;
        }
        public MTExpression getIdentifier() { return clazz; }
        public List<MTExpression> getArguments() {
            return args;
        }
        public MTClassDeclaration getClassBody() { return def; }
        @Override
        public int getTag() {
            return NEWCLASS;
        }
    }

