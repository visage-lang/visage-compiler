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
        if (instance == null) {
            instance = new JavafxDeclarationDefinitionMapper(context);
        }
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
            if ((definition.getTag() == JavafxTag.RETROATTRIBUTEDEF && ((JFXRetroAttributeDefinition)definition).declaration == null) ||
                (definition.getTag() == JavafxTag.RETROFUNCTIONDEF && ((JFXRetroFunctionMemberDefinition)definition).declaration == null) ||
                (definition.getTag() == JavafxTag.RETROOPERATIONDEF && ((JFXRetroOperationMemberDefinition)definition).declaration == null)) {
                log.error(definition.pos, "javafx.member.def.with.no.decl");
            }
        }
    }

    @Override
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

    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        JCTree prev = currentOwner;
        JFXClassDeclaration prevClass = currentClass;
        currentOwner = that;
        currentClass = that;
        try {
            super.visitClassDeclaration(that);
        } finally {
            currentOwner = prev;
            currentClass = prevClass;
        }
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            super.visitFunctionDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroAttributeDeclaration(JFXRetroAttributeDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.attr.not.in.class", that.name.toString());
            }

            Name fullName = Name.fromString(table, currentClass.name + nameSeparator + that.name);

            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.attr.not.in.class", that.name.toString(), currentClass.name.toString());
            } else {
                declarations.put(fullName, that);
            }

            JCTree def = definitions.get(fullName);
            if (def != null) {
                JFXRetroAttributeDefinition attrDef = (JFXRetroAttributeDefinition) def;
                if (attrDef.getTag() != JavafxTag.RETROATTRIBUTEDEF) {
                    log.error(that.pos, "javafx.no.attr.definition", that.name.toString(), currentClass.name.toString());
                } else {
                    if (attrDef.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.attr.declaration", that.name.toString(), currentClass.name.toString());
                    }

                    attrDef.declaration = that;
                    if (that.retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.attr.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.retroDefinition = attrDef;
                }
            }

            super.visitRetroAttributeDeclaration(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroFunctionDeclaration(JFXRetroFunctionMemberDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.function.not.in.class", that.name.toString());
            }

            Name fullName = Name.fromString(table, currentClass.name + "." + that.name);

            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.function.not.in.class", that.name.toString(), currentClass.name.toString());
            } else {
                declarations.put(fullName, that);
            }

            JCTree def = definitions.get(fullName);
            if (def != null) {
                JFXRetroFunctionMemberDefinition funcDef = (JFXRetroFunctionMemberDefinition)def;
                if (funcDef.getTag() != JavafxTag.RETROFUNCTIONDEF) {
                    log.error(that.pos, "javafx.no.func.definition", that.name.toString(), currentClass.name.toString());
                } else {
                    if (funcDef.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.func.declaration", that.name.toString(), currentClass.name.toString());
                    }

                    funcDef.declaration = that;
                    if (that.retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.func.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.retroDefinition = funcDef;
                }
            }

            super.visitRetroFunctionDeclaration(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroOperationDeclaration(JFXRetroOperationMemberDeclaration that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.operation.not.in.class", that.name.toString());
            }

            Name fullName = Name.fromString(table, currentClass.name + "." + that.name);

            if (declarations.get(fullName) != null) {
                log.error(that.pos, "javafx.member.operation.not.in.class", that.name.toString(), currentClass.name.toString());
            } else {
                declarations.put(fullName, that);
            }

            JCTree def = definitions.get(fullName);
            if (def != null) {
                JFXRetroOperationMemberDefinition operDef = (JFXRetroOperationMemberDefinition)def;
                if (operDef.getTag() != JavafxTag.RETROOPERATIONDEF) {
                    log.error(that.pos, "javafx.no.oper.definition", that.name.toString(), currentClass.name.toString());
                } else {
                    if (operDef.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.oper.declaration", that.name.toString(), currentClass.name.toString());
                    }

                    operDef.declaration = that;
                    if (that.retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.oper.definition", that.name.toString(), currentClass.name.toString());
                    }
                    that.retroDefinition = operDef;
                }
            }

            super.visitRetroOperationDeclaration(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroAttributeDefinition(JFXRetroAttributeDefinition that) {
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

            JCTree decl = declarations.get(fullName);
            if (decl != null) {
                JFXRetroAttributeDeclaration attrDecl = (JFXRetroAttributeDeclaration)decl;
                if (attrDecl.getTag() != JavafxTag.RETROATTRIBUTEDECL) {
                    log.error(that.pos, "javafx.no.attr.declaration", that.selector.name.toString(), that.selector.className.toString());
                } else {
                    if (attrDecl.retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.attr.definition", that.selector.name.toString(), that.selector.className.toString());
                    }

                    attrDecl.retroDefinition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.attr.declaration", that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = attrDecl;
                }
            }

            super.visitRetroAttributeDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroOperationDefinition(JFXRetroOperationMemberDefinition that) {
        JCTree prev = currentOwner;
        currentOwner = that;
        try {
            that.owner = prev;
            if (prev != currentClass) {
                log.error(that.pos, "javafx.member.oper.not.in.module", that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            }

            Name fullName = Name.fromString(table, that.selector.className.toString() + nameSeparator + that.selector.name.toString());

            if (definitions.get(fullName) != null) {
                log.error(that.pos, "javafx.duplicate.operation.in.module", that.selector.className.toString() + nameSeparator + that.selector.name.toString());
            } else {
                definitions.put(fullName, that);
            }

            JCTree decl = declarations.get(fullName);
            if (decl != null) {
                JFXRetroOperationMemberDeclaration operDecl = (JFXRetroOperationMemberDeclaration)decl;
                if (operDecl.getTag() != JavafxTag.RETROOPERATIONDECL) {
                    log.error(that.pos, "javafx.no.oper.declaration", that.selector.name.toString(), that.selector.className.toString());
                } else {
                    if (operDecl.retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.oper.definition", that.selector.name.toString(), that.selector.className.toString());
                    }

                    operDecl.retroDefinition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.oper.declaration",
                                that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = operDecl;
                }
            }

            super.visitRetroOperationDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroFunctionDefinition(JFXRetroFunctionMemberDefinition that) {
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
                if (funcDecl.getTag() != JavafxTag.RETROFUNCTIONDECL) {
                    log.error(that.pos, "javafx.no.func.declaration",
                            that.selector.name.toString(), that.selector.className.toString());
                }
                else {
                    if (((JFXRetroFunctionMemberDeclaration)funcDecl).retroDefinition != null) {
                        log.error(that.pos, "javafx.duplicate.func.definition", that.selector.name.toString(), that.selector.className.toString());
                    }

                    ((JFXRetroFunctionMemberDeclaration) funcDecl).retroDefinition = that;
                    if (that.declaration != null) {
                        log.error(that.pos, "javafx.duplicate.func.declaration",
                                that.selector.name.toString(), that.selector.className.toString());
                    }
                    that.declaration = (JFXRetroFunctionMemberDeclaration) funcDecl;
                }
            }

            super.visitRetroFunctionDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroOperationLocalDefinition(JFXRetroOperationLocalDefinition that) {
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

            super.visitRetroOperationLocalDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }

    @Override
    public void visitRetroFunctionLocalDefinition(JFXRetroFunctionLocalDefinition that) {
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

            super.visitRetroFunctionLocalDefinition(that);
        } finally {
            currentOwner = prev;
        }
    }
}
