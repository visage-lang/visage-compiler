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

package com.sun.tools.javafx.code;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 *
 * @author llitchev
 */
public class JavafxMethodSymbol extends MethodSymbol {
    private int javafxMethodType = JavafxFlags.SIMPLE_JAVA;
    private List<Symbol> capturedOuters = List.nil(); // captured outer references. For cloasure imlementation.

    /** Creates a new instance of JavafxMethodSymbol */
    public JavafxMethodSymbol(long flags,
                              int javafxMethodType,
                              Name name,
                              Type type,
                              Symbol owner,
                              List<Symbol> captureOuters) {
        super(flags, name, type, owner);
        this.javafxMethodType = javafxMethodType;
        this.capturedOuters = capturedOuters;
    }
    
}
