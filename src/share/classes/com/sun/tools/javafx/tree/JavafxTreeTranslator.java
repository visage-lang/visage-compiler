package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;

import java.io.OutputStreamWriter;

public class JavafxTreeTranslator extends TreeTranslator implements JavafxVisitor {
    /** Visitor method: translate a list of nodes.
     */
    @Override
    public <T extends JCTree> List<T> translate(List<T> trees) {
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

    public void visitClassDeclaration(JFXClassDeclaration that) {
        visitClassDef(that);
        that.supertypes = translate(that.supertypes);
        result = that;
    }
    
    public void visitAbstractMember(JFXAbstractMember that) {
        that.modifiers = translate(that.modifiers);
        if (that.memtype != null) {
            that.memtype = translate(that.memtype);
        } 
        result = that;
    }
    
    public void visitAbstractAttribute(JFXAbstractAttribute that) {
        visitAbstractMember(that);
        if (that.inverseOrNull != null) {
            that.inverseOrNull = translate(that.inverseOrNull);
        }      
        if (that.orderingOrNull != null) {
            that.orderingOrNull = translate(that.orderingOrNull);
        }
        result = that;
    }
    
    public void visitAbstractFunction(JFXAbstractFunction that) {
        visitAbstractMember(that);
        that.params = translate(that.params);
        result = that;
    }
    
    public void visitAttributeDefinition(JFXAttributeDefinition that) {
        visitVar(that);
        if (that.init != null) {
            that.init = translate(that.init);
        }
        if (that.onChange != null) {
            that.onChange = translate(that.onChange);
        }
        result = that;
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition that) {
        visitMethodDef(that);
        that.bodyExpression = translate(that.bodyExpression);
        result = that;
    }

    @Override
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement that) {
        visitOperationDefinition(that.funcDef);
        result = that;
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        that.body = translate(that.body);
        result = that;
    }

  public void visitBlockExpression(JFXBlockExpression tree) {
	tree.value = translate(tree.value);
        tree.stats = translate(tree.stats);
	result = tree;
    }
  
    public void visitDoLater(JFXDoLater that) {
        that.body = translate(that.body);
        result = that;
    }

    public void visitMemberSelector(JFXMemberSelector that) {
        result = that;
    }
    
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
        result = that;
    }
    
    public void visitSequenceRange(JFXSequenceRange that) {
        that.lower = translate(that.lower);
        that.upper = translate(that.upper);
        result = that;
    }
    
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        that.items = translate(that.items);
        result = that;
    }

    public void visitStringExpression(JFXStringExpression that) {
        that.parts = that.parts==null? null : translate(that.parts);
        result = that;
    }
    
    public void visitPureObjectLiteral(JFXPureObjectLiteral that) {
        that.ident = translate(that.ident);
        that.parts = translate(that.parts);
        result = that;
    }
    
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that) {
        visitVar(that);
        result = that;
    }
    
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
        result = that;
    }
    
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.expr = translate(that.expr);
        result = that;
    }  
    
    public void visitTypeAny(JFXTypeAny that) {
        visitType(that);
        result = that;
    }
    
    public void visitTypeClass(JFXTypeClass that) {
        visitType(that);
        result = that;
    }
    
    public void visitTypeFunctional(JFXTypeFunctional that) {
        that.params = translate(that.params);
        that.restype = translate(that.restype);
        result = that;
    }
    
    public void visitTypeUnknown(JFXTypeUnknown that) {
        visitType(that);
        result = that;
    }
        
    public void visitType(JFXType that) {
        result = that;
    }
    
    public void visitVar(JFXVar that) {
        if (that.jfxtype != null) {
            that.jfxtype = translate(that.jfxtype);
        }
        if (that.init != null) {
            that.init = translate(that.init);
        }
        result = that;
    }

    public void visitForExpression(JFXForExpression that) {
        that.var = translate(that.var);
        that.seqExpr = translate(that.seqExpr);
        that.whereExpr = translate(that.whereExpr);
        that.bodyExpr = translate(that.bodyExpr); 
        result = that;
    }
    
    public boolean shouldVisitRemoved() {
        return false;
    }
    
    public boolean shouldVisitSynthetic() {
        return true;
    }

    protected void prettyPrint(JCTree node) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.printExpr(node);
            osw.flush();
        } catch (Exception ex) {
            System.err.println("Error in pretty-printing: " + ex);
        }
    }
}
