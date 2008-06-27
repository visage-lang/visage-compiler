/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.tree;

import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javafx.comp.BlockExprAttr;
import com.sun.tools.javafx.comp.BlockExprEnter;
import com.sun.tools.javafx.comp.BlockExprMemberEnter;
import com.sun.tools.javafx.comp.JavafxPrepForBackEnd;

/**
 *
 * @author Per Bothner
 * @author Robert Field
 */
public class BlockExprJCBlockExpression extends JCExpression {
    
   public static final int BLOCK_EXPR_TAG = JCTree.LETEXPR + 1;
   
        public long flags;
        public List<JCStatement> stats;
    public JCExpression value;
    /** Position of closing brace, optional. */
    public int endpos = Position.NOPOS;

    public BlockExprJCBlockExpression(long flags, List<JCStatement> stats, JCExpression value) {
        this.stats = stats;
        this.flags = flags;
        this.value = value;
    }

    @Override
    public void accept(Visitor v) {
        // Kludge
        if (v instanceof Pretty) {
            JavaPretty.visitBlockExpression((Pretty) v, this);
        } else if (v instanceof BlockExprAttr) {
            ((BlockExprAttr) v).visitBlockExpression(this);
        } else if (v instanceof BlockExprEnter) {
            ((BlockExprEnter) v).visitBlockExpression(this);
        } else if (v instanceof BlockExprMemberEnter) {
            ((BlockExprMemberEnter) v).visitBlockExpression(this);
        } else if (v instanceof JavafxPrepForBackEnd) {
            ((JavafxPrepForBackEnd) v).visitBlockExpression(this);
        } else if (v instanceof TreeScanner) {
            ((TreeScanner) v).scan(stats);
            ((TreeScanner) v).scan(value);
        } else if (v instanceof TreeTranslator) {
            stats = ((TreeTranslator) v).translate(stats);
            value = ((TreeTranslator) v).translate(value);
            ((TreeTranslator) v).result = this;
        } else {
            v.visitTree(this);
        }
    }

    public Kind getKind() {
        return Kind.BLOCK;
    }

    public List<JCStatement> getStatements() {
        return stats;
    }

    public boolean isStatic() {
        return (flags & Flags.STATIC) != 0;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> v, D d) {
        throw new UnsupportedOperationException("This is a back-end node and should not be visable to the API");
    }

    @Override
    public int getTag() {
        return BLOCK_EXPR_TAG;
    }
}
