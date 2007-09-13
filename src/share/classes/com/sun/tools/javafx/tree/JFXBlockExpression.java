/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.*;
import com.sun.source.tree.*;
import com.sun.tools.javac.code.*;
import com.sun.source.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.comp.*;
/**
 *
 * @author bothner
 */
public class JFXBlockExpression extends JFXExpression {
    public long flags;
    public List<JCStatement> stats;
    public JCExpression value;
    /** Position of closing brace, optional. */
    public int endpos = Position.NOPOS;
    protected JFXBlockExpression(long flags, List<JCStatement> stats, JCExpression value) {
        this.stats = stats;
        this.flags = flags;
        this.value = value;
    }
    
    public void accept(JavafxVisitor v) { v.visitBlockExpression(this);}

    public List<JCStatement> getStatements() {
        return stats;
    }
    
    public void accept(Visitor v) {
        // Kludge
        if (v instanceof Pretty && !(v instanceof JavafxPretty))
            BlockExprPretty.visitBlockExpression((Pretty) v, this);
        else if (v instanceof BlockExprAttr)
            ((BlockExprAttr) v).visitBlockExpression(this);
        else if (v instanceof BlockExprEnter)
            ((BlockExprEnter) v).visitBlockExpression(this);
        else if (v instanceof BlockExprMemberEnter)
            ((BlockExprMemberEnter) v).visitBlockExpression(this);
        else if (v instanceof JavafxPrepForBackEnd)
            ((JavafxPrepForBackEnd) v).visitBlockExpression(this);
        
        // these should probably be removed
        else if (v instanceof JavafxTypeMorpher)
            ((JavafxTypeMorpher) v).visitBlockExpression(this);
        else if (v instanceof JavafxVarUsageAnalysis)
            ((JavafxVarUsageAnalysis) v).visitBlockExpression(this);
        else if (v instanceof TreeScanner) {
            ((TreeScanner)v).scan(stats);
            ((TreeScanner)v).scan(value);
        } else if (v instanceof TreeTranslator) {
            stats = ((TreeTranslator)v).translate(stats);
            value = ((TreeTranslator)v).translate(value);
            ((TreeTranslator)v).result = this;
        } else
            super.accept(v);
    }
    public boolean isStatic() { return (flags & Flags.STATIC) != 0; }

    @Override
    public int getTag() {
        return JavafxTag.BLOCK_EXPRESSION;
    }

}
