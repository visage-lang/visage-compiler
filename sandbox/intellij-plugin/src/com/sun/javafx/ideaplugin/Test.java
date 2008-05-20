package com.sun.javafx.ideaplugin;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import com.sun.tools.javafx.antlr.v3Lexer;
import com.sun.tools.javafx.antlr.v3Parser;
import com.sun.tools.javac.util.Context;
import com.intellij.psi.tree.IElementType;

/**
 * Test
 *
 * @author Brian Goetz
 */
public class Test {
    public static void main(String[] args) throws RecognitionException {
        String file = "var a = 1; var b = a+1; var c = (a+b)+1;";
        ANTLRStringStream input = new ANTLRStringStream(file);
        v3Lexer lexer = new v3Lexer(new Context(), input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        v3Parser parser = new v3Parser(tokens);
        v3Parser.module_return comReturn = parser.module();
        traverse((CommonTree) comReturn.getTree(), 0);
    }

    private static void traverse(CommonTree tree, int depth) {
        for (int i=0; i<depth; i++)
            System.out.print(" ");
        System.out.printf("%s(%b) @ %d:%d%n", tree.token == null ? Integer.toString(tree.getType()) : FxTokens.getElement(tree.getType()), tree.token == null, tree.getTokenStartIndex(), tree.getTokenStopIndex());
        for (int i=0; i<tree.getChildCount(); i++)
            traverse((CommonTree) tree.getChild(i), depth+1);
    }
}
