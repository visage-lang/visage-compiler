/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

public class JavafxInitializationBuilder {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private final JavafxTreeMaker make;
    private final Name.Table names;
    
    private final Name addChangeListenerName;
    private final Name changeListenerInterfaceName;
    final Name initializerName;
    private final Name changedName;
    private final Name valueChangedName;

    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        names = Name.Table.instance(context);
        
        addChangeListenerName = names.fromString("addChangeListener");
        changeListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "ChangeListener");
        initializerName = names.fromString(JavafxModuleBuilder.initMethodString);
        changedName = names.fromString("onChange");
        valueChangedName = names.fromString("valueChanged");
    }
    
    static class TranslatedAttributeInfo {
        Name name;
        JCExpression initExpr;
        JCBlock onChangeBlock;
        TranslatedAttributeInfo(Name name, JCExpression initExpr, JCBlock onChangeBlock) {
            this.name = name;
            this.initExpr = initExpr;
            this.onChangeBlock = onChangeBlock;
        }
    }
  
    /**
     * Non-destructively build the statements to fill-in the 
     * body of a translated class initializer method.
     * Incoming info MUST be translated into Java ASTs already
     */
    List<JCStatement> initializerMethodBody(
            JFXClassDeclaration classDecl,
            List<TranslatedAttributeInfo> attrInfo, 
            List<JCBlock> initBlocks) {
        ListBuffer<JCStatement> stmts = ListBuffer.<JCStatement>lb();
 
        // Initialize the default values for the atttributes.
        for (TranslatedAttributeInfo info : attrInfo) {
            if (info.initExpr != null) { // if there is an attribute initializer
                stmts.append(makeAttributeInitialization(info));
            }
        }

        for (JCBlock init : initBlocks) {
            stmts.append(init);   // insert the init blocks directly
        }

        // Now do the on change blocks
        for (TranslatedAttributeInfo info : attrInfo) {
            if (info.onChangeBlock != null) { // if there is an on change clause
               stmts.append(makeChangeListenerCall(info));
            }
        }
        
        // Now add a call to notify changes for all attribute, so dependents 
        // initial values will be set
        makeOnChangedCall(classDecl, stmts);

        return stmts.toList();
    }  
    
    /**
     * Non-destructive creation of initialization code for an attribute
     */
    private JCStatement makeAttributeInitialization(TranslatedAttributeInfo info) {
        JCLiteral nullValue = make.Literal(TypeTags.BOT, null);
        JCIdent lhsIdent = make.Ident(info.name);
        JCBinary cond = make.Binary(JCTree.EQ, lhsIdent, nullValue);

        JCIdent lhsAssignIdent = make.Ident(info.name);
        JCAssign defValAssign = make.Assign(lhsAssignIdent, info.initExpr);
        JCExpressionStatement defAttrValue = make.Exec(defValAssign);
        return make.If(cond, defAttrValue, null);
    }
    
    /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    private JCStatement makeChangeListenerCall(TranslatedAttributeInfo info) {
        DiagnosticPosition diagPos = info.onChangeBlock.pos();
        ListBuffer<JCStatement> ocMethStmts = ListBuffer.<JCStatement>lb();
        ocMethStmts.appendList(info.onChangeBlock.stats);
        ocMethStmts.append(make.at(diagPos).Return(make.at(diagPos).Literal(TypeTags.BOOLEAN, 1)));

        // changed() method
        List<JCTree> defs = List.<JCTree>of(make.at(diagPos).MethodDef(
                make.at(diagPos).Modifiers(Flags.PUBLIC), 
                changedName, 
                make.at(diagPos).TypeIdent(TypeTags.BOOLEAN), 
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>nil(), 
                List.<JCExpression>nil(), 
                make.at(diagPos).Block(0L, ocMethStmts.toList()), 
                null));

        JCNewClass anonymousChangeListener = make.NewClass(
                null, 
                List.<JCExpression>nil(), 
                make.at(diagPos).Identifier(changeListenerInterfaceName), 
                List.<JCExpression>nil(), 
                make.at(diagPos).AnonymousClassDef(make.Modifiers(0L), defs));

        JCIdent varIdent = make.at(diagPos).Ident(info.name);
        JCFieldAccess tmpSelect = make.at(diagPos).Select(varIdent, addChangeListenerName);

        List<JCExpression> typeargs = List.nil();
        List<JCExpression> args = List.<JCExpression>of(anonymousChangeListener);
        return make.at(diagPos).Exec(make.at(diagPos).Apply(typeargs, tmpSelect, args));
    }
    
    private void makeOnChangedCall(JFXClassDeclaration classDecl,
                                    ListBuffer<JCStatement> stmts) {
        for (JCTree tree : classDecl.defs) {
            if (tree.getTag() == JavafxTag.ATTRIBUTEDEF) {
                JFXAttributeDefinition attrDef = (JFXAttributeDefinition)tree;
                DiagnosticPosition diagPos = attrDef.pos();
                JCIdent varIdent = make.at(diagPos).Ident(attrDef.name);
                JCFieldAccess tmpSelect = make.at(diagPos).Select(varIdent, valueChangedName);

                List<JCExpression> typeargs = List.nil();
                List<JCExpression> args = List.<JCExpression>nil();
                stmts = stmts.append(make.at(diagPos).Exec(make.at(diagPos).Apply(typeargs, tmpSelect, args)));
            }
        }
    }
}

