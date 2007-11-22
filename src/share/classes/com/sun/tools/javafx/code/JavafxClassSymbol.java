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

package com.sun.tools.javafx.code;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.comp.JavafxClassReader;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder;

/**
 * Marker wrapper on class: this is a JavaFX var
 * 
 * @author llitchev
 */
public class JavafxClassSymbol extends ClassSymbol {
    private JavafxClassReader reader;
    private JavafxInitializationBuilder initBuilder;
    
    private List<Type> supertypes = List.<Type>nil();
    
    /** Creates a new instance of JavafxClassSymbol */
    public JavafxClassSymbol(long flags, Name name, Type type, Symbol owner, JavafxClassReader reader) {
        super(flags, name, type, owner);
        this.reader = reader;
        initBuilder = reader.getInitBuilder();
    }

    public JavafxClassSymbol(long flags, Name name, Symbol owner, JavafxClassReader reader) {
        super(flags, name, owner);
        this.reader = reader;
        initBuilder = reader.getInitBuilder();
    }
    
    public void addSuperType(Type type) {
        supertypes = supertypes.append(type);
    }
    
    public List<Type> getSuperTypes() {
        return supertypes;
    }

    public void complete() throws CompletionFailure {
        if (completer == null) {
            return;
        }
        super.complete();
        // Convert all the base interfaces of the form className$Intf gto base classes in the supertypes list.
        if (initBuilder != null && 
                this.type != null && 
                type instanceof ClassType) {
            String cName = this.fullname.toString() + initBuilder.interfaceNameSuffix.toString();
            Type baseIntf = null;
            if (type != null && ((ClassType)type).interfaces_field != null) {
                for (Type ct : ((ClassType)type).interfaces_field) {
                    if (ct.toString().equals(cName)) {
                        baseIntf = ct;
                        break;
                    }
                }
            }

            if (baseIntf != null && baseIntf instanceof ClassType) {
                baseIntf.complete();
                if (baseIntf.tsym != null && baseIntf.tsym.type != null &&
                        ((ClassType)baseIntf.tsym.type).interfaces_field != null) {
                    for (Type baseType : ((ClassType)baseIntf.tsym.type).interfaces_field) {
                        if (baseType.toString().endsWith(initBuilder.interfaceNameSuffix.toString())) {
                            String baseTypeName = baseType.toString();
                            Type tp = reader.enterClass(initBuilder.names.fromString(baseTypeName.substring(0, baseTypeName.length() - initBuilder.interfaceNameSuffix.toString().length()))).type;
                            addSuperType(tp);
                        }
                    }
                }
            }
        }
    }
}
