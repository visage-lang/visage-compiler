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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javafx.tree.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author llitchev
 */
public class JavafxDeclarationDefinitionMapper extends JavafxAbstractVisitor {
    /** Creates a new instance of DeclDefMapper */
    protected static final Context.Key<JavafxDeclarationDefinitionMapper> declDefMapperKey =
        new Context.Key<JavafxDeclarationDefinitionMapper>();

    private Table table;
    private Log log;
    
    private JCTree currentOwner;
    private JFXClassDeclaration currentClass;
    private String nameSeparator = ".";
    
    private Map<Name, JCTree> definitions = new HashMap<Name, JCTree>();
    private Map<Name, JCTree> declarations = new HashMap<Name, JCTree>();
    
    public static JavafxDeclarationDefinitionMapper instance(Context context) {
        JavafxDeclarationDefinitionMapper instance = context.get(declDefMapperKey);
        if (instance == null)
            instance = new JavafxDeclarationDefinitionMapper(context);
        return instance;
    }

    protected JavafxDeclarationDefinitionMapper(Context context) {
        table = Table.instance(context);
        log = Log.instance(context);
    }

    @Override
    public void visitTopLevel(JCCompilationUnit cu) {
        super.visitTopLevel(cu);
        for (JCTree definition : definitions.values()) {
            if ((definition.getTag() == JavafxTag.ATTRIBUTEDEF && ((JFXAttributeDefinition)definition).declaration == null) ||
                (definition.getTag() == JavafxTag.FUNCTIONDEF && ((JFXFunctionMemberDefinition)definition).declaration == null) ||
                (definition.getTag() == JavafxTag.OPERATIONDEF && ((JFXOperationMemberDefinition)definition).declaration == null)) {
                log.error(definition.pos, "javafx.member.def.with.no.decl");
            }
        }
    }
    
    public void visitClassDef(JCClassDecl that) {
        that.mods.accept(this);
        for (JCTypeParameter typaram : that.typarams) {
            typaram.accept(this);
        }

        if (that.extending != null) {
            that.extending.accept(this);
        }
        
        for (JCExpression impl : that.implementing) {
            impl.accept(this);
        }
        
        for (JCTree def : that.defs) {
            def.accept(this);
        }
    }

    public void visitClassDeclaration(JFXClassDeclaration that) {
        JCTree prev = currentOwner;
        JFXClassDeclaration prevClass = currentClass;
        currentOwner = that;
        currentClass = that;
        try {
            super.visitClassDeclaration(that);
        }
        finally {
            currentOwner = prev;
            currentClass = prevClass;
        }
    }
    
    public void visitAttributeDeclaration(JFXAttributeDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.attr.not.in.class", that.name.toString());
            }
            
            Name fullName = Name.fromString(table, currentClass.name + nameSeparator + that.name);
            
            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.attr.not.in.class", that.name.toString(), currentClass.name.toString());
            }
            else {
                declarations.put(fullName, that);
            }
            
            JCTree attrDef = definitions.get(fullName);
            if (attrDef != null) {
                if (attrDef.getTag() != JavafxTag.ATTRIBUTEDEF) {
                    log.error(that.pos, "javafx.no.attr.definition", that.name.toString(), currentClass.name.toString());
                }
                else {
                    if (((JFXAttributeDefinition)attrDef).declaration != null) {
                        log.error(that.pos, "javafx.duplicate.attr.declaration", that.name.toString(), currentClass.name.toString());
                    }
                    
                    ((JFXAttributeDefinition)attrDef).declaration = that;
                    if (that.definition != null) {
                        log.error(that.pos, "javafx.duplicate.attr.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.definition = (JFXAttributeDefinition)attrDef;
                }
            }
            
            super.visitAttributeDeclaration(that);
        }
        finally {
            currentOwner = prev;
        }
    }
    
    public void visitFunctionDeclaration(JFXFunctionMemberDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.function.not.in.class", that.name.toString());
            }
            
            Name fullName = Name.fromString(table, currentClass.name + "." + that.name);
            
            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.function.not.in.class", that.name.toString(), currentClass.name.toString());
            }
            else {
                declarations.put(fullName, that);
            }
            
            JCTree funcDef = definitions.get(fullName);
            if (funcDef != null) {
                if (funcDef.getTag() != JavafxTag.FUNCTIONDEF) {
                    log.error(that.pos, "javafx.no.func.definition", that.name.toString(), currentClass.name.toString());
                }
                else {
                    if (((JFXFunctionMemberDefinition)funcDef).declaration != null) {
                        log.error(that.pos, "javafx.duplicate.func.declaration", that.name.toString(), currentClass.name.toString());
                    }
                    
                    ((JFXFunctionMemberDefinition)funcDef).declaration = that;
                    if (that.definition != null) {
                        log.error(that.pos, "javafx.duplicate.func.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.definition = (JFXFunctionMemberDefinition)funcDef;
                }
            }
            
            super.visitFunctionDeclaration(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitOperationDeclaration(JFXOperationMemberDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.operation.not.in.class", that.name.toString());
            }
            
            Name fullName = Name.fromString(table, currentClass.name + "." + that.name);
            
            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.operation.not.in.class", that.name.toString(), currentClass.name.toString());
            }
            else {
                declarations.put(fullName, that);
            }
            
            JCTree operDef = definitions.get(fullName);
            if (operDef != null) {
                if (operDef.getTag() != JavafxTag.OPERATIONDEF) {
                    log.error(that.pos, "javafx.no.oper.definition", that.name.toString(), currentClass.name.toString());
                }
                else {
                    if (((JFXOperationMemberDefinition)operDef).declaration != null) {
                        log.error(that.pos, "javafx.duplicate.oper.declaration", that.name.toString(), currentClass.name.toString());
                    }
                    
                    ((JFXOperationMemberDefinition)operDef).declaration = that;
                    if (that.definition != null) {
                        log.error(that.pos, "javafx.duplicate.oper.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.definition = (JFXOperationMemberDefinition)operDef;
                }
            }

            super.visitOperationDeclaration(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitAttributeDefinition(JFXAttributeDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.attr.not.in.module",
                        that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            }
            
            Name fullName = Name.fromString(table, that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            
            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.attribute.in.module",
                        that.selector.className.toString() + nameSeparator +
                        that.selector.name.toString());
            }
            else {
                definitions.put(fullName, that);
            }
            
            JCTree attrDecl = declarations.get(fullName);
            if (attrDecl != null) {
                if (attrDecl.getTag() != JavafxTag.ATTRIBUTEDECL) {
                    log.error(that.pos, "javafx.no.attr.declaration",
                            that.selector.name.toString(), that.selector.className.toString());
                }
                else {
                    if (((JFXAttributeDeclaration)attrDecl).definition != null) {
                        log.error(that.pos, "javafx.duplicate.attr.definition", that.selector.name.toString(), that.selector.className.toString());
                    }
                    
                    ((JFXAttributeDeclaration)attrDecl).definition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.attr.declaration",
                                that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = (JFXAttributeDeclaration)attrDecl;
                }
            }

            super.visitAttributeDefinition(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitOperationDefinition(JFXOperationMemberDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.oper.not.in.module",
                        that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            }
            
            Name fullName = Name.fromString(table, that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            
            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.operation.in.module",
                        that.selector.className.toString() + nameSeparator +
                        that.selector.name.toString());
            }
            else {
                definitions.put(fullName, that);
            }
            
            JCTree operDecl = declarations.get(fullName);
            if (operDecl != null) {
                if (operDecl.getTag() != JavafxTag.OPERATIONDECL) {
                    log.error(that.pos, "javafx.no.oper.declaration",
                            that.selector.name.toString(), that.selector.className.toString());
                }
                else {
                    if (((JFXOperationMemberDeclaration)operDecl).definition != null) {
                        log.error(that.pos, "javafx.duplicate.oper.definition", that.selector.name.toString(), that.selector.className.toString());
                    }
                    
                    ((JFXOperationMemberDeclaration)operDecl).definition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.oper.declaration",
                                that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = (JFXOperationMemberDeclaration)operDecl;
                }
            }

            super.visitOperationDefinition(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitFunctionDefinition(JFXFunctionMemberDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.func.not.in.module",
                        that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            }
            
            Name fullName = Name.fromString(table, that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            
            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.function.in.module",
                        that.selector.className.toString() + nameSeparator +
                        that.selector.name.toString());
            }
            else {
                definitions.put(fullName, that);
            }
            
            JCTree funcDecl = declarations.get(fullName);
            if (funcDecl != null) {
                if (funcDecl.getTag() != JavafxTag.FUNCTIONDECL) {
                    log.error(that.pos, "javafx.no.func.declaration",
                            that.selector.name.toString(), that.selector.className.toString());
                }
                else {
                    if (((JFXFunctionMemberDeclaration)funcDecl).definition != null) {
                        log.error(that.pos, "javafx.duplicate.func.definition", that.selector.name.toString(), that.selector.className.toString());
                    }
                    
                    ((JFXFunctionMemberDeclaration)funcDecl).definition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.func.declaration",
                                that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = (JFXFunctionMemberDeclaration)funcDecl;
                }
            }

            super.visitFunctionDefinition(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitOperationLocalDefinition(JFXOperationLocalDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            Name fullName = that.getName();
            
            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.local.operation.definition",
                        fullName.toString());
            }
            else {
                definitions.put(fullName, that);
            }
            
            super.visitOperationLocalDefinition(that);
        }
        finally {
            currentOwner = prev;
        }
    }

    public void visitFunctionLocalDefinition(JFXFunctionLocalDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            Name fullName = that.getName();
            
            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.local.function.definition",
                        fullName.toString());
            }
            else {
                definitions.put(fullName, that);
            }
            
            super.visitFunctionLocalDefinition(that);
        }
        finally {
            currentOwner = prev;
        }
    }
}
