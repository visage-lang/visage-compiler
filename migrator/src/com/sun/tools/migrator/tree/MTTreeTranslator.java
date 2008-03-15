package com.sun.tools.migrator.tree;

import com.sun.tools.javac.util.*;
import com.sun.tools.migrator.tree.MTTree.*;

import com.sun.tools.javac.util.List;

import java.io.OutputStreamWriter;

public class MTTreeTranslator implements MTVisitor {

    /** Visitor result field: a tree
     */
    MTTree result = null;
    
    final MTTreeMaker make;
    
    MTTreeTranslator(MTTreeMaker make) {
        this.make = make;
    }

    /** Visitor method: translate a list of nodes.
     */
    public <T extends MTTree> List<T> translate(List<T> trees) {
	if (trees == null) return null;
        List<T> prev = null;
	for (List<T> l = trees; l.nonEmpty();) {
            T tree = translate(l.head);
            if (tree != null) {
                l.head = tree;
                prev = l;
                l = l.tail;
            } else {
                T nonNullTree = null;
                List<T> ls = null;
                for (ls = l.tail; ls.nonEmpty(); ls = ls.tail) {
                    nonNullTree = translate(ls.head);
                    if (nonNullTree != null) {
                        break;
                    }
                }
                if (nonNullTree != null) {
                    prev = l;
                    l.head = nonNullTree;
                    l.tail = ls.tail;
                    l = l.tail;
                }
                else {
                    prev.tail = ls;
                    l = ls;
                }
            }
        }
	return trees;
    }

    /**  Visitor method: translate a list of variable definitions.
     */
    public List<MTVar> translateJFXVarDefs(List<MTVar> trees) {
	for (List<MTVar> l = trees; l.nonEmpty(); l = l.tail)
	    l.head = translate(l.head);
	return trees;
    }


    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends MTTree> T translate(T tree) {
	if (tree == null) {
	    return null;
	} else {
	    tree.accept(this);
	    MTTree result = this.result;
	    this.result = null;
	    return (T)result; // XXX cast
	}
    }
    
    /**  Visitor method: translate a list of variable definitions.
     */
    public List<MTVariableDecl> translateVarDefs(List<MTVariableDecl> trees) {
	for (List<MTVariableDecl> l = trees; l.nonEmpty(); l = l.tail)
	    l.head = translate(l.head);
	return trees;
    }

    /**  Visitor method: translate a list of type parameters.
     */
    public List<MTTypeParameter> translateTypeParams(List<MTTypeParameter> trees) {
	for (List<MTTypeParameter> l = trees; l.nonEmpty(); l = l.tail)
	    l.head = translate(l.head);
	return trees;
    }

    /**  Visitor method: translate a list of catch clauses in try statements.
     */
    public List<MTCatch> translateCatchers(List<MTCatch> trees) {
	for (List<MTCatch> l = trees; l.nonEmpty(); l = l.tail)
	    l.head = translate(l.head);
	return trees;
    }

    /**  Visitor method: translate a list of catch clauses in try statements.
     */
    public List<MTAnnotation> translateAnnotations(List<MTAnnotation> trees) {
	for (List<MTAnnotation> l = trees; l.nonEmpty(); l = l.tail)
	    l.head = translate(l.head);
	return trees;
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public void visitTopLevel(MTCompilationUnit tree) {
	tree.pid = translate(tree.pid);
	tree.defs = translate(tree.defs);
	result = tree;
    }

    public void visitImport(MTImport tree) {
	tree.qualid = translate(tree.qualid);
	result = tree;
    }

    public void visitVarDef(MTVariableDecl tree) {
	tree.mods = translate(tree.mods);
	tree.vartype = translate(tree.vartype);
	tree.init = translate(tree.init);
	result = tree;
    }
	
    public void visitSkip(MTSkip tree) {
	result = tree;
    }

    public void visitBlock(MTBlock tree) {
	tree.stats = translate(tree.stats);
	result = tree;
    }

    public void visitWhileLoop(MTWhileLoop tree) {
	tree.cond = translate(tree.cond);
	tree.body = translate(tree.body);
	result = tree;
    }

    public void visitTry(MTTry tree) {
	tree.body = translate(tree.body);
	tree.catchers = translateCatchers(tree.catchers);
	tree.finalizer = translate(tree.finalizer);
	result = tree;
    }

    public void visitCatch(MTCatch tree) {
	tree.param = translate(tree.param);
	tree.body = translate(tree.body);
	result = tree;
    }

    public void visitConditional(MTConditional tree) {
	tree.cond = translate(tree.cond);
	tree.truepart = translate(tree.truepart);
	tree.falsepart = translate(tree.falsepart);
	result = tree;
    }

    public void visitIf(MTIf tree) {
	tree.cond = translate(tree.cond);
	tree.thenpart = translate(tree.thenpart);
	tree.elsepart = translate(tree.elsepart);
	result = tree;
    }

    public void visitExec(MTExpressionStatement tree) {
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitBreak(MTBreak tree) {
	result = tree;
    }

    public void visitContinue(MTContinue tree) {
	result = tree;
    }

    public void visitReturn(MTReturn tree) {
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitThrow(MTThrow tree) {
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitAssert(MTAssert tree) {
	tree.cond = translate(tree.cond);
	tree.detail = translate(tree.detail);
	result = tree;
    }

    public void visitApply(MTMethodInvocation tree) {
	tree.meth = translate(tree.meth);
	tree.args = translate(tree.args);
	result = tree;
    }

    public void visitParens(MTParens tree) {
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitAssign(MTAssign tree) {
	tree.lhs = translate(tree.lhs);
	tree.rhs = translate(tree.rhs);
	result = tree;
    }

    public void visitAssignop(MTAssignOp tree) {
	tree.lhs = translate(tree.lhs);
	tree.rhs = translate(tree.rhs);
	result = tree;
    }

    public void visitUnary(MTUnary tree) {
	tree.arg = translate(tree.arg);
	result = tree;
    }

    public void visitBinary(MTBinary tree) {
	tree.lhs = translate(tree.lhs);
	tree.rhs = translate(tree.rhs);
	result = tree;
    }

    public void visitTypeCast(MTTypeCast tree) {
	tree.clazz = translate(tree.clazz);
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitTypeTest(MTInstanceOf tree) {
	tree.expr = translate(tree.expr);
	tree.clazz = translate(tree.clazz);
	result = tree;
    }

    public void visitIndexed(MTArrayAccess tree) {
	tree.indexed = translate(tree.indexed);
	tree.index = translate(tree.index);
	result = tree;
    }

    public void visitSelect(MTFieldAccess tree) {
	tree.selected = translate(tree.selected);
	result = tree;
    }

    public void visitIdent(MTIdent tree) {
	result = tree;
    }

    public void visitLiteral(MTLiteral tree) {
	result = tree;
    }

    public void visitTypeIdent(MTPrimitiveTypeTree tree) {
	result = tree;
    }

    public void visitTypeArray(MTArrayTypeTree tree) {
	tree.elemtype = translate(tree.elemtype);
	result = tree;
    }

    public void visitTypeApply(MTTypeApply tree) {
	tree.clazz = translate(tree.clazz);
	tree.arguments = translate(tree.arguments);
	result = tree;
    }

    public void visitTypeParameter(MTTypeParameter tree) {
	tree.bounds = translate(tree.bounds);
	result = tree;
    }

    @Override
    public void visitWildcard(MTWildcard tree) {
        tree.kind = translate(tree.kind);
        tree.inner = translate(tree.inner);
        result = tree;
    }

    @Override
    public void visitTypeBoundKind(MTTypeBoundKind tree) {
        result = tree;
    }

    public void visitErroneous(MTErroneous tree) {
	result = tree;
    }

    public void visitLetExpr(MTLetExpr tree) {
	tree.defs = translateVarDefs(tree.defs);
	tree.expr = translate(tree.expr);
	result = tree;
    }

    public void visitModifiers(MTModifiers tree) {
	tree.annotations = translateAnnotations(tree.annotations);
	result = tree;
    }

    public void visitAnnotation(MTAnnotation tree) {
	tree.annotationType = translate(tree.annotationType);
	tree.args = translate(tree.args);
	result = tree;
    }

    public void visitTree(MTTree tree) {
	throw new AssertionError(tree);
    }

    public void visitClassDeclaration(MTClassDeclaration tree) {
        List<MTExpression> supertypes = translate(tree.getSupertypes());
        List<MTTree> mems = translate( tree.getMembers() );
        result = make.ClassDeclaration(tree.getModifiers(), tree.getSimpleName(), supertypes, mems);
    }
    
    @Override
    public void visitFunctionValue(MTOperationValue tree) {
        List<MTVar> params = translate( tree.getParameters() );
        MTBlockExpression bodyExpression = translate(tree.getBodyExpression());
        result = make.OperationValue(tree.getJFXReturnType(), params, bodyExpression);
    }

    @Override
    public void visitFunctionDefinition(MTOperationDefinition tree) {
	MTModifiers mods = translate(tree.mods);
	MTType rettype = translate(tree.getJFXReturnType());
	List<MTVar> funParams = translateJFXVarDefs(tree.getParameters());
        MTBlockExpression bodyExpression = translate(tree.getBodyExpression());
        result = make.OperationDefinition(mods, tree.name, rettype, funParams, bodyExpression);
    }

    @Override
    public void visitInitDefinition(MTInitDefinition tree) {
        tree.body = translate(tree.body);
        result = tree;
    }

  public void visitBlockExpression(MTBlockExpression tree) {
	tree.value = translate(tree.value);
        tree.stats = translate(tree.stats);
	result = tree;
    }
  
    public void visitMemberSelector(MTMemberSelector tree) {
        result = tree;
    }
    
    public void visitSequenceEmpty(MTSequenceEmpty tree) {
        result = tree;
    }
    
    public void visitSequenceRange(MTSequenceRange tree) {
        MTExpression lower = translate(tree.getLower());
        MTExpression upper = translate(tree.getUpper());
        result = make.RangeSequence(lower, upper);
    }
    
    public void visitSequenceExplicit(MTSequenceExplicit tree) {
        List<MTExpression> items = translate(tree.getItems());
        result = make.ExplicitSequence(items);
    }

    public void visitSequenceIndexed(MTSequenceIndexed tree) {
        MTExpression sequence = translate(tree.getSequence());
        MTExpression index = translate(tree.getIndex());
        result = make.SequenceIndexed(sequence, index);
    }
    
    public void visitSequenceInsert(MTSequenceInsert tree) {
        MTExpression element = translate(tree.getElement());
        MTExpression sequence = translate(tree.getSequence());
        result = make.SequenceInsert(sequence, element);
    }
    
    public void visitSequenceDelete(MTSequenceDelete tree) {
        MTExpression element = translate(tree.getElement());
        MTExpression sequence = translate(tree.getSequence());
        result = make.SequenceDelete(sequence, element);
    }
        
    @Override
    public void visitOnReplace(MTOnReplace tree) {
	MTVar value = translate(tree.getOldValue());  
        MTBlock body = translate(tree.getBody());
        result = make.OnReplace(value, body);
    }
    
    @Override
    public void visitOnReplaceElement(MTOnReplaceElement tree) {
	MTVar index = translate(tree.getIndex());
	MTVar element = translate(tree.getOldValue());  
        MTBlock body = translate(tree.getBody());
        result = make.OnReplaceElement(index, element, body);
    }
    
    @Override
    public void visitOnInsertElement(MTOnInsertElement tree) {
	MTVar index = translate(tree.getIndex());
	MTVar element = translate(tree.getOldValue());  
        MTBlock body = translate(tree.getBody());
        result = make.OnReplaceElement(index, element, body);
    }
    
    @Override
    public void visitOnDeleteElement(MTOnDeleteElement tree) {
	MTVar index = translate(tree.getIndex());
	MTVar element = translate(tree.getOldValue());  
        MTBlock body = translate(tree.getBody());
        result = make.OnReplaceElement(index, element, body);
    }
    
    public void visitStringExpression(MTStringExpression tree) {
        tree.parts = tree.parts==null? null : translate(tree.parts);
        result = tree;
    }
    
    public void visitPureObjectLiteral(MTPureObjectLiteral tree) {
        tree.ident = translate(tree.ident);
        tree.parts = translate(tree.parts);
        result = tree;
    }
    
    public void visitVarIsObjectBeingInitialized(MTVarIsObjectBeingInitialized tree) {
        //visitVar(tree);
        result = tree;
    }
    
    public void visitSetAttributeToObjectBeingInitialized(MTSetAttributeToObjectBeingInitialized tree) {
        result = tree;
    }
    
    public void visitObjectLiteralPart(MTObjectLiteralPart tree) {
        tree.expr = translate(tree.expr);
        result = tree;
    }  
    
    public void visitTypeAny(MTTypeAny tree) {
        result = tree;
    }
    
    public void visitTypeClass(MTTypeClass tree) {
        result = tree;
    }
    
    public void visitTypeFunctional(MTTypeFunctional tree) {
        tree.params = translate(tree.params);
        tree.restype = translate(tree.restype);
        result = tree;
    }
    
    public void visitTypeUnknown(MTTypeUnknown tree) {
        result = tree;
    }
    
    public void visitVar(MTVar tree) {
        MTType jfxtype;
        List<MTAbstractOnChange> onChanges;
        MTExpression init;
        if (tree.getJFXType() != null) {
            jfxtype = translate(tree.getJFXType());
        } else {
            jfxtype = make.TypeUnknown();
        }
        if (tree.init != null) {
            onChanges = translate(tree.getOnChanges());
        } else {
            onChanges = List.<MTAbstractOnChange>nil();
        }
        if (tree.init != null) {
            init = translate(tree.init);
        } else {
            init = null;
        }
        result = make.Var(tree.name, jfxtype, tree.mods, init, tree.getBindStatus(), onChanges);
    }

    public void visitForExpression(MTForExpression tree) {
        tree.inClauses = translate(tree.inClauses);
        tree.bodyExpr = translate(tree.bodyExpr); 
        result = tree;
    }

    public void visitForExpressionInClause(MTForExpressionInClause tree) {
        tree.var = translate(tree.var);
        tree.seqExpr = translate(tree.seqExpr);
        tree.whereExpr = translate(tree.whereExpr);
        result = tree;
    }
    
    @Override
    public void visitInstanciate(MTInstanciate tree) {
        MTExpression encl = translate(tree.getEnclosingExpression());
        MTExpression clazz = translate(tree.getIdentifier());
        List<MTExpression> args = translate(tree.getArguments());
        MTClassDeclaration def = translate(tree.getClassBody());
        result = make.Instanciate(encl, clazz, args, def);
    }
    
    protected void prettyPrint(MTTree node) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        MTPretty pretty = new MTPretty(osw, false);
        try {
            pretty.printExpr(node);
            osw.flush();
        } catch (Exception ex) {
            System.err.println("Error in pretty-printing: " + ex);
        }
    }
}
