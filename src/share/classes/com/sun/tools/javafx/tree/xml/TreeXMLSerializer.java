/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.tree.xml;

import com.sun.javafx.api.tree.AssignmentTree;
import com.sun.javafx.api.tree.BinaryTree;
import com.sun.javafx.api.tree.BlockExpressionTree;
import com.sun.javafx.api.tree.BreakTree;
import com.sun.javafx.api.tree.CatchTree;
import com.sun.javafx.api.tree.ClassDeclarationTree;
import com.sun.javafx.api.tree.CompoundAssignmentTree;
import com.sun.javafx.api.tree.ConditionalExpressionTree;
import com.sun.javafx.api.tree.ContinueTree;
import com.sun.javafx.api.tree.EmptyStatementTree;
import com.sun.javafx.api.tree.ErroneousTree;
import com.sun.javafx.api.tree.ExpressionTree;
import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.javafx.api.tree.ForExpressionTree;
import com.sun.javafx.api.tree.FunctionDefinitionTree;
import com.sun.javafx.api.tree.FunctionInvocationTree;
import com.sun.javafx.api.tree.FunctionValueTree;
import com.sun.javafx.api.tree.IdentifierTree;
import com.sun.javafx.api.tree.ImportTree;
import com.sun.javafx.api.tree.IndexofTree;
import com.sun.javafx.api.tree.InitDefinitionTree;
import com.sun.javafx.api.tree.InstanceOfTree;
import com.sun.javafx.api.tree.InstantiateTree;
import com.sun.javafx.api.tree.InterpolateValueTree;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.KeyFrameLiteralTree;
import com.sun.javafx.api.tree.LiteralTree;
import com.sun.javafx.api.tree.MemberSelectTree;
import com.sun.javafx.api.tree.ModifiersTree;
import com.sun.javafx.api.tree.ObjectLiteralPartTree;
import com.sun.javafx.api.tree.OnReplaceTree;
import com.sun.javafx.api.tree.ParenthesizedTree;
import com.sun.javafx.api.tree.ReturnTree;
import com.sun.javafx.api.tree.SequenceDeleteTree;
import com.sun.javafx.api.tree.SequenceEmptyTree;
import com.sun.javafx.api.tree.SequenceExplicitTree;
import com.sun.javafx.api.tree.SequenceIndexedTree;
import com.sun.javafx.api.tree.SequenceInsertTree;
import com.sun.javafx.api.tree.SequenceRangeTree;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.javafx.api.tree.StringExpressionTree;
import com.sun.javafx.api.tree.ThrowTree;
import com.sun.javafx.api.tree.TimeLiteralTree;
import com.sun.javafx.api.tree.Tree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.javafx.api.tree.TriggerTree;
import com.sun.javafx.api.tree.TryTree;
import com.sun.javafx.api.tree.TypeAnyTree;
import com.sun.javafx.api.tree.TypeArrayTree;
import com.sun.javafx.api.tree.TypeCastTree;
import com.sun.javafx.api.tree.TypeClassTree;
import com.sun.javafx.api.tree.TypeFunctionalTree;
import com.sun.javafx.api.tree.TypeTree;
import com.sun.javafx.api.tree.TypeUnknownTree;
import com.sun.javafx.api.tree.UnaryTree;
import com.sun.javafx.api.tree.UnitTree;
import com.sun.javafx.api.tree.VariableInvalidateTree;
import com.sun.javafx.api.tree.VariableTree;
import com.sun.javafx.api.tree.WhileLoopTree;
import com.sun.javafx.runtime.Entry;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.Position;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.JFXClassDeclaration;
import com.sun.tools.javafx.tree.JFXObjectLiteralPart;
import com.sun.tools.javafx.tree.JFXFunctionDefinition;
import com.sun.tools.javafx.tree.JFXIdent;
import com.sun.tools.javafx.tree.JFXModifiers;
import com.sun.tools.javafx.tree.JFXOnReplace;
import com.sun.tools.javafx.tree.JFXScript;
import com.sun.tools.javafx.tree.JFXSelect;
import com.sun.tools.javafx.tree.JFXSequenceSlice;
import com.sun.tools.javafx.tree.JFXStringExpression;
import com.sun.tools.javafx.tree.JFXTree;
import com.sun.tools.javafx.tree.JFXVar;
import com.sun.tools.javafx.tree.JFXVarScriptInit;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.tools.JavaFileObject;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import static javax.xml.XMLConstants.NULL_NS_URI;
import static com.sun.tools.javafx.tree.xml.Constants.*;

/**
 * This visitor that outputs SAX parser events for various Tree nodes of AST. 
 * This visitor can be used to generate XML representation of the AST.
 *
 * @author A. Sundararajan
 */
final class TreeXMLSerializer implements JavaFXTreeVisitor<Void, Void> {
    
    public Void visitMethodInvocation(FunctionInvocationTree invoke, Void v) {
        startElement(INVOKE, invoke);
        emitTree(METHOD, invoke.getMethodSelect());
        emitTreeList(ARGUMENTS, invoke.getArguments());
        endElement(INVOKE);
        return null;
    }

    
    public Void visitAssignment(AssignmentTree at, Void v) {
        startElement(ASSIGNMENT, at);
        emitTree(LEFT, at.getVariable());
        emitTree(RIGHT, at.getExpression());
        endElement(ASSIGNMENT);
        return null;
    }

    
    public Void visitCompoundAssignment(CompoundAssignmentTree cat, Void v) {
        final String tagName = enumToName(cat.getJavaFXKind());
        startElement(tagName, cat);
        emitTree(LEFT, cat.getVariable());
        emitTree(RIGHT, cat.getExpression());
        endElement(tagName);
        return null;
    }

    
    public Void visitBinary(BinaryTree bt, Void v) {
        final String tagName = enumToName(bt.getJavaFXKind());
        startElement(tagName, bt);
        emitTree(LEFT, bt.getLeftOperand());
        emitTree(RIGHT, bt.getRightOperand());
        endElement(tagName);
        return null;
    }

    
    public Void visitBreak(BreakTree bt, Void v) {
        startElement(BREAK, bt);
        Name label = bt.getLabel();
        if (label != null) {
            emitElement(LABEL, label.toString());
        }
        endElement(BREAK);
        return null;
    }

    
    public Void visitCatch(CatchTree ct, Void v) {
        startElement(CATCH, ct);
        emitTree(ct.getParameter());
        emitTree(ct.getBlock());
        endElement(CATCH);
        return null;
    }

    
    public Void visitConditionalExpression(ConditionalExpressionTree cet, Void v) {
        startElement(IF, cet);
        emitTree(TEST, cet.getCondition());
        emitTree(THEN, cet.getTrueExpression());
        emitTree(ELSE, cet.getFalseExpression());
        endElement(IF);
        return null;
    }
    
    
    public Void visitContinue(ContinueTree ct, Void v) {
        startElement(CONTINUE, ct);
        Name label = ct.getLabel();
        if (label != null) {
            emitElement(LABEL, label.toString());
        }
        endElement(CONTINUE);
        return null;
    }

    
    public Void visitErroneous(ErroneousTree et, Void v) {
        startElement(ERROR, et);
        emitTreeList(et.getErrorTrees());
        endElement(ERROR);
        return null;
    }

    
    public Void visitIdentifier(IdentifierTree ident, Void v) {
        startElement(IDENTIFIER, ident, ((JFXIdent) ident).sym);
        Name name = ident.getName();
        if (name != null) {
            emitData(name.toString());
        }
        endElement(IDENTIFIER);
        return null;
    }

    
    public Void visitImport(ImportTree imp, Void v) {
        startElement(IMPORT, imp);
        emitTree(imp.getQualifiedIdentifier());
        endElement(IMPORT);
        return null;
    }

    
    public Void visitLiteral(LiteralTree lt, Void v) {
        String tagName;
        JavaFXKind kind = lt.getJavaFXKind();
        switch (kind) {
            case INT_LITERAL:
                tagName = INT_LITERAL;
                break;
            case LONG_LITERAL:
                tagName = LONG_LITERAL;
                break;
            case FLOAT_LITERAL:
                tagName = FLOAT_LITERAL;
                break;
            case DOUBLE_LITERAL:
                tagName = DOUBLE_LITERAL;
                break;
            case BOOLEAN_LITERAL:
                tagName = Boolean.TRUE.equals(lt.getValue()) ? TRUE : FALSE;
                break;
            case STRING_LITERAL:
                tagName = STRING_LITERAL;
                break;
            case NULL_LITERAL:
                tagName = NULL;
                break;
            default:
                throw new IllegalArgumentException("unknown literal kind : " + kind);
        }
        startElement(tagName, lt);
        Object value = lt.getValue();
        if (value != null && !(value instanceof Boolean)) {
            emitData(value.toString());
        }
        endElement(tagName);
        return null;
    }

    
    public Void visitModifiers(ModifiersTree mt, Void v) {
        emitModifiers((JFXModifiers)mt);
        return null;
    }

    
    public Void visitParenthesized(ParenthesizedTree pt, Void v) {
        startElement(PARENTHESIS, pt);
        emitTree(pt.getExpression());
        endElement(PARENTHESIS);
        return null;
    }

    
    public Void visitReturn(ReturnTree rt, Void v) {
        startElement(RETURN, rt);
        emitTree(rt.getExpression());
        endElement(RETURN);
        return null;
    }
    
    
    public Void visitMemberSelect(MemberSelectTree mst, Void v) {
        startElement(SELECT, mst, ((JFXSelect) mst).sym);
        emitTree(EXPRESSION, mst.getExpression());
        Name name = mst.getIdentifier();
        if (name != null) {
            emitElement(MEMBER, name.toString());
        }
        endElement(SELECT);
        return null;
    }

    
    public Void visitEmptyStatement(EmptyStatementTree e, Void v) {
        startElement(EMPTY, e);
        endElement(EMPTY);
        return null;
    }

    
    public Void visitThrow(ThrowTree tt, Void v) {
        startElement(THROW, tt);
        emitTree(tt.getExpression());
        endElement(THROW);
        return null;
    }

    
    public Void visitCompilationUnit(UnitTree tree, Void v) {
        JFXScript script = (JFXScript) tree;
        endPositions = script.endPositions;
        startElement(JAVAFX_SCRIPT, script);
        JavaFileObject file = script.getSourceFile();
        if (file != null) {
            sourceFileName = new File(file.getName()).getName();
            int extIndex = sourceFileName.indexOf(FILE_EXT);
            if (extIndex != -1) {
                sourceFileName = sourceFileName.substring(0, extIndex);
            }
            emitElement(FILE, file.toString());
        }
        docComments = script.docComments;
        emitTree(PACKAGE, script.getPackageName());
        startElement(DEFINITIONS);
        emitTreeList(script.defs);
        if (javafxEntryMethod != null) {
            insideJavafxEntryMethod = true;
            try {
                emitTree(javafxEntryMethod.getBodyExpression());
            } finally {
                insideJavafxEntryMethod = false;
            }
        }
        endElement(DEFINITIONS);
        emitAllSymbols();
        emitAllTypes();
        endElement(JAVAFX_SCRIPT);
        return null;
    }

    
    public Void visitTry(TryTree tt, Void v) {
        startElement(TRY, tt);
        emitTree(tt.getBlock());
        emitTreeList(CATCHES, tt.getCatches());
        emitTree(FINALLY, tt.getFinallyBlock());
        endElement(TRY);
        return null;
    }

    
    public Void visitTypeCast(TypeCastTree tct, Void v) {
        startElement(CAST, tct);
        emitTree(TYPE, tct.getType());
        emitTree(EXPRESSION, tct.getExpression());
        endElement(CAST);
        return null;
    }

    
    public Void visitInstanceOf(InstanceOfTree it, Void v) {
        startElement(INSTANCEOF, it);
        emitTree(TYPE, it.getType());
        emitTree(EXPRESSION, it.getExpression());
        endElement(INSTANCEOF);
        return null;
    }

    
    public Void visitUnary(UnaryTree ut, Void v) {
        JavaFXKind kind = ut.getJavaFXKind();
        final String tagName = (kind == null) ? SIZEOF : enumToName(ut.getJavaFXKind());
        startElement(tagName, ut);
        emitTree(ut.getExpression());
        endElement(tagName);
        return null;
    }

    
    public Void visitVariable(VariableTree vt, Void v) {
        JFXVar jfxVar = (vt instanceof JFXVar) ? (JFXVar) vt : ((JFXVarScriptInit) vt).getVar();
        JFXModifiers mods = jfxVar.getModifiers();
        String tagName = VAR;
        if (mods != null) {
            // ignore static variables inside "run" method
            if (insideJavafxEntryMethod && (mods.flags & Flags.STATIC) != 0) {
                return null;
            }
            if ((mods.flags & JavafxFlags.IS_DEF) != 0) {
                tagName = DEF;
            }
        }
        startElement(tagName, vt, jfxVar.sym);
        Name name = vt.getName();
        if (name != null) {
            emitElement(NAME, name.toString());
        }
        emitModifiers(mods);
        emitTree(TYPE, vt.getJFXType());
        emitElement(BIND_STATUS, bindStatusToString(vt.getBindStatus()));
        emitTree(INITIAL_VALUE, vt.getInitializer());
        OnReplaceTree onReplace = vt.getOnReplaceTree();
        emitTree(onReplace);
        OnReplaceTree onInvalidate = vt.getOnInvalidateTree();
        emitTree(onInvalidate);
        endElement(tagName);
        return null;
    }

    
    public Void visitWhileLoop(WhileLoopTree wl, Void v) {
        startElement(WHILE, wl);
        emitTree(TEST, wl.getCondition());
        emitTree(STATEMENT, wl.getStatement());
        endElement(WHILE);
        return null;
    }

    
    public Void visitBlockExpression(BlockExpressionTree be, Void v) {
        startElement(BLOCK_EXPRESSION, be);
        emitTreeList(STATEMENTS, be.getStatements());
        // emitTree(VALUE, be.getValue());
        endElement(BLOCK_EXPRESSION);
        return null;
    }

    
    public Void visitClassDeclaration(ClassDeclarationTree tree, Void v) {
        JFXClassDeclaration jfxCt = (JFXClassDeclaration) tree;
        List<JFXTree> members = jfxCt.getMembers();
        List<JFXTree> staticMembers = new ArrayList<JFXTree>();
        List<JFXTree> instanceMembers = new ArrayList<JFXTree>();
        for (JFXTree m : members) {
            if (m instanceof JFXFunctionDefinition) {
                JFXFunctionDefinition func = (JFXFunctionDefinition)m;
                if (javafxEntryMethodName.equals(func.getName().toString())) {
                    javafxEntryMethod = func;
                }
                if (func.getModifiers().getFlags().contains(Modifier.STATIC)) {
                    staticMembers.add(m);
                } else {
                    instanceMembers.add(m);
                }
            } else if (m instanceof JFXVar) {
                JFXVar var = (JFXVar)m;
                if (var.getModifiers().getFlags().contains(Modifier.STATIC)) {
                    staticMembers.add(m);
                } else {
                    instanceMembers.add(m);
                }
            } else if (m instanceof JFXClassDeclaration) {
                staticMembers.add(m);
            } else {
                // add anything else to instance members list
                instanceMembers.add(m);
            }
        }

        // no instance member => this is a module class generated
        // to hold file level variables and functions.
        if (instanceMembers.isEmpty()) {
            emitTreeList(jfxCt.getMembers());
        } else {
            // emit static members that appear before the class in source order
            int classPos = jfxCt.pos;
            for (JCTree item : staticMembers) {
                if (item.pos <= classPos) {
                    emitTree((Tree)item);
                }
            }
            startElement(CLASS, jfxCt, jfxCt.sym);
            Name name = jfxCt.getSimpleName();
            if (name != null) {
                emitElement(NAME, name.toString());
            }
            emitModifiers(jfxCt.getModifiers());
            emitTreeList(EXTENDS, jfxCt.getSupertypeList());
            emitTreeList(MEMBERS, instanceMembers);
            endElement(CLASS);
            // emit static members that appear after the class in source order
            for (JCTree item : staticMembers) {
                if (item.pos > classPos) {
                    emitTree((Tree)item);
                }
            }
        }
        return null;
    }

    
    public Void visitForExpression(ForExpressionTree fe, Void v) {
        startElement(FOR, fe);
        emitTreeList(IN, fe.getInClauses());
        emitTree(BODY, fe.getBodyExpression());
        endElement(FOR);
        return null;
    }

    
    public Void visitForExpressionInClause(ForExpressionInClauseTree feic, Void v) {
        startElement(LIST_ITEM, feic);
        emitTree(feic.getVariable());
        emitTree(SEQUENCE, feic.getSequenceExpression());
        emitTree(WHERE, feic.getWhereExpression());
        endElement(LIST_ITEM);
        return null;
    }

    
    public Void visitInitDefinition(InitDefinitionTree id, Void v) {
        startElement(INIT, id);
        emitTree(id.getBody());
        endElement(INIT);
        return null;
    }

    
    public Void visitInterpolateValue(InterpolateValueTree ivt, Void v) {
        startElement(INTERPOLATE_VALUE, ivt);
        emitTree(ATTRIBUTE, ivt.getAttribute());
        emitTree(VALUE, ivt.getValue());
        System.out.println("value is " + ivt.getValue());
        emitTree(INTERPOLATION, ivt.getInterpolation());
        endElement(INTERPOLATE_VALUE);
        return null;
    }

    
    public Void visitIndexof(IndexofTree it, Void v) {
        startElement(INDEXOF, it);
        emitTree(it.getForVarIdentifier());
        endElement(INDEXOF);
        return null;
    }

    
    public Void visitInstantiate(InstantiateTree tree, Void v) {
        List<ExpressionTree> args = tree.getArguments();
        final String tagName = ((args == null) || args.isEmpty())? OBJECT_LITERAL : NEW;
        startElement(tagName, tree);
        emitTree(CLASS, tree.getIdentifier());
        emitTreeList(ARGUMENTS, tree.getArguments());
        startElement(DEFINITIONS);
        emitTreeList(tree.getLocalVariables());
        emitTreeList(tree.getLiteralParts());
        ClassDeclarationTree clazz = tree.getClassBody();
        if (clazz != null) {
            emitTreeList(clazz.getClassMembers());
        }
        endElement(DEFINITIONS);
        endElement(tagName);
        return null;
    }

    
    public Void visitKeyFrameLiteral(KeyFrameLiteralTree kfl, Void v) {
        startElement(KEYFRAME_LITERAL, kfl);
        emitTree(START_DURATION, kfl.getStartDuration());
        emitTreeList(INTERPOLATION_VALUES, kfl.getInterpolationValues());
        emitTree(TRIGGER, kfl.getTrigger());
        endElement(KEYFRAME_LITERAL);
        return null;
    }

    
    public Void visitObjectLiteralPart(ObjectLiteralPartTree olp, Void v) {
        startElement(OBJECT_LITERAL_INIT, olp, ((JFXObjectLiteralPart) olp).sym);
        Name name = olp.getName();
        if (name != null) {
            emitElement(NAME, name.toString());
        }
        emitElement(BIND_STATUS, bindStatusToString(olp.getBindStatus()));
        emitTree(EXPRESSION, olp.getExpression());
        endElement(OBJECT_LITERAL_INIT);
        return null;
    }

    
    public Void visitOnReplace(OnReplaceTree or, Void v) {
        final String tagName =
            (((JFXOnReplace)or).getTriggerKind() == JFXOnReplace.Kind.ONINVALIDATE)?
            ON_INVALIDATE : ON_REPLACE;
        startElement(tagName, or);
        emitTree(FIRST_INDEX, or.getFirstIndex());
        emitTree(LAST_INDEX, or.getLastIndex());
        emitTree(NEW_ELEMENTS, or.getNewElements());
        emitTree(OLD_VALUE, or.getOldValue());
        if (or.getEndKind() == JFXSequenceSlice.END_EXCLUSIVE) {
            emitElement(SLICE_END_KIND, EXCLUSIVE);
        }
        emitTree(or.getBody());
        endElement(tagName);
        return null;
    }

    
    public Void visitFunctionDefinition(FunctionDefinitionTree tree, Void v) {
        JFXFunctionDefinition ot = (JFXFunctionDefinition) tree;
        if (ot.equals(javafxEntryMethod)) {
            // handled specially, return from here
            return null;
        } else {
            startElement(FUNCTION, ot, ot.sym);
            Name name = ot.getName();
            if (name != null) {
                emitElement(NAME, name.toString());
            }
            emitModifiers(ot.getModifiers());
            emitTree(RETURN_TYPE, ot.getJFXReturnType());
            emitTreeList(PARAMETERS, ot.getParams());
            emitTree(ot.getBodyExpression());
            endElement(FUNCTION);
        }
        return null;
    }
    
    
    public Void visitFunctionValue(FunctionValueTree fv, Void v) {
        startElement(ANON_FUNCTION, fv);
        emitTree(RETURN_TYPE, fv.getType());
        emitTreeList(PARAMETERS, fv.getParameters());
        emitTree(fv.getBodyExpression());
        endElement(ANON_FUNCTION);
        return null;
    }

    
    public Void visitPostInitDefinition(InitDefinitionTree pid, Void v) {
        startElement(POSTINIT, pid);
        emitTree(pid.getBody());
        endElement(POSTINIT);
        return null;
    }

    
    public Void visitSequenceDelete(SequenceDeleteTree sd, Void v) {
        startElement(SEQUENCE_DELETE, sd);
        emitTree(SEQUENCE, sd.getSequence());
        emitTree(ELEMENT, sd.getElement());
        endElement(SEQUENCE_DELETE);
        return null;
    }

    
    public Void visitSequenceEmpty(SequenceEmptyTree se, Void v) {
        startElement(SEQUENCE_EMPTY, se);
        endElement(SEQUENCE_EMPTY);
        return null;
    }

    
    public Void visitSequenceExplicit(SequenceExplicitTree se, Void v) {
        startElement(SEQUENCE_EXPLICIT, se);
        emitTreeList(ITEMS, se.getItemList());
        endElement(SEQUENCE_EXPLICIT);
        return null;
    }

    
    public Void visitSequenceIndexed(SequenceIndexedTree si, Void v) {
        startElement(SEQUENCE_INDEXED, si);
        emitTree(SEQUENCE, si.getSequence());
        emitTree(INDEX, si.getIndex());
        endElement(SEQUENCE_INDEXED);
        return null;
    }

    
    public Void visitSequenceSlice(SequenceSliceTree ss, Void v) {
        startElement(SEQUENCE_SLICE, ss);
        emitTree(SEQUENCE, ss.getSequence());
        emitTree(FIRST, ss.getFirstIndex());
        emitTree(LAST, ss.getLastIndex());
        if (ss.getEndKind() == ss.END_EXCLUSIVE) {
            emitElement(SLICE_END_KIND, EXCLUSIVE);
        }
        endElement(SEQUENCE_SLICE);
        return null;
    }

    
    public Void visitSequenceInsert(SequenceInsertTree si, Void v) {
        startElement(SEQUENCE_INSERT, si);
        emitTree(SEQUENCE, si.getSequence());
        emitTree(ELEMENT, si.getElement());
        endElement(SEQUENCE_INSERT);
        return null;
    }

    
    public Void visitSequenceRange(SequenceRangeTree sr, Void v) {
        startElement(SEQUENCE_RANGE, sr);
        emitTree(LOWER, sr.getLower());
        emitTree(UPPER, sr.getUpper());
        emitTree(STEP, sr.getStepOrNull());
        emitElement(EXCLUSIVE, Boolean.toString(sr.isExclusive()));
        endElement(SEQUENCE_RANGE);
        return null;
    }


    public Void visitVariableInvalidate(VariableInvalidateTree it, Void v) {
        startElement(INVALIDATE, it);
        startElement(VAR);
        emitTree(it.getVariable());
        endElement(VAR);
        endElement(INVALIDATE);
        return null;
    }

    
    public Void visitStringExpression(StringExpressionTree se, Void v) {
        startElement(STRING_EXPRESSION, se);
        String translationKey = ((JFXStringExpression) se).translationKey;
        if (translationKey != null) {
            emitElement(STR_TRANS_KEY, translationKey);
        }
        List<ExpressionTree> parts = se.getPartList();
        int i;
        for (i = 0; i < parts.size() - 1; i += 3) {
            emitTree(PART, parts.get(i));
            startElement(PART);
            ExpressionTree format = parts.get(i + 1);
            if (format != null) {
                emitTree(FORMAT, format);
            }
            emitTree(EXPRESSION, parts.get(i + 2));
            endElement(PART);
        }
        emitTree(PART, parts.get(i));
        endElement(STRING_EXPRESSION);
        return null;
    }
    
    
    public Void visitTimeLiteral(TimeLiteralTree tt, Void v) {
        startElement(TIME_LITERAL, tt);
        emitData(tt.getValue().toString());
        endElement(TIME_LITERAL);
        return null;
    }

    
    public Void visitTrigger(TriggerTree tt, Void v) {
        startElement(OVERRIDE_VAR, tt);
        emitTree(EXPRESSION, tt.getExpressionTree());
        emitTree(tt.getOnReplaceTree());
        endElement(OVERRIDE_VAR);
        return null;
    }

    
    public Void visitTypeAny(TypeAnyTree at, Void v) {
        startElement(TYPE_ANY, at);
        TypeTree.Cardinality cardinality = ((TypeTree) at).getCardinality();
        emitElement(CARDINALITY, cardinalityToString(cardinality));
        endElement(TYPE_ANY);
        return null;
    }

    
    public Void visitTypeClass(TypeClassTree tc, Void v) {
        startElement(TYPE_CLASS, tc, getSymbolField(tc));
        emitTree(CLASS, tc.getClassName());
        TypeTree.Cardinality cardinality = ((TypeTree) tc).getCardinality();
        emitElement(CARDINALITY, cardinalityToString(cardinality));
        endElement(TYPE_CLASS);
        return null;
    }

    
    public Void visitTypeFunctional(TypeFunctionalTree tf, Void v) {
        startElement(TYPE_FUNCTIONAL, tf);
        emitTreeList(PARAMETERS, tf.getParameters());
        emitTree(RETURN_TYPE, (com.sun.tools.javafx.tree.JFXType) tf.getReturnType());
        TypeTree.Cardinality cardinality = ((TypeTree) tf).getCardinality();
        emitElement(CARDINALITY, cardinalityToString(cardinality));
        endElement(TYPE_FUNCTIONAL);
        return null;
    }

    
    public Void visitTypeArray(TypeArrayTree tat, Void v) {
        startElement(TYPE_ARRAY, tat);
        emitTree(tat.getElementType());
        endElement(TYPE_ARRAY);
        return null;
    }

    
    public Void visitTypeUnknown(TypeUnknownTree tu, Void v) {
        startElement(TYPE_UNKNOWN, tu);
        endElement(TYPE_UNKNOWN);
        return null;
    }

    
    public Void visitMissingExpression(ExpressionTree expr, Void v) {
        startElement(MISSING_EXPRESSION, expr);
        emitTree(expr);
        endElement(MISSING_EXPRESSION);
        return null;
    }

    // package private stuff below this point

    // accepts SAX content handler on which SAX events are called
    TreeXMLSerializer(ContentHandler handler) {
        this.handler = handler;
        this.javafxEntryMethodName = Entry.entryMethodName();
    }

    // start outputting SAX events based on given compilation unit tree
    void start(UnitTree ut) {
        try {
            handler.startDocument();
            ut.accept(this, null);
            handler.endDocument();
        } catch (Exception exp) {
            throw wrapException(exp);
        }
    }

    Symbol idToSymbol(String id) {
        return idToSymbol.get(id);
    }

    Type idToType(String id) {
        return idToType.get(id);
    }

    //-- Internals only below this point

    private Map<JCTree, String> docComments;

    /*
     * Symbols and types are networks (and not trees). We handle cycles by
     * generating id and idrefs (as is common in XML representations). Note
     * that the symbols and types are emitted only if XML representation is
     * created after "enter" or "analyze" phase. If XML document is created
     * just after "parse" phase, we emit only the tree nodes.
     */
    // next symbol id to be used -- symbol id is just a common
    // prefix concatenated with a number
    private int nextSymbol = 1;

    // Symbol to symbol id map
    private Map<Symbol, String> symbolToId = new HashMap<Symbol, String>();
    private Map<String, Symbol> idToSymbol = new HashMap<String, Symbol>();

    // next type id to be used --  type id is just a common
    // prefix concatenated with a number
    private int nextType = 1;
    // Type to type id map
    private Map<Type, String> typeToId = new HashMap<Type, String>();
    private Map<String, Type> idToType = new HashMap<String, Type>();

    private final AttributesImpl attrs = new AttributesImpl();

    // SAX sink to output SAX events
    private ContentHandler handler;

    // end positions map of the current compilation unit
    private Map<JCTree, Integer> endPositions;
    private String javafxEntryMethodName;
    private JFXFunctionDefinition javafxEntryMethod;
    private boolean insideJavafxEntryMethod;
    private String sourceFileName;

    private Symbol getSymbolField(Tree jcTree) {
        try {
            // Only few JCTree subclasses have "sym" field.
            // So, we need to use reflection to access the same.
            Field field = jcTree.getClass().getDeclaredField("sym");
            field.setAccessible(true);
            return (Symbol) field.get(jcTree);
        } catch (Exception exp) {
            throw wrapException(exp);
        }
    }

    // put Symbol into symbol map and return id
    private String putSymbol(Symbol sym) {
        if (symbolToId.containsKey(sym)) {
            return symbolToId.get(sym);
        }
        String id = SYMID_PREFIX + nextSymbol;
        symbolToId.put(sym, id);
        idToSymbol.put(id, sym);
        Type type = sym.asType();
        if (type != null) {
            putType(type);
        }
        nextSymbol++;
        Symbol owner = sym.getEnclosingElement();
        if (owner != null) {
            putSymbol(owner);
        }
        return id;
    }

    // put Type into type map and return id
    private String putType(Type type) {
        if (typeToId.containsKey(type)) {
            return typeToId.get(type);
        }
        String id = TYPEID_PREFIX + nextType;
        typeToId.put(type, id);
        idToType.put(id, type);
        nextType++;
        return id;
    }

    // emit all symbol elements
    private void emitAllSymbols() {
        if (symbolToId.isEmpty()) {
            return;
        }
        startElement(SYMBOLS);
        for (Map.Entry<Symbol, String> entry : symbolToId.entrySet()) {
            Symbol sym = entry.getKey();
            Type type = sym.asType();
            attrs.clear();
            attrs.addAttribute(NULL_NS_URI, ID, ID, ATTR_ID, entry.getValue());
            if (type != null && typeToId.containsKey(type)) {
                String ref = typeToId.get(type);
                attrs.addAttribute(NULL_NS_URI, TYPEREF, TYPEREF, ATTR_IDREF, ref);
            }
            startElement(SYMBOL, attrs);
            Name name = sym.getSimpleName();
            if (name != null) {
                emitElement(NAME, name.toString());
                Name qualifiedName = sym.getQualifiedName();
                if (qualifiedName != null && !name.equals(qualifiedName)) {
                    emitElement(FULL_NAME, qualifiedName.toString());
                }
            }
            emitElement(KIND, enumToName(sym.getKind()));
            startElement(MODIFIERS);
            emitFlags(sym.flags());
            endElement(MODIFIERS);
            attrs.clear();
            Symbol owner = sym.getEnclosingElement();
            if (owner != null) {
                attrs.addAttribute(NULL_NS_URI, SYMREF, SYMREF, ATTR_IDREF, symbolToId.get(owner));
                startElement(OWNER, attrs);
                endElement(OWNER);
            }
            endElement(SYMBOL);
        }
        endElement(SYMBOLS);
    }

    // emit all type elements
    private void emitAllTypes() {
        if (typeToId.isEmpty()) {
            return;
        }
        startElement(TYPES);
        for (Map.Entry<Type, String> entry : typeToId.entrySet()) {
            attrs.clear();
            Type type = entry.getKey();
            String id = entry.getValue();
            attrs.addAttribute(NULL_NS_URI, ID, ID, ATTR_ID, entry.getValue());
            startElement(TYPE, attrs);
            emitElement(NAME, type.toString());
            emitElement(KIND, enumToName(type.getKind()));
            endElement(TYPE);
        }
        endElement(TYPES);
    }

    private void startElement(String name, Tree t) {
        startElement(name, t, null);
    }

    private void startElement(String name, Tree t, Symbol sym) {
        JCTree jcTree = (JCTree) t;
        attrs.clear();
        if (sym != null) {
            String ref = putSymbol(sym);
            attrs.addAttribute(NULL_NS_URI, SYMREF, SYMREF, ATTR_IDREF, ref);
        }

        Type type = jcTree.type;
        if (type != null) {
            String ref = putType(type);
            attrs.addAttribute(NULL_NS_URI, TYPEREF, TYPEREF, ATTR_IDREF, ref);
        }

        if (jcTree.pos != Position.NOPOS) {
            attrs.addAttribute(NULL_NS_URI, POSITION, POSITION, ATTR_CDATA, Integer.toString(jcTree.pos));
        }
        if (endPositions != null) {
            int endPos = jcTree.getEndPosition(endPositions);
            if (endPos != Position.NOPOS) {
                attrs.addAttribute(NULL_NS_URI, END_POSITION, END_POSITION, ATTR_CDATA, Integer.toString(endPos));
            }
        }
        startElement(name, attrs);
        if (docComments != null && docComments.containsKey(t)) {
            emitElement(DOC_COMMENT, docComments.get(t));
        }
    }

    private void startElement(String element) {
        attrs.clear();
        startElement(element, attrs);
    }

    private void startElement(String element, Attributes attrs) {
        try {
            handler.startElement(JFXASTXML_NS, element, JFXASTXML_PREFIX + element, attrs);
        } catch (Exception exp) {
            throw wrapException(exp);
        }
    }

    private void endElement(String element) {
        try {
            handler.endElement(JFXASTXML_NS, element, JFXASTXML_PREFIX + element);
        } catch (Exception exp) {
            throw wrapException(exp);
        }
    }

    private void emitData(String data) {
        if (data == null) {
            return;
        }
        char[] chars = data.toCharArray();
        try {
            handler.characters(chars, 0, chars.length);
        } catch (Exception exp) {
            throw wrapException(exp);
        }
    }

    private void emitElement(String name, String d) {
        if (d != null) {
            startElement(name);
            emitData(d);
            endElement(name);
        }
    }

    private void emitModifiers(JFXModifiers mods) {
        if (mods != null) {
            startElement(MODIFIERS, mods);
            emitFlags(mods.flags);
            endElement(MODIFIERS);
        }
    }
    
    private void emitFlags(long flagBits) {
        // Java flags applicable here
        if ((flagBits & Flags.ABSTRACT) != 0) {
            emitListItem(ABSTRACT);
        }
        if ((flagBits & Flags.PROTECTED) != 0) {
            emitListItem(PROTECTED);
        }
        if ((flagBits & Flags.PUBLIC) != 0) {
            emitListItem(PUBLIC);
        }
            
        // Now handle JavaFX specific flags
        if ((flagBits & JavafxFlags.PUBLIC_INIT) != 0) {
            emitListItem(PUBLIC_INIT);
        }
        if ((flagBits & JavafxFlags.PUBLIC_READ) != 0) {
            emitListItem(PUBLIC_READ);
        }
        if ((flagBits & JavafxFlags.PACKAGE_ACCESS) != 0) {
            emitListItem(PACKAGE_ACCESS);
        }
        if ((flagBits & JavafxFlags.SCRIPT_PRIVATE) != 0) {
            emitListItem(SCRIPT_PRIVATE);
        }
        if ((flagBits & JavafxFlags.OVERRIDE) != 0) {
            emitListItem(OVERRIDE);
        }
        if ((flagBits & JavafxFlags.MIXIN) != 0) {
            emitListItem(MIXIN);
        }
        if ((flagBits & JavafxFlags.BOUND) != 0) {
            emitListItem(BOUND);
        }
    }

    private void emitListItem(String data) {
        startElement(LIST_ITEM);
        emitData(data);
        endElement(LIST_ITEM);
    }

    private void emitTree(Tree t) {
        if (t != null) {
            t.accept(this, null);
        }
    }

    private void emitTree(String name, Tree t) {
        if (t != null) {
            if (name != null) {
                startElement(name);
            }
            t.accept(this, null);
            if (name != null) {
                endElement(name);
            }
        }
    }

    private void emitTreeList(List<? extends Tree> list) {
        emitTreeList(null, null, list);
    }

    private void emitTreeList(String name, List<? extends Tree> list) {
        emitTreeList(name, null, list);
    }

    private void emitTreeList(String name, String itemName, List<? extends Tree> list) {
        if (list != null && !list.isEmpty()) {
            if (name != null) {
                startElement(name);
            }
            for (Tree item : list) {
                emitTree(itemName, item);
            }
            if (name != null) {
                endElement(name);
            }
        }
    }

    private RuntimeException wrapException(Exception exp) {
        if (exp instanceof RuntimeException) {
            return (RuntimeException) exp;
        } else {
            return new RuntimeException(exp);
        }
    }
}
