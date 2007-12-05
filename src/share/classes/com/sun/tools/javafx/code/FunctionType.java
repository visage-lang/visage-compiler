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

package com.sun.tools.javafx.code;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Symbol.TypeSymbol;

/**
 *
 * @author bothner
 */
public class FunctionType extends Type.ClassType {

    Type restype;
    public MethodType mtype;
    
    public FunctionType(Type outer, List<Type> typarams, TypeSymbol tsym, Type restype) {
        super(outer, typarams, tsym);
        this.restype = restype;
    }

    /** Copy constructor. */
    public FunctionType(FunctionType orig) {
        this(orig.getEnclosingType(), orig.typarams_field, orig.tsym, orig.restype);
        mtype = orig.mtype;
    }

    public List<Type>        getParameterTypes() { return mtype.getParameterTypes(); } 
    public Type              getReturnType()     { return restype; }
    
    public MethodType asMethodType () { return mtype; }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("function(");
        if (mtype == null)
            s.append("???");
        else {
            List<Type> args = mtype.argtypes;
            for (List<Type> l = args; l.nonEmpty(); l = l.tail) {
                if (l != args)
                    s.append(',');
                s.append(':');
                s.append(l.head);
            }
        }
        s.append("):");
        s.append(restype);
        return s.toString();
    }
}
