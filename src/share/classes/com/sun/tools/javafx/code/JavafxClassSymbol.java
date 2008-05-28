/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.code;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ErrorType;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.comp.JavafxClassReader;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder;
import static com.sun.tools.javafx.comp.JavafxDefs.*;

/**
 * Marker wrapper on class: this is a JavaFX var
 * 
 * @author llitchev
 */
public class JavafxClassSymbol extends ClassSymbol {
    public ClassSymbol jsymbol;
    private List<Type> supertypes = List.<Type>nil();
    
    /** Creates a new instance of JavafxClassSymbol */
    public JavafxClassSymbol(long flags, Name name, Symbol owner) {
        super(flags, name, owner);
    }
    
    public void addSuperType(Type type) {
        if (this.type instanceof ErrorType &&
                type instanceof ClassType)
            type = new ErrorType((ClassSymbol) type.tsym);
        supertypes = supertypes.append(type);
    }
    
    public List<Type> getSuperTypes() {
        return supertypes;
    }
}
