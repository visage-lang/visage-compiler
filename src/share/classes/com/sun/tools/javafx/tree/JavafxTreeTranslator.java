package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;

public class JavafxTreeTranslator extends TreeTranslator implements JavafxVisitor {
    /** Visitor method: translate a list of nodes.
     */
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
        that.mods = translate(that.mods);
        that.declarations = translate(that.declarations);
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
        visitAbstractAttribute(that);
        if (that.init != null) {
            that.init = translate(that.init);
        }
        if (that.onChange != null) {
            that.onChange = translate(that.onChange);
        }
        result = that;
    }
    
    public void visitFunctionDefinition(JFXFunctionDefinition that) {
        visitAbstractFunction(that);
        that.bodyExpression = translate(that.bodyExpression);
        result = that;
    }

    public void visitInitDefinition(JFXInitDefinition that) {
        that.body = translate(that.body);
        result = that;
    }

    // old-style "retro" separate definition/declaration
    
    public void visitRetroAttributeDeclaration(JFXRetroAttributeDeclaration that) {
        visitAbstractAttribute(that);
        result = that;
    }
    
    public void visitRetroFunctionDeclaration(JFXRetroFunctionMemberDeclaration that) {
        visitAbstractFunction(that);
        result = that;
    }
    
    public void visitRetroOperationDeclaration(JFXRetroOperationMemberDeclaration that) {
        visitAbstractFunction(that);
        result = that;
    }
    
    public void visitRetroMemberDefinition(JFXRetroMemberDefinition that) {
        if (that.selector != null) {
            that.selector = translate(that.selector);
        }
        if (that.memtype != null) {
            that.memtype = translate(that.memtype);
        }
        result = that;
    }
    
    public void visitRetroFuncOpDefinition(JFXRetroFuncOpMemberDefinition that) {
        visitRetroMemberDefinition(that);
        
        that.params = translate(that.params);
        that.body = translate(that.body);
        result = that;
    }
    
    public void visitRetroAttributeDefinition(JFXRetroAttributeDefinition that) {
        visitRetroMemberDefinition(that);
        if (that.init != null) {
            that.init = translate(that.init);
        }
        
        result = that;
    }
    
    public void visitRetroFunctionDefinition(JFXRetroFunctionMemberDefinition that) {
        visitRetroFuncOpDefinition(that);
        result = that;
    }
    
    public void visitRetroOperationDefinition(JFXRetroOperationMemberDefinition that) {
        visitRetroFuncOpDefinition(that);
        result = that;
    }
    
    public void visitRetroOperationLocalDefinition(JFXRetroOperationLocalDefinition that) {
        if (that.restype != null) {
            that.restype = translate(that.restype);
        }
        
        that.params = translate(that.params);
        that.body = translate(that.body);
        
        result = that;
    }
    
    public void visitRetroFunctionLocalDefinition(JFXRetroFunctionLocalDefinition that) {
        if (that.restype != null) {
            that.restype = translate(that.restype);
        }
        
        that.params = translate(that.params);
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

    public void visitTriggerOnInsert(JFXTriggerOnInsert that) {
        that.selector = translate(that.selector);
        that.identifier = translate(that.identifier);
        that.block = translate(that.block);
        result = that;
    }    

    public void visitTriggerOnDelete(JFXTriggerOnDelete that) {
        that.selector = translate(that.selector);
        that.identifier = translate(that.identifier);
        that.block = translate(that.block);
        result = that;
    }
    
    public void visitTriggerOnDeleteElement(JFXTriggerOnDeleteElement that) {
        that.selector = translate(that.selector);
        that.identifier = translate(that.identifier);
        that.block = translate(that.block);
        result = that;
    }
    
    public void visitTriggerOnNew(JFXTriggerOnNew that) {
        that.typeIdentifier = translate(that.typeIdentifier);
        if (that.identifier != null) {
            that.identifier = translate(that.identifier);
        }
        
        that.block = translate(that.block);
 
        result = that;
    }
    
    public void visitTriggerOnReplace(JFXTriggerOnReplace that) {
        that.selector = translate(that.selector);
        that.identifier = translate(that.identifier);
        that.block = translate(that.block);
        result = that;
    }
    
    public void visitTriggerOnReplaceElement(JFXTriggerOnReplaceElement that) {
        that.selector = translate(that.selector);
        that.identifier = translate(that.identifier);
        that.elementIdentifier = translate(that.elementIdentifier);
        that.block = translate(that.block);
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
    
    public void visitVarInit(JFXVarInit that) {
        that.initializer = translate(that.initializer);
        that.type = translate(that.type);
        result = that;
    }
    
    public void visitVarStatement(JFXVarStatement that) {
        visitVar(that);
        result = that;
    }
    
    public void visitVar(JFXVar that) {
        that.type = translate(that.type);
        result = that;
    }

    public boolean shouldVisitRemoved() {
        return false;
    }
    
    public boolean shouldVisitSynthetic() {
        return true;
    }
}
